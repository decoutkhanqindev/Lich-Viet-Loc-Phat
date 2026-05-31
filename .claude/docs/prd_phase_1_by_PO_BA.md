# TÀI LIỆU YÊU CẦU SẢN PHẨM (PRD) - PHASE 1 (MVP)

**Dự án:** Ứng dụng Lịch Việt Lộc Phát (Âm - Dương)  
**Vai trò:** Senior Product Owner / Business Analyst  
**Phiên bản:** 2.0 *(Cập nhật: Điều chỉnh luồng điều hướng theo Bottom Nav Bar 4 tab)*  
**Trạng thái:** Sẵn sàng triển khai

---

## CHANGELOG v2.0

| Hạng mục                    | v1.0                                         | v2.0                               |
|-----------------------------|----------------------------------------------|------------------------------------|
| Kiến trúc điều hướng        | 3 luồng tuyến tính (Tháng → Ngày → Đổi ngày) | 4 tab độc lập qua Bottom Nav Bar   |
| Màn hình mặc định           | Lưới Lịch Tháng                              | **Tab 1 — Hôm Nay (Daily Detail)** |
| Cơ chế Chi tiết Ngày        | Bottom Sheet 60% đè lên lưới lịch            | Tab riêng biệt, full-screen        |
| AC 1.1 (Hiển thị mặc định)  | Hiển thị lưới lịch tháng                     | Hiển thị Chi tiết Ngày hôm nay     |
| AC 2.1 (Kích hoạt chi tiết) | Tap → Bottom Sheet cập nhật tại chỗ          | Tap → Navigate sang Tab 1          |
| Tham chiếu "Bottom Sheet"   | Có                                           | Xóa toàn bộ                        |

---

## 1. TỔNG QUAN DỰ ÁN & MỤC TIÊU SẢN PHẨM

### 1.1. Bối cảnh sản phẩm

Lịch Âm Dương là một phần không thể thiếu trong đời sống văn hóa của người Việt Nam để theo dõi các
ngày lễ truyền thống, ngày rằm, mùng một, và các yếu tố phong thủy, tử vi. Dự án hướng tới xây dựng
một ứng dụng **Lịch Việt Lộc Phát** trên nền tảng Android.

Về mặt định hướng trải nghiệm người dùng tổng thể, ứng dụng sẽ mang phong cách Cổ điển (Classic) pha
trộn xu hướng Glassmorphism (Glass Liquid) hiện đại. Tuy nhiên, tài liệu PRD Phase 1 này sẽ tập
trung hoàn toàn vào việc định hình **Trải nghiệm Tính năng Lõi (Core Functionality)** và **Quy tắc
Nghiệp vụ (Business Rules)** để xây dựng một sản phẩm MVP tinh gọn, vận hành chính xác và ổn định
trước khi mở rộng.

### 1.2. Mục tiêu Phase 1 (MVP)

* **Xây dựng tính năng cốt lõi:** Cung cấp công cụ xem và tra cứu lịch Dương - Âm chính xác tuyệt
  đối theo múi giờ Việt Nam.
* **Tối ưu luồng trải nghiệm chính:** Hoàn thiện 4 tab điều hướng độc lập qua Bottom Navigation Bar:
    * **Tab 1 — Hôm Nay:** Xem chi tiết ngày hiện tại (màn hình mặc định khi mở app).
    * **Tab 2 — Lịch:** Xem tổng quan lưới lịch tháng, điều hướng sang Tab 1 khi chọn ngày.
    * **Tab 3 — Đổi Ngày:** Công cụ chuyển đổi định dạng thời gian Âm ↔ Dương.
    * **Tab 4 — Cài Đặt:** Tùy chỉnh giao diện và thông số ứng dụng.
* **Độc lập và Tin cậy:** Đảm bảo ứng dụng hoạt động mượt mà trong môi trường không có kết nối
  Internet (100% Offline).

---

## 2. PHẠM VI SẢN PHẨM (PRODUCT SCOPE)

### 2.1. Trong phạm vi Phase 1 (In-Scope)

* **Epic 1: Lịch Tháng (Month Calendar) — Tab 2:** Màn hình tổng quan hiển thị lưới lịch tháng song
  song cả ngày Dương và ngày Âm. Điều hướng linh hoạt giữa các tháng và đánh dấu ngày đặc biệt. Chạm
  vào ô ngày sẽ điều hướng sang Tab 1 để xem chi tiết.
