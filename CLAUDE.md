# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build debug APK (clean output = success)
./gradlew assembleDebug --quiet

# Install to connected device
./gradlew installDebug

# Run unit tests
./gradlew testDebugUnitTest

# Full clean rebuild
./gradlew clean assembleDebug
```

A clean build produces **no output** with `--quiet`. Any output indicates errors.

## Architecture

**Clean Architecture + MVI** with three layers and a strict one-way dependency rule:

```
ui/       ──►  domain/  ◄──  data/
              (pure Kotlin)
```

`ui` and `data` never import from each other. `domain` has zero Android/Compose dependencies.

---

### Domain Layer (`domain/`)

#### Models

All pure Kotlin data classes — no Android imports.

| Model | Key fields | Notes |
|---|---|---|
| `SolarDate` | `day`, `month`, `year` | Has `today()` (Asia/Ho_Chi_Minh zone), `plusDays`, `minusDays` |
| `LunarDate` | `day`, `month`, `year`, `isLeapMonth` | `isLeapMonth` distinguishes tháng nhuận |
| `CanChi` | `canNam/chiNam`, `canThang/chiThang`, `canNgay/chiNgay` | Six sexagenary cycle components |
| `DailyMetadata` | `solar`, `lunar`, `canChi`, `auspiciousHours`, `solarTerm` | Full metadata bundle for one day |
| `DayCell` | `solar`, `lunar`, `isCurrentMonth`, `isToday`, `holiday` | One cell in the 42-cell calendar grid |
| `HourInfo` | `name`, `timeRange`, `isAuspicious` | One of 12 two-hour slots (Tý → Hợi) |

#### Repository Interface

`CalendarRepository` is the single boundary between domain and data. All methods are `suspend` and live in `domain/repository/`. The interface exposes:

- `getDailyMetadata(date)` — bundles solar→lunar + Can-Chi + auspicious hours + solar term in one call
- `getDaysInMonth(year, month)` — produces exactly 42 `DayCell`s (6 rows × 7 cols, Monday-first, overflow days included)
- `convertSolarToLunar(solar)` / `convertLunarToSolar(lunar)`
- `calculateCanChi(date)` / `getAuspiciousHours(date)` / `getSolarTerm(date)`

#### Use Cases

One class per repository method, in `domain/usecase/`. Each wraps a single `repository.*` call in `runCatching` and returns `Result<T>`. Use cases are `factory`-scoped in Koin (new instance per ViewModel injection).

```kotlin
class GetDailyMetadataUseCase(private val repository: CalendarRepository) {
    suspend operator fun invoke(date: SolarDate): Result<DailyMetadata> =
        runCatching { repository.getDailyMetadata(date) }
}
```

---

### Data Layer (`data/`)

#### Data Sources

Two independent sources behind one repository. Neither source knows about the other.

**`LunarMathAlgorithmDataSource` / `LunarMathAlgorithmDataSourceImpl`**

Kotlin port of the Hồ Ngọc Đức lunar calendar algorithm. All calculations are pure arithmetic — no I/O, no network.

| Method | What it does |
|---|---|
| `solarToLunar(solar)` | Gregorian → Vietnamese lunar date |
| `lunarToSolar(lunar)` | Vietnamese lunar → Gregorian; throws if leap month doesn't exist |
| `calculateCanChi(solar)` | Delegates to the two-arg overload after calling `solarToLunar` |
| `calculateCanChi(solar, lunar)` | Computes all 6 Can-Chi components from a pre-fetched `LunarDate` — avoids double conversion |
| `getAuspiciousHours(solar)` | Returns 12 `HourInfo` entries (Tý 23:00 → Hợi 23:00) with auspiciousness flag |

`LunarMath` (internal `object` in `data/source/LunarMath.kt`) exposes `jdFromDate` and `sunLongitudeDeg` as `internal fun` — shared by both data sources. Do not make these `public`.

**`StaticAssetDataSource` / `StaticAssetDataSourceImpl`**

In-memory lookup tables — no files, no assets, no I/O.

| Method | What it does |
|---|---|
| `getSolarTerm(solar)` | Detects one of 24 solar terms (Tiết khí) by comparing sun longitude zone today vs. yesterday |
| `getHoliday(solar, lunar)` | Returns holiday name for national holidays (solar) and lunar holidays (Tết, Giỗ Tổ); null otherwise |

#### Repository Implementation

`CalendarRepositoryImpl` wires the two sources. All operations dispatch to `Dispatchers.Default` via `withContext`. Key pattern in `getDailyMetadata`:

```kotlin
val lunar = algorithmSource.solarToLunar(date)
val canChi = algorithmSource.calculateCanChi(date, lunar)  // pass pre-computed LunarDate
```

`getDaysInMonth` builds a 42-cell grid: finds the Monday on or before the 1st of the month (ISO weekday - 1 offset), then maps 0..41 days from that start date. Each cell calls `solarToLunar` + `getHoliday`.

---

### UI Layer (`ui/`)

#### App Entry Point

```
App (Application) → startKoin(appModule)
MainActivity → enableEdgeToEdge → AppScaffold
AppScaffold → rememberNavBackStack(TodayDestination()) + Scaffold(bottomBar = GlassBottomNavBar)
AppNavDisplay → NavDisplay with entryProvider mapping destinations → screens
```

`MainActivity` locks orientation to portrait. `AppScaffold` owns the single `NavBackStack` instance and passes it to both `GlassBottomNavBar` (for tab selection) and `AppNavDisplay` (for content rendering).

#### MVI Contract Pattern

Every screen defines a `XxxContract` object with three nested types:

```kotlin
object XxxContract {
    @Immutable
    data class State(...)        // all vals; defaults = initial/idle state

