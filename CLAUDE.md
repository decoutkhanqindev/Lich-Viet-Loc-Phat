# CLAUDE.md

## Android CLI Commands

Dùng skill `/android-cli` để xem đầy đủ các lệnh.

## Android Gradle Commands
Dùng skill `/android-gradle` để xem đầy đủ các lệnh.

## Git

Không được tự ý chạy các lệnh sau khi chưa được phép:
- `git push` / `git push --force`
- `git reset --hard`
- `git rebase`
- `git merge`
- `git branch -D`
- `git commit` (chỉ commit khi user yêu cầu rõ ràng)

## Project Description

**Lịch Việt Lộc Phát** — Ứng dụng lịch âm Việt Nam trên Android.

- Package: `com.decoutkhanqindev.lich_viet_loc_phat`
- minSdk: 26 · compileSdk/targetSdk: 36 · Kotlin 2.3.21 · AGP 9.2.1
- Single module: `:app`

**Tính năng hiện tại:**
- Xem thông tin lịch âm hôm nay (can chi, tiết khí, giờ hoàng đạo, ngày lễ)
- Lịch tháng dạng grid
- Glance App Widget hiển thị ngày âm
- Cài đặt (hiện/ẩn can chi trên ô ngày)

## Architecture

**Clean Architecture + MVI**, một module `:app`.

### Cấu trúc package

```
com.decoutkhanqindev.lich_viet_loc_phat/
├── domain/
│   ├── model/          # SolarDate, LunarDate, DailyMetadata, ...
│   ├── repository/     # CalendarRepository, SettingsRepository (interfaces)
│   └── usecase/        # GetDailyMetadataUseCase, ObserveShowCanChiOnCellUseCase, ...
├── data/
│   ├── source/         # LunarMathAlgorithmDataSource, StaticAssetDataSource + Impls
│   └── repository/     # CalendarRepositoryImpl, SettingsRepositoryImpl
├── ui/
│   ├── base/           # BaseViewModel<S, I, E>
│   ├── model/          # DayCellUiModel, DailyMetadataUiModel, ...
│   ├── screens/        # {today,calendar,settings}/ — Screen + Content + ViewModel + state/
│   └── components/     # AppTopBar, AppBottomNavBar, ...
├── navigation/         # AppDestinations, AppNavDisplay
├── theme/              # Color, Shape, Type, Brush, Theme
├── di/                 # AppModule
├── widget/             # CalendarWidget (Glance)
├── App.kt
└── MainActivity.kt
```

### Tech Stack

| Thư viện | Version | Vai trò |
|---|---|---|
| Jetpack Compose BOM | 2026.05.01 | UI framework |
| Navigation 3 | 1.1.2 | Navigation |
| Koin | 4.2.1 | Dependency Injection |
| Coroutines | 1.11.0 | Async |
| kotlinx.collections.immutable | 0.4.0 | ImmutableList cho Compose stability |
| Timber | 5.0.1 | Logging |
| Glance | 1.1.1 | App Widget |
| Material3 | BOM | UI components |

### MVI Pattern

**State** — `@Immutable data class`, default values, list dùng `ImmutableList`:
```kotlin
@Immutable
data class XxxState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val items: ImmutableList<ItemUiModel> = persistentListOf(),
)
```

**Intent** — `sealed interface`:
```kotlin
sealed interface XxxIntent {
    data object DoSomething : XxxIntent
    data class SelectItem(val id: Int) : XxxIntent
}
```

**Effect** (one-time events) — `sealed interface`, phát qua `Channel<Effect>` hoặc `SharedFlow<Effect>`:
```kotlin
sealed interface XxxEffect {
    data class NavigateTo(val destination: NavKey) : XxxEffect
}
```