* **Epic 2: Chi tiết Ngày (Daily Details) — Tab 1 (Mặc định):** Màn hình full-screen hiển thị thông
  tin chi tiết về ngày Dương/Âm, Can - Chi, Tiết khí và Giờ Hoàng Đạo/Hắc Đạo. Là màn hình đầu tiên
  người dùng thấy khi mở app. Hỗ trợ điều hướng ngày trước/sau trực tiếp trên màn hình.
* **Epic 3: Đổi ngày (Date Converter) — Tab 3:** Tiện ích chuyển đổi qua lại giữa một ngày Dương
  lịch bất kỳ và ngày Âm lịch tương ứng. Hoạt động độc lập, không phụ thuộc ngày đang xem ở các tab
  khác.
* **Epic 4: Cài Đặt (Settings) — Tab 4:** Tùy chỉnh giao diện và thông số cơ bản của ứng dụng.

### 2.2. Nằm ngoài phạm vi Phase 1 (Out-of-Scope)

*Quá trình phát triển sẽ hoãn các tính năng sau sang các giai đoạn tiếp theo để đảm bảo tiến độ
MVP:*

* Chức năng tạo nhắc nhở, quản lý sự kiện cá nhân hoặc đồng bộ với Google Calendar/Apple Calendar.
* Tiện ích hiển thị trên màn hình chính của điện thoại (Widget).
* Kho nội dung mở rộng (Hệ thống các bài văn khấn cổ truyền, tử vi trọn đời, phong thủy nhà ở, xem
  tuổi xung khắc sâu).
* Tài khoản người dùng, đồng bộ đám mây (Cloud Sync) và tùy chỉnh giao diện nâng cao (Dark
  Mode/Light Mode tự động).

---

## 3. QUY TẮC NGHIỆP VỤ (BUSINESS RULES)

* **Múi giờ tiêu chuẩn (Timezone):** Mọi công thức và thuật toán tính toán lịch Âm lịch phải dựa
  trên múi giờ chính thức của Việt Nam là **UTC+7**.
* **Quy chuẩn tuần làm việc:** Lưới lịch tháng sẽ cấu hình ngày bắt đầu của một tuần là **Thứ Hai
  ** (theo chuẩn ISO 8601 phổ biến tại Việt Nam) và kết thúc tuần là Chủ Nhật.
* **Xử lý tháng nhuận Âm lịch (Leap Month):** Thuật toán phải xử lý chính xác các năm nhuận có 2
  tháng Âm lịch trùng tên. Trong tính năng đổi ngày, hệ thống phải cung cấp tùy chọn rõ ràng cho
  người dùng xác định ngày cần đổi thuộc tháng thường hay tháng nhuận.
* **Giới hạn thời gian tra cứu (Timeline Limit):** Hệ thống hỗ trợ tra cứu chính xác dữ liệu lịch
  trong khoảng thời gian từ năm **1900 đến năm 2100** (200 năm).
* **Cơ chế vận hành Offline:** Ứng dụng tích hợp sẵn thư viện thuật toán chuyển đổi (không phụ thuộc
  vào API bên thứ ba). Mọi logic xử lý dữ liệu lịch phải chạy trực tiếp trên thiết bị của người dùng
  mà không cần kết nối mạng.
* **Tab mặc định khi khởi động:** Ứng dụng luôn mở Tab 1 (Hôm Nay) khi khởi động — hiển thị chi tiết
  ngày hiện tại theo thời gian thực của thiết bị.
* **Giữ trạng thái tab (Tab State Preservation):** Khi người dùng rời một tab và quay lại, trạng
  thái trước đó (tháng đang xem, ngày đang chọn, kết quả đổi ngày...) phải được giữ nguyên trong
  cùng một phiên sử dụng.

---

## 4. CHI TIẾT TÍNH NĂNG & TIÊU CHÍ NGHIỆM THU (ACCEPTANCE CRITERIA)

Tất cả các tiêu chí nghiệm thu dưới đây được viết theo chuẩn **Given - When - Then** để đảm bảo tính
minh bạch, dễ hiểu cho cả đội ngũ thiết kế và kiểm thử.

---

### EPIC 1: CHI TIẾT NGÀY — TAB 1 (DAILY DETAILS) ← *Tab mặc định*

