# TÀI LIỆU KIẾN TRÚC KỸ THUẬT (TECHNICAL BLUEPRINT) - PHASE 1

**Dự án:** Ứng dụng Lịch Việt Lộc Phát
**Vai trò:** Senior Android Architect  
**Nền tảng:** Android Native (100% Kotlin, Jetpack Compose)  
**Phiên bản:** 2.2 *(Cập nhật: Xác nhận Clean Architecture + MVI)*  
**Trạng thái:** Sẵn sàng triển khai

---

## CHANGELOG

| Phiên bản | Thay đổi                                                                                      |
|-----------|-----------------------------------------------------------------------------------------------|
| v1.0      | Baseline: Clean Architecture + MVVM + Compose                                                 |
| v2.0      | Bổ sung Navigation Architecture cho Bottom Nav Bar 4 tab                                      |
| v2.1      | Thay Navigation Compose → **Navigation 3**. Chuẩn hóa DI → **Koin**                           |
| v2.2      | Làm rõ ranh giới **Clean Architecture** 3 tầng. Thay MVVM → **MVI** (Intent → State → Effect) |

---

## 1. TỔNG QUAN HỆ THỐNG & ĐỊNH HƯỚNG KỸ THUẬT

Tài liệu này định hình cấu trúc kỹ thuật tổng thể cho giai đoạn Phase 1 (MVP) của ứng dụng **Lịch
Việt Lộc Phát**. Hệ thống được thiết kế hướng tới sự tinh gọn, độc lập hoàn toàn (100% Offline) và
tối ưu hóa hiệu suất render đồ họa cho các hiệu ứng giao diện phức tạp (Glassmorphism kết hợp Motion
mượt mà).

**Hai trụ cột kiến trúc:**

- **Clean Architecture:** Tách biệt tuyệt đối 3 tầng (Presentation / Domain / Data). Tầng trong
  không phụ thuộc tầng ngoài. Domain không biết Android tồn tại.
- **MVI (Model-View-Intent):** Luồng dữ liệu một chiều nghiêm ngặt. `Intent` → `ViewModel xử lý` →
  `State` mới → UI recompose. Không có two-way binding, không có side-effect ẩn.

---

## 2. THÀNH PHẦN CÔNG NGHỆ (TECH STACK BREAKDOWN)

| Hạng mục              | Thư viện / Công nghệ                                     | Ghi chú                                                           |
|-----------------------|----------------------------------------------------------|-------------------------------------------------------------------|
| Ngôn ngữ              | Kotlin (phiên bản ổn định mới nhất)                      | Coroutines + Flow                                                 |
| UI Framework          | Jetpack Compose                                          | 100% Compose, không XML                                           |
| Mô hình kiến trúc     | **Clean Architecture + MVI**                             | Thay thế MVVM                                                     |
| Navigation            | **Navigation 3** (`androidx.navigation3:navigation3-ui`) | BackStack là State thuần Kotlin                                   |
| DI                    | **Koin** (`io.insert-koin:koin-androidx-compose`)        | Không dùng Hilt                                                   |
| Immutable Collections | `kotlinx.collections.immutable`                          | `ImmutableList` cho Presentation State — không dùng ở Domain/Data |
| Async                 | Kotlin Coroutines + Flow                                 | `Dispatchers.Default` cho tính toán lịch                          |
| Core Engine           | Thuật toán Hồ Ngọc Đức (Kotlin thuần)                    | 100% Offline, không network                                       |

---

## 3. CLEAN ARCHITECTURE — PHÂN TẦNG & QUY TẮC PHỤ THUỘC

### 3.1. Sơ đồ phụ thuộc (Dependency Rule)

```
┌──────────────────────────────────────────┐
│         Tầng Presentation                │  ← Android, Compose, ViewModel, Koin
│   (Screens, ViewModels, NavDisplay)      │
└───────────────────┬──────────────────────┘
                    │  chỉ gọi xuống (không ngược lại)
┌───────────────────▼──────────────────────┐
│           Tầng Domain                    │  ← Kotlin thuần, KHÔNG import Android
│   (UseCases, Models, Repository I/F)     │
└───────────────────┬──────────────────────┘
                    │  interface (Dependency Inversion)
┌───────────────────▼──────────────────────┐
│           Tầng Data                      │  ← Implement interface của Domain
│   (Repositories Impl, DataSources)       │
└──────────────────────────────────────────┘
```