    sealed class Intent { ... }  // every user action that the screen can express

    sealed class Effect { ... }  // one-shot events that cannot live in state
                                 // (navigation push, scroll command)
                                 // omit this class entirely if the screen has none
}
```

`State` rules:
- Annotated `@Immutable` — mandatory for Compose stability
- All list fields use `ImmutableList<T>` (`kotlinx-collections-immutable`), never `List<T>`
- Default values represent the idle/loading state before any data arrives

`Effect` is delivered via `Channel<Effect>(BUFFERED)` + `receiveAsFlow()`. The screen collects it in `LaunchedEffect(Unit)`.

#### ViewModel Pattern

```kotlin
class XxxViewModel(private val useCase: XxxUseCase) : ViewModel() {
    private val _state = MutableStateFlow(XxxContract.State())
    val state: StateFlow<XxxContract.State> = _state.asStateFlow()

    // Only when the screen needs effects:
    private val _effect = Channel<XxxContract.Effect>(Channel.BUFFERED)
    val effect: Flow<XxxContract.Effect> = _effect.receiveAsFlow()

    fun onIntent(intent: XxxContract.Intent) {
        when (intent) { ... }
    }
}
```

**Concurrency rule** — any async operation that can be triggered multiple times in rapid succession (navigation, month change) must cancel the in-flight job before launching a new one. Failing to do so causes stale results to overwrite fresh ones:

```kotlin
private var loadJob: Job? = null

private fun load(param: X) {
    loadJob?.cancel()
    loadJob = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null) }
        useCase(param)
            .onSuccess { data -> _state.update { it.copy(isLoading = false, result = data) } }
            .onFailure { err -> _state.update { it.copy(isLoading = false, error = err.message) } }
    }
}
```

**ViewModel scoping**:
- `TodayViewModel` and `CalendarViewModel` — scoped to their `NavEntry` via `rememberViewModelStoreNavEntryDecorator`; created fresh each time the destination enters the back stack
- `ConverterViewModel` and `SettingsViewModel` — scoped to the `Activity` via `koinViewModel(viewModelStoreOwner = activity)`; survive tab switches and retain state across the session

#### Screen Composable Pattern

```kotlin
// Public entry point — thin: only VM wiring + effect collection
@Composable
fun XxxScreen() {
    val viewModel: XxxViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {                          // only if the screen has effects
        viewModel.effect.collect { effect ->
            when (effect) { ... }
        }
    }

    XxxContent(state = state, onIntent = viewModel::onIntent)
}