#### Chức năng 1.1: Hiển thị mặc định khi khởi động

* **Mô tả:** Khi mở app, người dùng thấy ngay thông tin chi tiết của ngày hôm nay mà không cần thao
  tác thêm.
* **Tiêu chí nghiệm thu (AC):**
    * **AC 1 (Màn hình mặc định):**
        * **Given:** Người dùng khởi động ứng dụng thành công lần đầu tiên hoặc từ trạng thái nền.
        * **When:** Ứng dụng tải xong.
        * **Then:** Hệ thống hiển thị **Tab 1 — Hôm Nay** với đầy đủ thông tin chi tiết của ngày
          hiện tại theo thời gian thực của thiết bị. Bottom Nav Bar hiển thị Tab 1 ở trạng thái
          active.
    * **AC 2 (Điều hướng ngày trước/sau):**
        * **Given:** Người dùng đang xem Tab 1.
        * **When:** Người dùng nhấn nút mũi tên `←` (ngày trước) hoặc `→` (ngày sau).
        * **Then:** Hệ thống cập nhật toàn bộ thông tin trên Tab 1 sang ngày tương ứng mà không cần
          quay lại Tab 2.
    * **AC 3 (Nhận dữ liệu từ Tab 2):**
        * **Given:** Người dùng chạm vào một ô ngày trên Tab 2 (Lịch Tháng).
        * **When:** Hệ thống điều hướng sang Tab 1.
        * **Then:** Tab 1 hiển thị đúng thông tin chi tiết của ngày vừa được chọn từ Tab 2. Bottom
          Nav Bar tự động chuyển sang trạng thái active Tab 1.

#### Chức năng 1.2: Hiển thị thông tin thời gian cơ bản

* **Mô tả:** Cung cấp toàn bộ thông tin chuẩn xác về ngày đang xem — song song cả Dương và Âm lịch.
* **Tiêu chí nghiệm thu (AC):**
    * **AC 1 (Cấu trúc thông tin song song):**
        * **Given:** Tab 1 đang hiển thị một ngày bất kỳ.
        * **When:** Người dùng quan sát khu vực Header.
        * **Then:** Hệ thống phải thể hiện rõ ràng và tách biệt hai khối dữ liệu: **Khối Dương lịch
          ** (Ngày, Tháng, Năm, Thứ) và **Khối Âm lịch** (Ngày, Tháng, Năm Âm).

#### Chức năng 1.3: Hiển thị hệ thông tin Can - Chi & Tiết khí

* **Mô tả:** Cung cấp thông tin tên gọi theo hệ Can Chi phong thủy truyền thống của người Việt.
* **Tiêu chí nghiệm thu (AC):**
    * **AC 1 (Tính toán Can - Chi):**
        * **Given:** Người dùng đang xem chi tiết một ngày.
        * **When:** Hệ thống hiển thị các thông tin định danh cổ truyền.
        * **Then:** Phải hiển thị chính xác tên gọi Can Chi của: Ngày, Tháng, Năm.
    * **AC 2 (Hiển thị Tiết khí):**
        * **Given:** Ngày được chọn trùng hoặc nằm trong khoảng thời gian khởi đầu của một Tiết khí.
        * **When:** Hệ thống hiển thị chi tiết ngày.
        * **Then:** Phải hiển thị tên Tiết khí tương ứng của ngày đó.

#### Chức năng 1.4: Hiển thị thông tin Giờ Hoàng Đạo / Hắc Đạo

* **Mô tả:** Hỗ trợ người dùng tra cứu các khung giờ tốt/xấu trong ngày.
* **Tiêu chí nghiệm thu (AC):**
    * **AC 1 (Phân loại Giờ Cát / Hung):**
        * **Given:** Người dùng đang xem chi tiết một ngày cụ thể.
        * **When:** Hệ thống hiển thị danh mục giờ giấc.
        * **Then:** Hệ thống phải liệt kê đầy đủ 12 khung giờ trong ngày, chia làm 2 nhóm rõ ràng:
          Nhóm Giờ Hoàng Đạo và Nhóm Giờ Hắc Đạo kèm tên con giáp và khoảng thời gian Dương lịch
          tương ứng.

---

### EPIC 2: LỊCH THÁNG — TAB 2 (MONTH CALENDAR)