**Quy tắc bất khả vi phạm:**

- Tầng Domain **không** import bất kỳ class Android hay Compose nào (`Context`, `ViewModel`,
  `@Immutable`, `@Stable`, v.v.) — chỉ Kotlin stdlib.
- Tầng Data **không** import Compose — chỉ Kotlin stdlib + Android framework tối thiểu (`Context`nếu
  cần đọc asset).
- Tầng Data **không** được gọi trực tiếp từ Presentation — phải qua Domain interface.
- Dependency Inversion: Domain định nghĩa `interface Repository`, Data implement nó.
- `@Immutable` / `@Stable` / `ImmutableList` chỉ được dùng ở **Tầng Presentation**.

### 3.2. Tầng Domain (Nhân lõi — Không phụ thuộc gì)

Đây là tầng duy nhất **không thay đổi** dù thay Compose bằng View, hay thay Room bằng DataStore.

**Domain Models** — pure Kotlin `data class`, **không import bất kỳ dependency nào** kể cả
`@Immutable`/`@Stable` (đây là Compose annotation, chỉ thuộc Presentation). Dùng `List<T>` của
Kotlin stdlib:

```kotlin
// Không có annotation nào ngoài Kotlin thuần
data class SolarDate(val day: Int, val month: Int, val year: Int)
data class LunarDate(val day: Int, val month: Int, val year: Int, val isLeapMonth: Boolean)
data class CanChi(val canNam: String, val chiNam: String, val canNgay: String, val chiNgay: String)
data class HourInfo(val name: String, val isAuspicious: Boolean)
data class DailyMetadata(
    val solar: SolarDate,
    val lunar: LunarDate,
    val canChi: CanChi,
    val auspiciousHours: List<HourInfo>,   // List Kotlin stdlib — không phải ImmutableList
    val solarTerm: String?                 // Tiết khí
)
```

**Repository Interface** — hợp đồng dữ liệu, Domain định nghĩa, Data implement:

```kotlin
// domain/repository/CalendarRepository.kt
interface CalendarRepository {
    suspend fun getDailyMetadata(date: SolarDate): DailyMetadata
    suspend fun getDaysInMonth(year: Int, month: Int): List<DayCell>
    suspend fun convertSolarToLunar(solar: SolarDate): LunarDate
    suspend fun convertLunarToSolar(lunar: LunarDate): SolarDate
}
```

**UseCases** — mỗi UseCase = một hành vi nghiệp vụ duy nhất, không hơn không kém:

```kotlin
// domain/usecase/GetDailyMetadataUseCase.kt
class GetDailyMetadataUseCase(private val repository: CalendarRepository) {
    suspend operator fun invoke(date: SolarDate): Result<DailyMetadata> =
        runCatching { repository.getDailyMetadata(date) }
}

// domain/usecase/ConvertSolarToLunarUseCase.kt
class ConvertSolarToLunarUseCase(private val repository: CalendarRepository) {
    suspend operator fun invoke(solar: SolarDate): Result<LunarDate> =
        runCatching { repository.convertSolarToLunar(solar) }
}

// Tương tự: ConvertLunarToSolarUseCase, GetDaysInMonthUseCase, CalculateCanChiUseCase,
//            GetHourlyAuspiciousnessUseCase, GetSolarTermUseCase
```

### 3.3. Tầng Data (Implement Domain Interface)

```kotlin
// data/repository/CalendarRepositoryImpl.kt
class CalendarRepositoryImpl(
    private val algorithmSource: LunarMathAlgorithmDataSource,
    private val assetSource: StaticAssetDataSource
) : CalendarRepository {

    override suspend fun getDailyMetadata(date: SolarDate): DailyMetadata =
        withContext(Dispatchers.Default) {        // CPU-intensive → Default dispatcher
            val lunar = algorithmSource.solarToLunar(date)
            val canChi = algorithmSource.calculateCanChi(date)
            val hours = algorithmSource.getAuspiciousHours(date)
            val term = assetSource.getSolarTerm(date)
            DailyMetadata(date, lunar, canChi, hours, term)
        }
    // ... implement các hàm còn lại
}
```

