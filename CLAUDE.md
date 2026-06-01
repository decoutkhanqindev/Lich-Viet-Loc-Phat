# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Android CLI Commands

`minSdk 26`, `compileSdk 36`, `targetSdk 36`, JDK 17. Uses the `android` CLI tool (not raw `adb`).

```bash
# Build + deploy debug APK to connected device
./gradlew assembleDebug && android run --apks=app/build/outputs/apk/debug/app-debug.apk

# Run with specific activity
android run --apks=app/build/outputs/apk/debug/app-debug.apk --activity=.MainActivity

# Capture screenshot to file
android screen capture --output=screenshot.png

# Inspect live UI layout as JSON (faster than screenshot for debugging UI)
android layout --pretty

# Inspect layout and save to file
android layout --pretty --output=layout.json

# Show only layout changes since last call
android layout --diff

# Manage emulators
android emulator list
android emulator create --name=Pixel9 --sdk=34
android emulator start <avd-name>
android emulator stop <avd-name>

# Search Android documentation
android docs search "Navigation3 NavBackStack"
android docs search "Compose recomposition stability"

# List installed SDK packages
android sdk list

# Install SDK package
android sdk install platforms/android-36
```

### Gradle (not covered by android CLI)

```bash
# Run unit tests
./gradlew testDebugUnitTest

# Clean build cache
./gradlew clean

# Stop Gradle daemon (if build hangs)
./gradlew --stop
```

### ADB fallback (for operations not covered by android CLI)

```bash
# Stream logcat filtered to this app
adb logcat --pid=$(adb shell pidof -s com.decoutkhanqindev.lich_viet_loc_phat)

# Clear app data (reset SharedPreferences)
adb shell pm clear com.decoutkhanqindev.lich_viet_loc_phat

# Print current SharedPreferences (app_settings)
adb shell run-as com.decoutkhanqindev.lich_viet_loc_phat cat shared_prefs/app_settings.xml
```

## Architecture

**Clean Architecture + MVI** in a single `:app` module.

### Layer Structure

```
domain/          — pure Kotlin, no Android deps
  model/         — SolarDate, LunarDate, CanChi, DayCell, DailyMetadata, HourInfo
  repository/    — CalendarRepository interface
  usecase/       — one class per operation, delegates to CalendarRepository

data/
  source/lunar_math_algorithm/   — LunarMathAlgorithmDataSourceImpl: pure math (solar↔lunar conversion, can-chi, auspicious hours)
  source/static_asset/           — StaticAssetDataSourceImpl: lookup tables (solar terms, holidays)
  repository/                    — CalendarRepositoryImpl: composes both sources, runs on Dispatchers.Default

ui/
  model/         — UiModel wrappers (@Immutable data classes, ImmutableList for Compose stability)
  screens/{screen}/
    state/       — {Screen}State, {Screen}Intent, {Screen}Effect
    {Screen}ViewModel.kt
    {Screen}Screen.kt   — thin: gets VM, calls LaunchedEffect, passes to Content
    {Screen}Content.kt  — all Compose UI, receives state + onIntent lambda
  components/    — shared composables (GlassBottomNavBar, GlassTopAppBar, GlassCard, Common)
  AppScaffold.kt — root: creates backStack, hosts Scaffold + NavDisplay

navigation/
  AppDestinations.kt  — NavKey sealed hierarchy
  AppNavDisplay.kt    — NavDisplay with entry decorators
  NavExtensions.kt    — navigateToTab()

di/
  AppModule.kt   — single Koin module for everything
```

### Data Flow

```
StaticAssetDataSourceImpl ─┐
                           ├→ CalendarRepositoryImpl → UseCase → ViewModel → UiModel → Composable
LunarMathAlgorithmDataSourceImpl ─┘
```

`CalendarRepository` wraps all coroutine dispatching — data sources are pure synchronous functions.

### Navigation

Uses **Navigation 3** (`androidx.navigation3`). The backstack is `rememberNavBackStack(TodayDestination())` created in `AppScaffold`.

- `TodayDestination(day, month, year)` — **data class**, carries an optional date (`day==0` means today)
- `CalendarDestination`, `ConverterDestination`, `SettingsDestination` — **data objects**

**`navigateToTab`** pops back to the existing tab entry without destroying root. Never use `clear()` + `add(root)` — that would kill the Today entry and recreate its ViewModel.

### ViewModel Scoping

All 4 tab ViewModels are **Activity-scoped** via `koinActivityViewModel()` to survive tab navigation. None are ever recreated on tab switch.

`TodayScreen` additionally uses `LaunchedEffect(initialDate)` to call `viewModel.setInitialDate(date)` when navigated from Calendar with a specific date — only fires when `initialDate` changes.

### MVI Pattern

Each screen has:
- `State` — `@Immutable data class`, default `isLoading=true`
- `Intent` — sealed interface or class of user actions
- `Effect` — one-shot events sent via `Channel<Effect>`, collected via `ObserveOnLifecycleOwner` in the Screen composable (not Content)

### Settings Persistence

`CalendarViewModel` reads/writes `SharedPreferences("app_settings")` directly and registers a listener for live updates. The key `show_can_chi_on_cell` controls whether can-chi labels appear in calendar grid cells.

## UI / Theme

Theme: Vietnamese dark — `BaTrauDark` → `NauToi` gradient, `GoldAccent` for active/auspicious, `IvoryWhite` for body text.

Key colors in `theme/Color.kt`:
- `GoldAccent` — active tab, auspicious hours, today glow
- `HolidayDot` — holiday text/dot (red-pink)
- `SolarTermColor` — solar term text (jade green)
- `WeekendColor` — Saturday/Sunday labels

Calendar cell indicator priority (when `isCurrentMonth`): **holiday name** > **solar term name** > **lunar 1st/15th dot**.

### Shared Composables

- `Modifier.onClick(shape, ripple)` — scale-down press animation (85%), use instead of raw `clickable` for interactive elements
- `ObserveOnLifecycleOwner` — collect a Flow (usually effects) scoped to lifecycle STARTED state
- `GlassCard` — frosted glass card with `GlassTint` background + `GlassBorder`

## Key Domain Notes

- `SolarDate.today()` uses `Asia/Ho_Chi_Minh` timezone
- `DayCell` grid is always 42 cells (6 rows × 7 cols); overflow days from adjacent months have `isCurrentMonth = false`
- `LunarDate.isLeapMonth` is respected in `getHoliday()` — leap months never match lunar holidays
- `CanChi` carries: `canNgay/chiNgay` (day), `canThang/chiThang` (month), `canNam/chiNam` (year)