**ViewModel** — extend `BaseViewModel<S, I, E>`, truyền `initialState` qua constructor:
```kotlin
class XxxViewModel(
    private val getSomething: GetSomethingUseCase,
) : BaseViewModel<XxxState, XxxIntent, XxxEffect>(
    initialState = XxxState(value = getSomething().getOrDefault(defaultValue))
) {
    override fun onIntent(intent: XxxIntent) { ... }
    // dùng updateState { copy(...) } và sendEffect(XxxEffect.Something)
}
```

`BaseViewModel` cung cấp sẵn: `state`, `effect`, `updateState { }`, `sendEffect()`, `onCleared()`.

### Screens vs Content

- `XxxScreen.kt` — lấy ViewModel qua `koinActivityViewModel()` hoặc `koinViewModel()`, collect state, gọi Content
- `XxxContent.kt` — pure composable, nhận `state` + `onIntent`, không biết ViewModel

```kotlin
// Screen
@Composable
fun TodayScreen() {
    val viewModel: TodayViewModel = koinActivityViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    TodayContent(state = state, onIntent = viewModel::onIntent)
}
```

### Navigation 3

Destinations là `@Serializable data class/object : NavKey` trong `navigation/AppDestinations.kt`.
`AppNavDisplay` dùng `NavDisplay` + `rememberDecoratedNavEntries` với 2 decorators:
- `rememberSaveableStateHolderNavEntryDecorator()`
- `rememberViewModelStoreNavEntryDecorator()`

`NavBackStack` được tạo và quản lý ở `AppScaffold`.

### Dependency Injection (Koin)

Một module duy nhất `appModule` trong `di/AppModule.kt`:
- `viewModel { XxxViewModel(get()) }` — ViewModels
- `single<Interface> { Impl() }` — DataSources, Repository
- `factory { XxxUseCase(get()) }` — Use Cases

Khởi tạo trong `App.kt` qua `startKoin { androidContext(this@App); modules(appModule) }`.

### Data Layer

- CPU-bound work phải dùng `withContext(Dispatchers.Default)` trong Repository
- **Suspend UseCases** (calendar, algorithm) — `suspend operator fun invoke(): Result<T>` via `runCatching`
- **Non-suspend UseCases** (settings — sync SharedPreferences) — `operator fun invoke(): Result<T>` via `runCatching`
- **Observe UseCases** — trả về `Flow<T>` trực tiếp, không wrap `Result` (Flow tự handle error)
- ViewModel xử lý `onSuccess / onFailure` hoặc `.getOrDefault(value)`

`SettingsRepository` dùng `callbackFlow` + `SharedPreferences.OnSharedPreferenceChangeListener` để expose `Flow<Boolean>`, listener tự unregister qua `awaitClose`.

## UI Conventions

### remember & derivedStateOf

- `remember { }` — cache giá trị tính toán qua recomposition, tránh tính lại mỗi lần compose.
- `remember(key) { }` — recompute khi `key` thay đổi; dùng khi input là tham số composable.
- `derivedStateOf { }` — dùng khi input là **Compose `State` objects** (`mutableStateOf`, `collectAsStateWithLifecycle`, ...) và chỉ muốn recompose khi kết quả thực sự thay đổi.

```kotlin
// ✅ Đúng: count là mutableStateOf → derivedStateOf observe được
var count by remember { mutableStateOf(0) }
val isVisible by remember { derivedStateOf { count > 0 } }

// ❌ Sai: items là tham số bình thường → dùng remember(items) thay thế
val isVisible by remember { derivedStateOf { items.isNotEmpty() } }
val isVisible = remember(items) { items.isNotEmpty() } // ✅
```

### Composable Stability

- List trong State phải là `ImmutableList<T>` (từ `kotlinx.collections.immutable`)
- State class phải có annotation `@Immutable`
- UiModel classes nên là `@Immutable data class`

### Observing Effects

Dùng `ObserveOnLifecycleOwner` để collect effect trong Screen:
```kotlin
ObserveOnLifecycleOwner { viewModel.effect.collect { effect -> ... } }
```