### 3.4. Tầng Presentation (MVI — xem Mục 4)

Tầng này chỉ biết: nhận `State` → hiển thị. Gửi `Intent` → không quan tâm xử lý thế nào.

---

## 4. MVI — MÔ HÌNH LUỒNG DỮ LIỆU

### 4.1. Nguyên lý MVI

MVI là mô hình **một chiều nghiêm ngặt** gồm 3 thành phần:

```
        ┌─────────────────────────────────────────┐
        │              Compose UI                 │
        │  - Render State (vô điều kiện)          │
        │  - Phát Intent (không xử lý logic)      │
        └────────┬──────────────────▲─────────────┘
                 │ Intent           │ State
        ┌────────▼──────────────────┴─────────────┐
        │             ViewModel                   │
        │  - Nhận Intent                          │
        │  - Gọi UseCase                          │
        │  - Tính State mới (reduce)              │
        │  - Phát Effect (one-time event)         │
        └────────┬──────────────────▲─────────────┘
                 │ call             │ Result
        ┌────────▼──────────────────┴─────────────┐
        │    Domain (UseCase → Repository)        │
        └─────────────────────────────────────────┘
```

**3 khái niệm cốt lõi:**

- **Intent:** Hành động người dùng hoặc hệ thống (VD: `SelectDay`, `SwipeMonth`, `RequestToday`).
  Sealed class, bất biến.
- **State:** Toàn bộ trạng thái UI tại một thời điểm. Data class đánh dấu `@Immutable` (Compose
  annotation — **chỉ ở Presentation**), dùng `ImmutableList<T>` (`kotlinx.collections.immutable`)
  thay cho `List<T>` để Compose Compiler nhận diện bất biến, tránh Recomposition thừa. UI chỉ render
  State — không giữ state riêng.
- **Effect:** Sự kiện một lần không thuộc về State (VD: `NavigateToToday`, `ShowSnackbar`). Dùng
  `Channel` thay vì `StateFlow` để tránh replay.

### 4.2. Định nghĩa Contract cho từng màn hình

Mỗi màn hình có một `Contract` object gom `State`, `Intent`, `Effect` vào cùng một chỗ để dễ đọc.

**Lưu ý quan trọng về mapping:**

- `State` ở Presentation dùng `@Immutable` và `ImmutableList<T>` (`kotlinx.collections.immutable`).
- ViewModel có trách nhiệm **map** `List<T>` từ Domain → `ImmutableList<T>` trước khi đưa vào State.
- `DayCellUiModel`, `HourInfoUiModel`... là các UiModel riêng của Presentation — không dùng thẳng
  Domain Model trong State nếu cần transform dữ liệu.

```kotlin
// screens/today/TodayContract.kt
object TodayContract {

    @Immutable
    data class State(
        val isLoading: Boolean = true,
        val selectedDate: SolarDate = SolarDate.today(),
        val dailyMetadata: DailyMetadataUiModel? = null,  // UiModel, không phải Domain Model
        val error: String? = null
    )

    sealed class Intent {
        data class SelectDate(val date: SolarDate) : Intent()
        data object RequestToday : Intent()
        data object NavigateToPrevDay : Intent()
        data object NavigateToNextDay : Intent()
    }

    sealed class Effect {
        // TodayScreen hiện tại chưa cần Effect (navigation do CalendarScreen gửi)
    }
}

// UiModel tương ứng — dùng ImmutableList thay List
@Immutable
data class DailyMetadataUiModel(
    val solar: SolarDate,
    val lunar: LunarDate,
    val canChi: CanChi,
    val auspiciousHours: ImmutableList<HourInfoUiModel>,  // ImmutableList — Compose friendly
    val solarTerm: String?
)

// Mapper trong ViewModel: Domain List → ImmutableList cho Presentation
fun DailyMetadata.toUiModel() = DailyMetadataUiModel(
    solar = solar,
    lunar = lunar,
    canChi = canChi,
    auspiciousHours = auspiciousHours.map { it.toUiModel() }.toImmutableList(),
    solarTerm = solarTerm
)
```

