# TÀI LIỆU THIẾT KẾ GIAO DIỆN (UI/UX BLUEPRINT) - PHASE 1

**Dự án:** Ứng dụng Lịch Việt Lộc Phát
**Vai trò:** Senior UI/UX Designer  
**Chủ đề (Theme):** Classic & Glass Liquid  
**Phiên bản:** 2.0 *(Cập nhật: Chuyển từ Single-Screen sang Multi-Screen + Bottom Nav Bar)*  
**Trạng thái:** Sẵn sàng triển khai

---

## CHANGELOG v2.0

| Hạng mục       | v1.0                     | v2.0                          |
|----------------|--------------------------|-------------------------------|
| Kiến trúc      | Single-Screen + Overlays | Multi-Screen + Bottom Nav Bar |
| Date Converter | Modal/Dialog đè lên      | Tab riêng biệt                |
| Daily Detail   | Bottom Sheet (60%)       | Tab đầu tiên, full-screen     |
| Điều hướng     | Không có nav cố định     | Bottom Nav Bar (Glass, 4 tab) |

---

## 1. KIẾN TRÚC ĐIỀU HƯỚNG (NAVIGATION MAP)

Ứng dụng **Lịch Việt Lộc Phát** sử dụng cấu trúc **Multi-Screen Architecture** với **Bottom
Navigation Bar** cố định ở cuối màn hình. Mỗi tab là một màn hình độc lập, giữ trạng thái (state)
khi người dùng chuyển qua lại.

```
┌─────────────────────────────────────────────────┐
│                  Nội dung Tab                   │
│                                                 │
│                  (Thay đổi)                     │
│                                                 │
├─────────────────────────────────────────────────┤
│  [☀️ Hôm Nay]  [📅 Lịch]  [🔄 Đổi Ngày]  [⚙️ Cài Đặt] │
└─────────────────────────────────────────────────┘
```

### Sơ đồ điều hướng tổng thể

* **[Tab 1] Chi Tiết Ngày (Daily Detail)** ← *Tab mặc định khi mở app*
    * Hiển thị chi tiết ngày hôm nay khi khởi động.
* **[Tab 2] Lịch Tháng (Month Calendar)**
    * *Chạm vào ô ngày* → **[Tab 1] Hôm Nay** (tự động điều hướng & hiển thị ngày được chọn)
* **[Tab 3] Đổi Ngày (Date Converter)**
    * Màn hình độc lập, không phụ thuộc ngày được chọn.
* **[Tab 4] Cài Đặt (Settings)**
    * Tùy chỉnh giao diện và dữ liệu.

---

## 2. THIẾT KẾ BOTTOM NAVIGATION BAR

### 2.1. Đặc tả chung

* **Vị trí:** Cố định ở cuối màn hình, nằm trên `safe area` (home indicator).
* **Chiều cao:** 64dp (+ safe area inset phía dưới).
* **Chất liệu:** `Heavy Blur Glass` — độ mờ cao, xuyên thấu nhẹ nội dung phía sau.
* **Viền trên:** Đường `Highlight Stroke` 0.5dp màu trắng mờ (opacity 20%) để tạo chiều sâu.
* **Bóng:** `Drop Shadow` nhẹ hướng lên trên (elevation shadow), không dùng border cứng.

### 2.2. Cấu trúc 4 Tab

| STT | Icon | Label    | Màn hình       | Ghi chú                     |
|-----|------|----------|----------------|-----------------------------|
| 1   | ☀️   | Hôm Nay  | Daily Detail   | **Tab mặc định** khi mở app |
| 2   | 📅   | Lịch     | Month Calendar |                             |
| 3   | 🔄   | Đổi Ngày | Date Converter |                             |
| 4   | ⚙️   | Cài Đặt  | Settings       |                             |

### 2.3. Trạng thái Tab (Tab States)

* **Active (Đang chọn):**
    * Viên `Pill Indicator` (hình viên thuốc kính) bao quanh Icon + Label.
    * `Pill` dùng chất liệu `GlassCard` (mờ nhẹ, có highlight stroke).
    * Icon và Label dùng màu nhấn chính (Vàng đồng / Trắng sáng).
* **Inactive (Không được chọn):**
    * Icon và Label màu trắng, opacity 50%.
    * Không có nền/pill.