#### Chức năng 2.1: Lưới lịch tháng tổng quan

* **Mô tả:** Người dùng có thể nhìn thấy cấu trúc một tháng hoàn chỉnh để dễ dàng định vị thời gian
  Dương và Âm.
* **Tiêu chí nghiệm thu (AC):**
    * **AC 1 (Hiển thị khi chuyển sang Tab 2):**
        * **Given:** Người dùng nhấn vào Tab 2 trên Bottom Nav Bar.
        * **When:** Tab 2 được kích hoạt.
        * **Then:** Hệ thống hiển thị lưới lịch của tháng và năm hiện tại (hoặc tháng đang xem nếu
          đã điều hướng trước đó trong cùng phiên).
    * **AC 2 (Cấu trúc lưới):**
        * **Given:** Người dùng ở Tab 2.
        * **When:** Quan sát bố cục lưới.
        * **Then:** Lưới lịch bắt buộc phải hiển thị 7 cột, tương ứng từ Thứ Hai đến Chủ Nhật. Các
          tiêu đề Thứ phải được cố định ở đầu lưới.
    * **AC 3 (Hiển thị thông tin ô ngày):**
        * **Given:** Hệ thống render các ô ngày trong lưới lịch.
        * **When:** Người dùng nhìn vào một ô ngày bất kỳ thuộc tháng hiện tại.
        * **Then:** Ô đó phải chứa 2 thông tin: Số ngày Dương lịch (vị trí trung tâm, kích thước lớn
          hơn) và Số ngày Âm lịch (góc dưới phải, kích thước nhỏ hơn). Trường hợp ngày Âm lịch rơi
          vào ngày mùng 1 của tháng Âm đó, hệ thống phải hiển thị định dạng `Mùng 1 / Tháng Âm` (Ví
          dụ: `1/3` thay vì chỉ hiển thị số `1`) để người dùng nhận biết tháng Âm mới.
    * **AC 4 (Xử lý ngày tràn - Overflow days):**
        * **Given:** Lưới lịch của một tháng có các ô trống ở hàng đầu tiên hoặc hàng cuối cùng.
        * **When:** Hệ thống hiển thị toàn bộ lưới lịch.
        * **Then:** Các ô trống đó phải hiển thị ngày của tháng trước hoặc tháng sau, nhưng các chữ
          số ngày này phải hiển thị ở trạng thái mờ (có sự phân biệt rõ ràng) so với các ngày thuộc
          tháng hiện tại.

#### Chức năng 2.2: Chọn ngày để xem chi tiết

* **Mô tả:** Từ lưới lịch, người dùng chọn một ngày cụ thể để xem toàn bộ thông tin phong thủy chi
  tiết tại Tab 1.
* **Tiêu chí nghiệm thu (AC):**
    * **AC 1 (Kích hoạt xem chi tiết):**
        * **Given:** Người dùng đang ở Tab 2 (Lịch Tháng).
        * **When:** Người dùng nhấn (Tap) vào một ô ngày bất kỳ trên lưới.
        * **Then:** Hệ thống điều hướng ngay lập tức sang **Tab 1 (Hôm Nay)** và hiển thị đầy đủ
          thông tin chi tiết của ngày vừa chọn. Ô ngày được chọn trên lưới Tab 2 chuyển sang trạng
          thái "Đang được chọn" (Selected State). Bottom Nav Bar tự động chuyển Tab 1 sang trạng
          thái active.
    * **AC 2 (Quay lại Tab 2 sau khi xem chi tiết):**
        * **Given:** Người dùng đã navigate sang Tab 1 từ Tab 2.
        * **When:** Người dùng nhấn Tab 2 trên Bottom Nav Bar để quay lại.
        * **Then:** Lưới lịch Tab 2 phải khôi phục đúng tháng và ô ngày đang được chọn trước đó —
          không reset về trạng thái ban đầu.

#### Chức năng 2.3: Điều hướng thời gian (Chuyển tháng)