```kotlin
// screens/calendar/CalendarContract.kt
object CalendarContract {

    @Immutable
    data class State(
        val isLoading: Boolean = true,
        val displayedYear: Int = LocalDate.now().year,
        val displayedMonth: Int = LocalDate.now().monthValue,
        val days: ImmutableList<DayCellUiModel> = persistentListOf(),  // ImmutableList
        val selectedDate: SolarDate? = null,
        val showTodayButton: Boolean = false
    )

    sealed class Intent {
        data class SelectDay(val date: SolarDate) : Intent()
        data class SwipeMonth(val direction: SwipeDirection) : Intent()
        data object RequestToday : Intent()
    }

    sealed class Effect {
        data class NavigateToToday(val date: SolarDate) : Effect()
    }
}
```

```kotlin
// screens/converter/ConverterContract.kt
object ConverterContract {

    @Immutable
    data class State(
        val mode: ConvertMode = ConvertMode.SOLAR_TO_LUNAR,
        val inputDay: Int = 1,
        val inputMonth: Int = 1,
        val inputYear: Int = LocalDate.now().year,
        val isLeapMonth: Boolean = false,
        val result: ConvertResultUiModel? = null,   // UiModel
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed class Intent {
        data class ChangeMode(val mode: ConvertMode) : Intent()
        data class InputChanged(val day: Int, val month: Int, val year: Int) : Intent()
        data class ToggleLeapMonth(val checked: Boolean) : Intent()
        data object Convert : Intent()
    }

    sealed class Effect {
        data object ScrollResultIntoView : Effect()
    }
}
```

### 4.3. ViewModel chuẩn MVI

ViewModel nhận `Intent` → gọi `UseCase` → tính `State` mới (reduce) → phát `Effect` nếu cần:

```kotlin
// screens/today/TodayViewModel.kt
class TodayViewModel(
    private val getDailyMetadata: GetDailyMetadataUseCase,
    private val sharedVm: SharedCalendarViewModel       // Inject Activity-scoped shared VM
) : ViewModel() {

    private val _state = MutableStateFlow(TodayContract.State())
    val state: StateFlow<TodayContract.State> = _state.asStateFlow()

    private val _effect = Channel<TodayContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        // Observe selectedDate từ SharedVM — tự động reload khi CalendarScreen chọn ngày
        viewModelScope.launch {
            sharedVm.selectedDate.collectLatest { date ->
                handleIntent(TodayContract.Intent.SelectDate(date.toSolarDate()))
            }
        }
    }

    fun handleIntent(intent: TodayContract.Intent) {
        when (intent) {
            is TodayContract.Intent.SelectDate -> loadMetadata(intent.date)
            is TodayContract.Intent.RequestToday -> loadMetadata(SolarDate.today())
            is TodayContract.Intent.NavigateToPrevDay -> loadMetadata(
                _state.value.selectedDate.minusDays(
                    1
                )
            )
            is TodayContract.Intent.NavigateToNextDay -> loadMetadata(
                _state.value.selectedDate.plusDays(
                    1
                )
            )
        }
    }

    private fun loadMetadata(date: SolarDate) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, selectedDate = date) }
            getDailyMetadata(date)
                .onSuccess { metadata ->
                    _state.update { it.copy(isLoading = false, dailyMetadata = metadata) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }
}
```

### 4.4. Compose Screen chuẩn MVI

UI chỉ làm 2 việc: **render State** và **gửi Intent**. Không logic, không điều kiện phức tạp:

```kotlin
// screens/today/TodayScreen.kt
@Composable
fun TodayScreen() {
    val viewModel: TodayViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Xử lý Effect (one-time events) — dùng LaunchedEffect để không bị replay
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                // Handle effects nếu có
            }
        }
    }

    TodayContent(
        state = state,
        onIntent = viewModel::handleIntent   // Truyền handler xuống, UI không biết gì hơn
    )
}

// Stateless content composable — dễ Preview, dễ test
@Composable
private fun TodayContent(
    state: TodayContract.State,
    onIntent: (TodayContract.Intent) -> Unit
) {
    when {
        state.isLoading -> LoadingIndicator()
        state.error != null -> ErrorView(message = state.error)
        state.dailyMetadata != null -> DailyDetailView(
            metadata = state.dailyMetadata,
            onPrevDay = { onIntent(TodayContract.Intent.NavigateToPrevDay) },
            onNextDay = { onIntent(TodayContract.Intent.NavigateToNextDay) },
            onToday = { onIntent(TodayContract.Intent.RequestToday) }
        )
    }
}
```