* **Transition (Chuyển tab):**
    * `Pill Indicator` trượt ngang mượt mà giữa các vị trí tab (`Sliding Pill Animation`).
    * Thời gian: 300ms, easing: `Spring Physics`.

---

## 3. BLUEPRINT CHI TIẾT TỪNG MÀN HÌNH

### 3.1. Tab 1 — Chi Tiết Ngày (Daily Detail) ← *Mặc định*

Màn hình đầy đủ, mặc định hiển thị ngày hôm nay khi mở app. Tự động cập nhật khi người dùng chạm vào
ô ngày từ Tab 2.

#### 3.1.1. Khu vực Header (Thông tin Ngày)

* **Bố cục:** Chia đôi theo chiều dọc.
* **Trái (Dương lịch):** Cụm text lớn gồm Ngày, Tháng, Năm và Thứ.
* **Phải (Âm lịch):** Tương đương, hiển thị dữ liệu Âm lịch.
* **Phân cách:** `Glass Divider` mỏng ở giữa.
* **Điều hướng ngày:** Nút mũi tên `←` / `→` ở hai bên để lướt qua ngày trước/sau mà không cần quay
  lại Tab 2.

#### 3.1.2. Khu vực Body (Thông tin Phong thủy)

Các thông tin nhóm vào `Glass Card` nhỏ, xếp trong danh sách cuộn dọc.

* **Card 1 — Can Chi & Tiết khí:**
    * 3 hàng ngang: Can Chi Năm, Tháng, Ngày.
    * 1 hàng: Tiết khí hiện tại (nếu có).
* **Card 2 — Giờ Hoàng Đạo / Hắc Đạo:**
    * Tiêu đề kèm icon.
    * Danh sách hiển thị dạng `Chip` xếp ngang.
    * Giờ tốt: màu Vàng đồng.
    * Giờ xấu: màu chìm, opacity thấp.

---

### 3.2. Tab 2 — Lịch Tháng (Month Calendar)

Nền (Background) của màn hình là dải gradient cổ điển tĩnh (Đỏ bã trầu → Nâu tối).

#### 3.2.1. Khu vực Header (Cố định ở trên cùng)

* **Chất liệu:** Trong suốt hoàn toàn, hòa vào nền tĩnh.
* **Thành phần:**
    * **Trái:** Nút "Hôm nay" (Icon hoặc Text) → nhảy về tháng hiện tại.
    * **Giữa:** Tiêu đề Tháng & Năm (Font Serif, size lớn).
    * **Phải:** *(Dự phòng cho tính năng phụ sau — VD: tìm kiếm ngày).*

#### 3.2.2. Khu vực Tiêu đề Tuần (Weekday Row)

* **Cấu trúc:** 1 hàng ngang chia 7 cột đều nhau (T2 → CN).
* **Hiển thị:** Text nhỏ, viết tắt. T7 và CN dùng màu nhấn phụ.

#### 3.2.3. Khu vực Lưới Lịch (Month Grid)

* **Chất liệu:** Toàn bộ lưới đặt trên `Glass Container` (tấm kính nền mờ, viền sáng 1dp).
* **Cấu trúc:** Ma trận 7 cột × 6 hàng. Hỗ trợ swipe ngang để chuyển tháng.
* **Bản thiết kế 1 Ô Ngày (Day Cell Component):**
    * **Trung tâm:** Ngày Dương lịch (Font Serif, size lớn, màu tương phản cao).
    * **Góc dưới phải:** Ngày Âm lịch (Font Sans-serif, size nhỏ, opacity 70%).
    * **Chấm chỉ thị:** Dot indicator phát sáng cho ngày Lễ / Rằm / Mùng Một.
    * **Trạng thái Hôm nay:** Nền ô là vòng tròn gradient có hiệu ứng Glow.
    * **Trạng thái Được chọn:** Viền ô có Glass border stroke nổi lên. Chạm vào sẽ điều hướng sang *
      *Tab 1** với dữ liệu ngày đó.

---

### 3.3. Tab 3 — Đổi Ngày (Date Converter)

Màn hình độc lập, không phụ thuộc vào ngày đang xem ở các tab khác.

