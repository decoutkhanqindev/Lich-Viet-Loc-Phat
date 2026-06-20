# CLAUDE.md

## Project

**Lịch Việt Lộc Phát** — Ứng dụng lịch âm Việt Nam (Android).

- Package: `com.decoutkhanqindev.lich_viet_loc_phat` · Single module `:app`
- minSdk 26 · compile/target 36 · Kotlin 2.3.21 · AGP 9.2.1
- Tính năng: lịch âm hôm nay (can chi, tiết khí, giờ hoàng đạo), lịch tháng grid, Glance widget, cài đặt.

## Commands

### Android CLI (`/android-cli` để xem đầy đủ)

| Lệnh | Mô tả |
|---|---|
| `android info` | Thông tin môi trường (vị trí SDK, …) |
| `android run --device=<serial>` | Deploy & chạy app |
| `android screen capture -o <file.png>` | Chụp màn hình thiết bị |
| `android layout -p` | Dump cây UI (JSON) — debug UI nhanh hơn screenshot |
| `android emulator list/start/stop` | Quản lý AVD |
| `android sdk list --all / install <pkg>` | Quản lý gói SDK |
| `android docs search "<keyword>"` | Tra cứu tài liệu Android chính thống |

> Cần info API/Android mới nhất → dùng `android docs search`, đừng đoán.

### Gradle (`/android-gradle` để xem đầy đủ)

Luôn dùng wrapper: `.\gradlew` (PowerShell) / `./gradlew` (bash).

| Mục đích | Lệnh |
|---|---|
| Verify nhanh (ưu tiên) | `.\gradlew :app:compileDebugKotlin` |
| Build / cài debug | `.\gradlew assembleDebug` · `installDebug` |
| Clean (+build) | `.\gradlew clean [assembleDebug]` |
| Unit test | `.\gradlew testDebugUnitTest` · `test --tests "*.ClassName"` |
| Dependency `:app` | `.\gradlew :app:dependencies` |

> Build treo → `.\gradlew --stop`; OOM → `org.gradle.jvmargs=-Xmx4g`; iterate nhanh → `-x lint -x test`.

## Hard Rules ⚠️

### Git — KHÔNG tự chạy khi chưa được phép

`git push` / `--force` · `git reset --hard` · `git rebase` · `git merge` · `git branch -D` · `git commit` (chỉ commit khi user yêu cầu rõ ràng).

### Coding rules (toàn bộ `presentation/`)

1. **Không hardcode color/brush/shape** → định nghĩa trong `theme/{Color,Brush,Shape}.kt`. Naming **PascalCase mô tả giá trị**, alpha = `<Base>Alpha<percent>` (vd `VangDongAlpha70`, `RoundedCornerShape12dp`).
2. **Không top-level var dùng-một-lần** → inline. Giữ token (rule 1) và type/data holder.
3. **Không comment** ở UI; chỉ comment ở `data/` khi logic thực sự khó (thuật toán âm lịch).
4. **Không hardcode text** → `strings.xml`, lấy qua `stringResource(R.string.x, …)` (Glance: `LocalContext.current.getString(…)`). Placeholder `%1$d`/`%1$s`. **Ngoại lệ:** nội suy thuần số/dấu (`"${date.day}"`, `"$can $chi"`) và error string trong ViewModel.

### Banned patterns

Hilt · LiveData · XML layout · `GlobalScope` · `Thread.sleep` · `Log.*` (dùng Timber) · dark theme · landscape · hardcoded hex · `List<T>` trong State (dùng `ImmutableList`) · gọi Repository trực tiếp từ ViewModel (phải qua UseCase).

## Architecture

**Clean Architecture + MVI**, một module `:app`.

```
com.decoutkhanqindev.lich_viet_loc_phat/
├── domain/         model (AdUnit, AdUnitState, …) · repository (interface) · usecase
├── data/           source (LunarMathAlgorithm, StaticAsset) · repository (Impl)
├── ads/            AdsManager · BannerAdUnit
├── device/         NetworkManager
├── di/             AppModule
├── utils/
├── presentation/
│   ├── base/       BaseViewModel
│   ├── common/
│   ├── model/
│   ├── components/ (+ ads/BannerAd)
│   ├── navigation/ AppDestinations · AppNavDisplay · NavExtensions
│   ├── screens/    splash · main · today · calendar · settings
│   ├── theme/      Color · Brush · Shape · Type · Theme
│   └── widget/     CalendarWidget (Glance)
├── App.kt · MainActivity.kt
```