### 4.5. Effect — One-time Event (Không dùng StateFlow)

`Effect` là sự kiện chỉ xảy ra một lần (navigate, show snackbar). Dùng `Channel` thay `StateFlow` vì
`StateFlow` replay giá trị cuối — Effect sẽ bị kích hoạt lại sau rotation:

```kotlin
// ĐÚNG — Channel không replay
private val _effect = Channel<CalendarContract.Effect>(Channel.BUFFERED)
val effect = _effect.receiveAsFlow()

// SAI — StateFlow replay → Effect bị trigger lại sau rotation
private val _effect = MutableStateFlow<CalendarContract.Effect?>(null) // ❌
```

---

## 5. KIẾN TRÚC ĐIỀU HƯỚNG — NAVIGATION 3

### 5.1. Định nghĩa Destinations

```kotlin
// navigation/AppDestinations.kt
import kotlinx.serialization.Serializable

@Serializable
data object TodayDestination

@Serializable
data object CalendarDestination

@Serializable
data object ConverterDestination

@Serializable
data object SettingsDestination
```

### 5.2. BackStack & NavDisplay

```kotlin
// MainActivity.kt
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            LichVietTheme {
                val backStack = rememberNavBackStack(TodayDestination)
                AppScaffold(backStack)
            }
        }
    }
}

// AppScaffold.kt
@Composable
fun AppScaffold(backStack: NavBackStack<Any>) {
    Scaffold(
        bottomBar = { GlassBottomNavBar(backStack) }
    ) { innerPadding ->
        AppNavDisplay(backStack, Modifier.padding(innerPadding))
    }
}

// navigation/AppNavDisplay.kt
@Composable
fun AppNavDisplay(backStack: NavBackStack<Any>, modifier: Modifier = Modifier) {
    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
        ),
        entryContent = { entry ->
            when (entry.key) {
                is TodayDestination -> TodayScreen()
                is CalendarDestination -> CalendarScreen(backStack)
                is ConverterDestination -> ConverterScreen()
                is SettingsDestination -> SettingsScreen()
            }
        }
    )
}
```

### 5.3. Tab Navigation Pattern

```kotlin
// navigation/NavExtensions.kt
fun <T : Any> NavBackStack<T>.navigateToTab(destination: T) {
    val existingIndex = indexOfFirst { it.key == destination }
    if (existingIndex >= 0) {
        while (size > existingIndex + 1) removeLast()
    } else {
        val root = first()
        clear()
        add(root)
        if (destination != root.key) add(NavEntry(destination))
    }
}
```

### 5.4. GlassBottomNavBar

```kotlin
@Composable
fun GlassBottomNavBar(backStack: NavBackStack<Any>) {
    val currentDestination = backStack.lastOrNull()?.key

    val tabs = listOf(
        Triple(TodayDestination, Icons.Default.Today, "Hôm Nay"),
        Triple(CalendarDestination, Icons.Default.CalendarMonth, "Lịch"),
        Triple(ConverterDestination, Icons.Default.SwapHoriz, "Đổi Ngày"),
        Triple(SettingsDestination, Icons.Default.Settings, "Cài Đặt"),
    )

    NavigationBar {
        tabs.forEach { (destination, icon, label) ->
            NavigationBarItem(
                selected = currentDestination == destination,
                onClick = { backStack.navigateToTab(destination) },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) }
            )
        }
    }
}
```

### 5.5. Shared State Tab 1 ↔ Tab 2 (Koin Activity Scope)

```kotlin
// screens/shared/SharedCalendarViewModel.kt
// Không có Intent/State/Effect riêng — chỉ là cầu nối selectedDate giữa 2 tab
class SharedCalendarViewModel : ViewModel() {
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    fun onDaySelected(date: LocalDate) = _selectedDate.update { date }
}
```