* **Mô tả:** Người dùng có thể chủ động chuyển sang các tháng khác để xem lịch.
* **Tiêu chí nghiệm thu (AC):**
    * **AC 1 (Chuyển sang tháng tương lai):**
        * **Given:** Người dùng đang xem lịch ở một tháng bất kỳ trên Tab 2.
        * **When:** Người dùng thực hiện thao tác vuốt từ phải sang trái (Swipe Left) hoặc nhấn nút
          điều hướng "Tháng sau".
        * **Then:** Hệ thống thực hiện hiệu ứng chuyển trang mượt mà và hiển thị chính xác lưới lịch
          của tháng kế tiếp.
    * **AC 2 (Quay lại tháng quá khứ):**
        * **Given:** Người dùng đang xem lịch ở một tháng bất kỳ trên Tab 2.
        * **When:** Người dùng thực hiện thao tác vuốt từ trái sang phải (Swipe Right) hoặc nhấn nút
          điều hướng "Tháng trước".
        * **Then:** Hệ thống thực hiện hiệu ứng chuyển trang mượt mà và hiển thị chính xác lưới lịch
          của tháng trước đó.

#### Chức năng 2.4: Nút quay về ngày hiện tại ("Hôm nay")

* **Mô tả:** Phím tắt giúp người dùng lập tức quay về mốc thời gian hiện tại dù đang tra cứu ở
  tháng/năm nào.
* **Tiêu chí nghiệm thu (AC):**
    * **AC 1 (Phản hồi khi nhấn nút):**
        * **Given:** Người dùng đã điều hướng lịch đi rất xa so với thời gian thực tế trên Tab 2.
        * **When:** Người dùng nhấn vào nút "Hôm nay" (Today) trên Header của Tab 2.
        * **Then:** Hệ thống lập tức dịch chuyển lưới lịch về tháng và năm hiện tại. Ô ngày hôm nay
          được áp dụng trạng thái nổi bật đặc biệt (Highlight).

#### Chức năng 2.5: Đánh dấu các ngày đặc biệt

* **Mô tả:** Giúp người dùng nhận diện nhanh các ngày lễ quan trọng hoặc ngày rằm, mùng một mà không
  cần đọc chi tiết từng ô.
* **Tiêu chí nghiệm thu (AC):**
    * **AC 1 (Đánh dấu ngày Sóc / Vọng):**
        * **Given:** Hệ thống đang hiển thị lưới lịch tháng trên Tab 2.
        * **When:** Có các ngày tương ứng với ngày mùng 1 (Ngày Sóc) hoặc ngày 15 (Ngày Vọng - Rằm)
          Âm lịch.
        * **Then:** Hệ thống phải hiển thị một ký hiệu đánh dấu trực quan riêng biệt ngay tại ô ngày
          đó.
    * **AC 2 (Đánh dấu ngày Lễ/Tết quốc gia):**
        * **Given:** Hệ thống đang hiển thị lưới lịch tháng trên Tab 2.
        * **When:** Có các ngày nằm trong danh sách Ngày lễ chính thức của Việt Nam.
        * **Then:** Ô ngày đó phải hiển thị nhãn (label) ký hiệu ngày lễ hoặc tên viết tắt của ngày
          lễ đó để người dùng nhận biết ngay lập tức.

---

### EPIC 3: ĐỔI NGÀY — TAB 3 (DATE CONVERTER)

#### Chức năng 3.1: Tiện ích chuyển đổi Dương lịch sang Âm lịch

* **Mô tả:** Công cụ tra cứu một ngày trong tương lai hoặc quá khứ từ lịch quốc tế sang lịch truyền
  thống Việt Nam.
* **Tiêu chí nghiệm thu (AC):**
    * **AC 1 (Nhập liệu đầu vào):**
        * **Given:** Người dùng truy cập vào Tab 3 và chọn chế độ "Dương sang Âm".
        * **When:** Người dùng chọn Ngày, Tháng, Năm Dương lịch thông qua bộ cuốn chọn số (Picker).
        * **Then:** Hệ thống phải giới hạn dữ liệu nhập nằm trong khoảng năm hỗ trợ [1900 - 2100].
    * **AC 2 (Xuất kết quả):**
        * **Given:** Người dùng đã nhập ngày Dương lịch hợp lệ.
        * **When:** Người dùng nhấn vào nút "Đổi Ngày".
        * **Then:** Hệ thống thực hiện tính toán offline và trả về kết quả ngay lập tức (độ trễ <
          200ms), hiển thị rõ ràng thông tin ngày Âm lịch tương ứng.
    * **AC 3 (Giữ kết quả khi chuyển tab):**
        * **Given:** Người dùng đã thực hiện đổi ngày thành công trên Tab 3.
        * **When:** Người dùng chuyển sang tab khác rồi quay lại Tab 3.
        * **Then:** Kết quả và dữ liệu nhập liệu trước đó phải được giữ nguyên trong cùng một phiên
          sử dụng.