// Private stateless composable — all UI lives here; no VM reference
@Composable
private fun XxxContent(
    state: XxxContract.State,
    onIntent: (XxxContract.Intent) -> Unit,
) { ... }
```

`CalendarScreen` is the only exception: it receives `backStack: NavBackStack<NavKey>` as a parameter so it can push `TodayDestination` in its effect handler.

#### UI Models (`ui/model/`)

Domain models pass through a mapping layer before reaching the UI. The UI model adds presentation-only fields that the domain doesn't know about:

| Domain | UI model | Added by mapping |
|---|---|---|
| `DayCell` | `DayCellUiModel` | `isSelected` (compared against `selectedDate` in `CalendarViewModel`) |
| `DailyMetadata` | `DailyMetadataUiModel` | `auspiciousHours` converted to `ImmutableList<HourInfoUiModel>` |
| `HourInfo` | `HourInfoUiModel` | Mirrors domain; typed for Compose stability |

All UI models are annotated `@Immutable`. Mapping functions are `fun DomainType.toUiModel(...)` extension functions in the same file as the UI model class.

## Navigation

**Navigation 3 v1.0.1** (`androidx.navigation3`). Entry point: `AppNavDisplay.kt`.

- `NavBackStack<NavKey>` is the single source of truth for navigation state
- Tab navigation uses `NavBackStack.navigateToTab()` (custom extension in `NavExtensions.kt`) — pops to existing tab if present, resets to root + pushes otherwise
- Destinations are `@Serializable` data classes/objects implementing `NavKey` (in `AppDestinations.kt`)
- `TodayDestination(day=0, month=0, year=0)` means "today"; non-zero means a specific date from the calendar
- Tab animation was intentionally removed — do not add slide/fade transitions between tabs

`CalendarScreen` receives `backStack` as a parameter to push `TodayDestination` when a day cell is tapped.

## Dependency Injection

**Koin 4.0.4**. Single module in `AppModule.kt`:

```kotlin
single<Interface> { Impl() }        // singletons (data sources, repository)
factory { UseCase(get()) }          // new instance per injection (use cases)
viewModel { XxxViewModel(get()) }   // scoped to NavEntry by rememberViewModelStoreNavEntryDecorator
viewModel { (p: Type?) -> XxxViewModel(get(), p) }  // with parametersOf(...)
```

## Compose Rules

- **Stability**: all `State` classes annotated `@Immutable`; lists always `ImmutableList<T>` — never plain `List` in state
- **Animations**: every state branch (`if`/`when`) that changes visible content uses `AnimatedContent` or `AnimatedVisibility` — no hard snaps
  - Standard content/loading/error: `AnimatedContent(contentKey)` with `fadeIn(tween(220)) togetherWith fadeOut(tween(180))`
  - Date/header text changes: `slideInVertically { it/3 } + fadeIn(tween(200)) togetherWith slideOutVertically { -it/3 } + fadeOut(tween(150))`
  - Show/hide rows: `AnimatedVisibility` with `fadeIn + expandVertically` / `fadeOut + shrinkVertically`
- **Recomposition**: expensive derived values go in `remember(key) { ... }`; computed booleans that gate recomposition go in `derivedStateOf`
- **Splitting**: composables over ~80 LOC extract private sub-composables in the same file; no new files unless splitting to a new feature

## Design System

**Theme**: "Classic & Glass Liquid" — dark Vietnamese red-brown + gold accents.

Key colors from `theme/Color.kt`:

| Token | Use |
|---|---|
| `BaTrauDark` / `NauToi` | Background gradient (top → bottom) |
| `GoldAccent` | Active elements, auspicious hours, icons |
| `IvoryWhite` | Primary text on dark background |
| `GlassTint` / `GlassBorder` | Glass card fill / border (use `GlassCard` component) |
| `WeekendColor` | Saturday/Sunday in calendar grid |
| `HolidayDot` | Holiday indicator dot on calendar cells |

`GlassCard` in `ui/components/` is the standard card container. Use it instead of a raw `Surface` or `Box` with manual border+background.

## Settings Persistence

`SharedPreferences("app_settings")` injected via Koin. `SettingsViewModel` reads/writes directly — no Room, no DataStore in Phase 1.