```kotlin
// CalendarScreen — khi user chọn ngày, ghi vào SharedVM rồi navigate
@Composable
fun CalendarScreen(backStack: NavBackStack<Any>) {
    val sharedVm: SharedCalendarViewModel = koinActivityViewModel()
    val viewModel: CalendarViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CalendarContract.Effect.NavigateToToday -> {
                    sharedVm.onDaySelected(effect.date.toLocalDate())
                    backStack.navigateToTab(TodayDestination)
                }
            }
        }
    }

    CalendarContent(
        state = state,
        onIntent = viewModel::handleIntent
    )
}
```

---

## 6. DEPENDENCY INJECTION — KOIN MODULE

```kotlin
// di/AppModule.kt
val appModule = module {

    // --- Shared (Activity-scoped) ---
    viewModel { SharedCalendarViewModel() }

    // --- Screen ViewModels ---
    viewModel { TodayViewModel(get(), get()) }
    viewModel { CalendarViewModel(get()) }
    viewModel { ConverterViewModel(get(), get()) }
    viewModel { SettingsViewModel() }

    // --- UseCases ---
    factory { GetDailyMetadataUseCase(get()) }
    factory { GetDaysInMonthUseCase(get()) }
    factory { ConvertSolarToLunarUseCase(get()) }
    factory { ConvertLunarToSolarUseCase(get()) }
    factory { CalculateCanChiUseCase(get()) }
    factory { GetHourlyAuspiciousnessUseCase(get()) }
    factory { GetSolarTermUseCase(get()) }

    // --- Repositories ---
    single<CalendarRepository> { CalendarRepositoryImpl(get(), get()) }

    // --- Data Sources ---
    single { LunarMathAlgorithmDataSource() }
    single { StaticAssetDataSource(androidContext()) }
}
```

---

## 7. CHIẾN LƯỢC TỐI ƯU HIỆU NĂNG UI & HOẠT HỌA

### 7.1. Kiểm soát Recomposition

* **`@Stable` / `@Immutable` chỉ ở Presentation:** Đánh dấu toàn bộ UiModel và `State` trong
  Contract — Compose Compiler bỏ qua Recomposition không cần thiết. Domain Model không được đánh dấu
  các annotation này.
* **`ImmutableList` cho State:** Dùng `ImmutableList<T>` (`kotlinx.collections.immutable`) trong
  `State` thay cho `List<T>` — Compose Compiler nhận diện bất biến, không recompose lưới 42 ô khi
  State không thay đổi. ViewModel map `List` → `ImmutableList` trước khi emit State.
* **`key` cho Grid:** Mỗi `DayCell` trong lưới 7×6 gán `key = "YYYY-MM-DD"` — Compose chỉ recompose
  ô có thay đổi.
* **`derivedStateOf`:** Dùng cho logic phụ thuộc State khác (VD: nút "Hôm nay" hiện/ẩn theo tháng
  đang cuộn).
* **`remember` + `rememberSaveable`:** `remember` cho tính toán tốn kém trong session.
  `rememberSaveable` cho state sống qua rotation (VD: tháng đang hiển thị).

### 7.2. Render Glass Liquid tinh gọn

* **Không** dùng `Modifier.blur()` trực tiếp trên phần tử con nhỏ lẻ.
* **Layering:** Blur trên Layout cha (`Box`/`Surface`) → xếp nội dung lên trên qua `graphicsLayer` →
  tận dụng Hardware Layer Caching, giảm tải GPU.
* **`RenderEffect` (API 31+):** Android 12+ dùng `RenderEffect` + `BlurMaskFilter` — render GPU trực
  tiếp, không tốn CPU.
* **Animation:** `Animatable` + `spring()` spec cho Liquid Motion. Tránh animation lồng nhau block
  Main Thread.

### 7.3. Tab Transition

* `AnimatedNavDisplay` (Navigation 3 built-in) cho transition mượt mà khi đổi tab — không cần
  accompanist.
* `BackStack` là State → Compose chỉ recompose phần thay đổi, không rebuild toàn bộ NavGraph.
* Pill Indicator: `Animatable` scoped tại `GlassBottomNavBar` — trượt ngang độc lập với
  recomposition tab con.