### Tech stack

Compose BOM 2026.05.01 · Navigation 3 (1.1.2) · Koin 4.2.1 · Coroutines 1.11.0 · kotlinx.collections.immutable 0.4.0 · Timber 5.0.1 · Glance 1.1.1 · Material3.

### Data layer

- CPU-bound → `withContext(Dispatchers.Default)` trong Repository.
- UseCase: **suspend** (calendar/algorithm) & **non-suspend** (settings) → `operator fun invoke(): Result<T>` via `runCatching`. **Observe** → trả `Flow<T>` trực tiếp (không wrap Result).
- ViewModel xử lý `onSuccess/onFailure` hoặc `.getOrDefault(value)`. **Không gọi Repository trực tiếp** — luôn qua UseCase.
- `SettingsRepository`: `callbackFlow` + `OnSharedPreferenceChangeListener`, unregister qua `awaitClose`.

### Ads layer

- `domain/model/AdUnit.kt` — abstract class (`id`, `name`, `StateFlow<AdUnitState>`); contract `load/pause/resume/destroy`.
- `domain/model/AdUnitState.kt` — enum `NONE · LOADING · LOADED · FAILED · IMPRESSION`; companion giữ delay constants.
- `ads/BannerAdUnit.kt` — extends `AdUnit`; tạo `AdView` idempotent, `loadAd()` luôn gọi mỗi lần `load()`.
- `ads/AdsManager.kt` — singleton Koin (`single { AdsManager() }`); expose `bannerSplash`, `bannerHome`.
- **Load trigger**: `SplashScreen` gọi `load(context)` trong `LaunchedEffect(networkAvailable)` khi network available và state == `NONE`.
- **BannerAd**: không tự gọi `load()` — chỉ render; guard `NONE` / `FAILED` / no network → return early.
- **Navigation**: `SplashScreen` nhận `onNavigateToMain` callback, observe `bannerSplashState` rồi delay + navigate.

### DI (Koin)

Một module `appModule` (`di/AppModule.kt`): `viewModel { }` · `single<Interface> { Impl() }` · `factory { UseCase() }`. Khởi tạo trong `App.kt`: `startKoin { androidContext(this@App); modules(appModule) }`.

### Navigation 3

- Destinations: `@Serializable data class/object : NavKey` trong `AppDestinations.kt`.
- `AppNavDisplay` = `NavDisplay` + `rememberDecoratedNavEntries` với 2 decorator: `rememberSaveableStateHolderNavEntryDecorator()` + `rememberViewModelStoreNavEntryDecorator()`.
- `NavBackStack` quản lý ở `MainScreen`.

## Conventions

### MVI

State = `@Immutable data class` (default values, list `ImmutableList`). Intent/Effect = `sealed interface`. ViewModel extend `BaseViewModel<S,I,E>`, truyền `initialState` qua constructor.

```kotlin
class XxxViewModel(
    private val getSomething: GetSomethingUseCase,
) : BaseViewModel<XxxState, XxxIntent, XxxEffect>(
    initialState = XxxState(value = getSomething().getOrDefault(default))
) {
    override fun onIntent(intent: XxxIntent) { /* updateState { copy(...) }, sendEffect(...) */ }
}
```

`BaseViewModel` cung cấp: `state`, `effect`, `updateState { }`, `sendEffect()`, `onCleared()`.

### Screen vs Content

- `XxxScreen.kt` — lấy VM qua `koinActivityViewModel()`/`koinViewModel()`, collect state, gọi Content. Collect effect qua `ObserveOnLifecycleOwner { viewModel.effect.collect { … } }`.
- `XxxContent.kt` — pure composable, nhận `state` + `onIntent`, không biết ViewModel.

### Stability

State `@Immutable` or ; list `ImmutableList<T>`; UiModel là `@Immutable data class`.

### remember & derivedStateOf

- `remember { }` cache qua recomposition; `remember(key) { }` recompute khi `key` đổi (input là tham số).
- `derivedStateOf { }` **chỉ** khi input là Compose `State` (`mutableStateOf`, `collectAsStateWithLifecycle`).

```kotlin
val isVisible by remember { derivedStateOf { count > 0 } } // ✅ count là mutableStateOf
val isVisible = remember(items) { items.isNotEmpty() }      // ✅ items là tham số (KHÔNG dùng derivedStateOf)
```