#### 3.3.1. Khối Nhập liệu (Input Glass Card)

* **Tabs:** Segmented control kính với 2 tab: `Dương → Âm` và `Âm → Dương`.
* **Bộ chọn số (Wheel Pickers):** 3 cột cuộn độc lập (Ngày / Tháng / Năm). Số mờ dần ở hai đầu (
  `Fade edge`).
* **Tùy chọn:** Checkbox "Tháng Nhuận" (chỉ hiện khi tab Âm → Dương).
* **Nút hành động:** Nút "Chuyển Đổi" dạng `Elevated Glass Button`, chiếm trọn bề ngang.

#### 3.3.2. Khối Kết quả (Output Result Card)

* **Trạng thái mặc định:** Ẩn / thu gọn.
* **Trạng thái kích hoạt:** Mở rộng xuống (`Expand animation`) sau khi nhấn chuyển đổi. Nền xuất
  hiện bằng `Circular Reveal` từ nút "Chuyển Đổi". Hiển thị ngày kết quả với font lớn nhất màn hình,
  kèm Can Chi.

---

### 3.4. Tab 4 — Cài Đặt (Settings)

Màn hình cài đặt đơn giản, dùng danh sách `Glass Card` xếp theo nhóm.

#### Nhóm tùy chỉnh gợi ý:

* **Giao diện:** Chọn màu chủ đạo (theme accent), độ mờ kính (blur intensity).
* **Hiển thị:** Bật/tắt thông tin Can Chi trên ô lịch, chọn ngôn ngữ (Việt/Anh).
* **Dữ liệu:** Chọn múi giờ, vùng tính lịch (Bắc/Nam/Trung).
* **Về ứng dụng:** Phiên bản, liên hệ, đánh giá.

---

## 4. THƯ VIỆN COMPONENT LÕI (UI KIT)

Các component cốt lõi dùng xuyên suốt ứng dụng:

* **`GlassBackground`:** Quy định Blur Radius, Tint Color, Highlight Stroke.
* **`GlassCard`:** Kế thừa `GlassBackground`, thêm Drop Shadow tạo không gian 3D.
* **`GlassBottomNavBar`:** Component Bottom Nav Bar với Blur Glass, Pill Indicator trượt ngang.
* **`PillIndicator`:** Viên capsule kính bao quanh tab active, animate bằng Spring Physics.
* **`ClassicTypography`:**
    * *Display/Heading:* Font Serif — Số ngày Dương, Header kết quả.
    * *Body/Label:* Font Sans-serif — Số ngày Âm, Can Chi, Giờ giấc, Chú thích.
* **`LiquidButton`:** Nút bấm có hiệu ứng Ripple, màu loang nhẹ khi nhấn.
* **`DayCell`:** Ô ngày tái sử dụng, hỗ trợ đầy đủ các trạng thái (Default, Today, Selected,
  Holiday).

---

## 5. ĐỊNH NGHĨA CHUYỂN ĐỘNG (MOTION & ANIMATION)

* **Chuyển Tab (Bottom Nav):** `Pill Indicator` trượt ngang. Nội dung màn hình fade-in mượt mà (
  opacity 0→1, 250ms).
* **Lướt Lịch Tháng:** `Glass Container` đứng yên, ma trận số trượt đi. Số mới trượt vào kèm
  `Fade-in`.
* **Mở Daily Detail từ Tab 2:** Shared Element Transition — ô ngày được chọn "phóng to" thành header
  của Tab 1.
* **Hiển thị kết quả đổi ngày:** `Circular Reveal` từ nút "Chuyển Đổi".
* **Spring Physics:** Áp dụng cho tất cả transition chính để tạo cảm giác vật lý tự nhiên.

---

## 6. GHI CHÚ TRIỂN KHAI

* Bottom Nav Bar phải nằm trên `Window Inset` (safe area) để không bị che bởi home indicator trên
  các thiết bị không có nút cứng.
* Mỗi tab duy trì **Back Stack riêng biệt** — khi quay lại tab đã rời, trạng thái (scroll position,
  ngày đang xem) được giữ nguyên.
* Tab 1 (Hôm Nay) có thể nhận **Deep Link** từ Tab 2 kèm theo tham số ngày (`date`) để hiển thị đúng
  ngày được chọn.