---

## 8. CẤU TRÚC PACKAGE (PROJECT STRUCTURE)

```
com.locphat.lichviet
├── MainActivity.kt
├── LichVietApplication.kt              # startKoin { modules(appModule) }
├── navigation/
│   ├── AppDestinations.kt              # @Serializable data objects
│   ├── AppNavDisplay.kt
│   └── NavExtensions.kt               # navigateToTab()
├── ui/
│   ├── components/                     # GlassCard, DayCell, PillIndicator, GlassBottomNavBar...
│   ├── theme/                          # Color, Typography, Shape
│   └── screens/
│       ├── today/
│       │   ├── TodayScreen.kt
│       │   ├── TodayContract.kt        # State + Intent + Effect
│       │   └── TodayViewModel.kt
│       ├── calendar/
│       │   ├── CalendarScreen.kt
│       │   ├── CalendarContract.kt
│       │   └── CalendarViewModel.kt
│       ├── converter/
│       │   ├── ConverterScreen.kt
│       │   ├── ConverterContract.kt
│       │   └── ConverterViewModel.kt
│       ├── settings/
│       │   ├── SettingsScreen.kt
│       │   ├── SettingsContract.kt
│       │   └── SettingsViewModel.kt
│       └── shared/
│           └── SharedCalendarViewModel.kt
├── domain/
│   ├── model/                          # SolarDate, LunarDate, DailyMetadata, CanChi, HourInfo...
│   ├── usecase/                        # GetDailyMetadataUseCase, ConvertSolarToLunarUseCase...
│   └── repository/                     # CalendarRepository (interface)
├── data/
│   ├── repository/
│   │   └── CalendarRepositoryImpl.kt
│   └── source/
│       ├── LunarMathAlgorithmDataSourceImpl.kt
│       └── StaticAssetDataSourceImpl.kt
└── di/
    └── AppModule.kt
```

---

## 9. GHI CHÚ TRIỂN KHAI & RÀNG BUỘC KỸ THUẬT

* **Single Activity:** Toàn bộ app chạy trong `MainActivity`. `BackStack` khởi tạo tại đây.
* **MVI Contract bắt buộc:** Mỗi màn hình phải có file `*Contract.kt` chứa `State`, `Intent`,
  `Effect`. Không được để State, Intent phân tán trong nhiều file.
* **`@Immutable` / `ImmutableList` chỉ ở Presentation:** Domain và Data chỉ dùng Kotlin `List<T>`.
  ViewModel có trách nhiệm map sang `ImmutableList` (`toImmutableList()`) trước khi emit vào State.
* **Dependency `kotlinx.collections.immutable`:**
  ```kotlin
  implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7")
  ```
* **Effect qua Channel:** Tuyệt đối không dùng `StateFlow` cho Effect — sẽ bị replay sau rotation.
* **Domain không import Android:** Tầng Domain chỉ import Kotlin stdlib. Vi phạm điều này là vi phạm
  Clean Architecture.
* **Navigation 3 Dependency:**
  ```kotlin
  implementation("androidx.navigation3:navigation3-ui:1.0.0-alpha01")
  implementation("androidx.lifecycle:lifecycle-viewmodel-navigation3:2.9.0-alpha01")
  ```
* **Koin Setup:**
  ```kotlin
  // LichVietApplication.kt
  startKoin { androidContext(this@LichVietApplication); modules(appModule) }
  ```
* **Window Insets (Edge-to-Edge):** `WindowCompat.setDecorFitsSystemWindows(window, false)`.
  `GlassBottomNavBar` tự padding theo `WindowInsets.navigationBars`.
* **minSdk = 26:** Dùng `java.time.LocalDate` trực tiếp. Thấp hơn → bật `coreLibraryDesugaring`.
* **Compose BOM:** `platform("androidx.compose:compose-bom:...")` tránh version conflict.
* **Coroutine Dispatcher:** Tính toán lịch âm dương bắt buộc trên `Dispatchers.Default`.
* **Serialization cho BackStack:** Thêm `kotlin-serialization` plugin + `@Serializable` cho tất cả
  Destination để BackStack sống qua process death.