#### Chức năng 3.2: Tiện ích chuyển đổi Âm lịch sang Dương lịch

* **Mô tả:** Công cụ giúp người dùng tra cứu ngày giỗ chạp, ngày lễ Âm lịch rơi vào ngày Dương lịch
  nào.
* **Tiêu chí nghiệm thu (AC):**
    * **AC 1 (Nhập liệu đặc thù Âm lịch):**
        * **Given:** Người dùng ở Tab 3, chế độ "Âm sang Dương".
        * **When:** Người dùng nhập các thông số Ngày, Tháng, Năm Âm lịch.
        * **Then:** Hệ thống phải kiểm tra tính hợp lệ của ngày trong tháng Âm, và bắt buộc phải
          hiển thị thêm một tùy chọn Checkbox/Switch có nhãn **"Tháng Nhuận"** nếu rơi vào năm
          nhuận.
    * **AC 2 (Xuất kết quả):**
        * **Given:** Người dùng nhập đầy đủ dữ liệu ngày Âm lịch hợp lệ.
        * **When:** Người dùng nhấn nút "Đổi Ngày".
        * **Then:** Hệ thống ngay lập tức trả ra kết quả ngày Dương lịch chính xác.

---

### EPIC 4: CÀI ĐẶT — TAB 4 (SETTINGS)

#### Chức năng 4.1: Tùy chỉnh giao diện cơ bản

* **Mô tả:** Cho phép người dùng cá nhân hóa một số thông số hiển thị cơ bản của ứng dụng.
* **Tiêu chí nghiệm thu (AC):**
    * **AC 1 (Lưu trữ cài đặt):**
        * **Given:** Người dùng thay đổi một tùy chọn bất kỳ trên Tab 4.
        * **When:** Người dùng thoát app và mở lại.
        * **Then:** Tùy chọn đã thay đổi phải được lưu và áp dụng lại đúng như lần trước.

---

## 5. MA TRẬN PHÂN TÍCH RỦI RO & PHƯƠNG ÁN XỬ LÝ CA ĐẶC BIỆT (EDGE CASES)

| Tình huống đặc biệt (Edge Case)                                                    | Rủi ro ảnh hưởng                                                               | Phương án giải quyết nghiệp vụ (BA Solution)                                                                                          |
|:-----------------------------------------------------------------------------------|:-------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------------------------|
| Người dùng đổi ngày rơi đúng vào tháng nhuận Âm lịch.                              | Người dùng nhận sai kết quả Dương lịch.                                        | Bắt buộc áp dụng bộ lọc "Tháng Nhuận" ở đầu vào. Kết quả trả ra phải ghi rõ chữ "(Tháng Nhuận)".                                      |
| Thời gian trên thiết bị của người dùng bị sai lệch.                                | Ứng dụng hiển thị sai ngày "Hôm nay" và tính toán sai lệch mốc lịch Âm.        | Áp dụng bộ lọc múi giờ cố định UTC+7 cho mọi biểu thức toán học tính lịch Âm.                                                         |
| Người dùng nhập năm tra cứu vượt quá mốc giới hạn (Ví dụ: Năm 1899 hoặc năm 2102). | Thuật toán trả ra kết quả rác, gây crash ứng dụng.                             | Thiết lập giới hạn cứng (Hard Limit) trên giao diện nhập liệu từ 1900 đến 2100.                                                       |
| Người dùng chuyển tab liên tục trong khi dữ liệu đang tải.                         | UI hiển thị sai dữ liệu hoặc bị trống.                                         | Mỗi tab duy trì trạng thái loading riêng biệt. Tab chưa được render lần nào sẽ hiển thị loading indicator khi được chọn lần đầu.      |
| Người dùng chọn ngày tháng trước/sau từ ô tràn (overflow) trên lưới lịch.          | Không rõ hành vi — navigate sang Tab 1 hiển thị đúng tháng hay tháng hiện tại. | Ô ngày tràn (overflow) vẫn có thể nhấn chọn. Khi chọn, Tab 1 hiển thị đúng ngày đó, Tab 2 tự động cập nhật lưới sang tháng tương ứng. |