# 🛍️ Hướng Dẫn Phát Triển & Tổng Quan Dự Án

> **Smart Store Management System (StoreManager)**  
> Tài liệu kỹ thuật & kiến trúc dành cho tất cả thành viên nhóm môn **CSD201 (Data Structures and Algorithms)** theo phương pháp **Computational Thinking**.

---

## 📑 Mục Lục

1. [Tổng Quan Dự Án](#1-tổng-quan-dự-án)
2. [Cài Đặt Môi Trường](#2-cài-đặt-môi-trường)
3. [Cấu Trúc Dự Án](#3-cấu-trúc-dự-án)
4. [Kiến Trúc & Luồng Hoạt Động](#4-kiến-trúc--luồng-hoạt-động)
5. [Phân Công Nhiệm Vụ](#5-phân-công-nhiệm-vụ)
6. [Hướng Dẫn Git Workflow](#6-hướng-dẫn-git-workflow)
7. [Quy Tắc Viết Code](#7-quy-tắc-viết-code)
8. [Hướng Dẫn Implement Chi Tiết](#8-hướng-dẫn-implement-chi-tiết)
9. [Cách Test Riêng Phần Của Mình](#9-cách-test-riêng-phần-của-mình)
10. [Ghép Code & Tích Hợp](#10-ghép-code--tích-hợp)
11. [Xử Lý Lỗi Thường Gặp](#11-xử-lý-lỗi-thường-gặp)
12. [Đo Đạc Hiệu Năng (Benchmark) & Liên Hệ](#12-đo-đạc-hiệu-năng-benchmark--liên-hệ)

---

## 1. Tổng Quan Dự Án

**Smart Store Management System** là hệ thống quản trị siêu thị / cửa hàng thông minh được xây dựng nhằm giải quyết bài toán: **Quản lý danh mục hàng hóa**, **Điều phối luồng khách hàng thanh toán** và **Tra cứu sản phẩm siêu tốc khi quét mã tính tiền**.

Dự án áp dụng 4 cấu trúc dữ liệu cốt lõi:

| Cấu trúc dữ liệu | Vị trí trong hệ thống | Mục đích & Nghiệp vụ | Độ phức tạp |
|---|---|---|---|
| **BST** (Binary Search Tree) | `datastructure/bst/` | Lưu trữ chỉ mục và tra cứu thông tin sản phẩm siêu tốc theo mã `productId` khi thu ngân quét mã barcode | Tìm kiếm: $O(\log n)$<br>Chèn/Xóa: $O(\log n)$ |
| **Doubly Linked List** | `datastructure/linkedlist/` | Quản lý toàn bộ danh mục sản phẩm kho hàng theo thứ tự thời gian nhập kho, hỗ trợ duyệt tiến/lùi | Chèn đầu/cuối: $O(1)$<br>Duyệt: $O(n)$ |
| **Queue** (FIFO) | `datastructure/queue/` | Hàng chờ thanh toán thông thường, đảm bảo nguyên tắc công bằng: Khách đến trước được phục vụ trước | Enqueue/Dequeue: $O(1)$ |
| **Binary Max-Heap** | `datastructure/heap/` | Làn ưu tiên dành cho khách hàng VIP / Kim cương (điểm ưu tiên hạng thẻ cao hơn sẽ được phục vụ trước) | Enqueue/Dequeue: $O(\log n)$ |

### Nguyên tắc thiết kế (Design Principles)
* **Interface-first**: Mọi cấu trúc dữ liệu đều có `interface` định nghĩa sẵn (`IProductBST`, `IInventoryList`, `ICheckoutQueue`, `IVIPQueue`).
* **Loose coupling**: Tầng nghiệp vụ (`service`) chỉ phụ thuộc vào `interface`, không phụ thuộc trực tiếp vào lớp cài đặt cụ thể.
* **Parallel development**: Các thành viên trong nhóm có thể code song song từng cấu trúc dữ liệu mà không bị xung đột (conflict).
* **Computational Thinking (CT)**: Áp dụng đầy đủ 5 trụ cột CT (Decomposition, Pattern Recognition, Abstraction, Algorithm Design, Implementation).

---

## 2. Cài Đặt Môi Trường

### Yêu cầu hệ thống
* **Java**: JDK 8 trở lên (Đã kiểm nghiệm tương thích 100% với JDK 1.8.0_172).
* **IDE**: Apache NetBeans 13, IntelliJ IDEA, hoặc VS Code.
* **Console**: Hỗ trợ font Unicode UTF-8 trên Windows.

### Biên dịch & Chạy từ dòng lệnh (Terminal / PowerShell)

```powershell
# 1. Chuyển encoding console sang UTF-8
[Console]::InputEncoding = [System.Text.Encoding]::UTF8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
chcp 65001

# 2. Biên dịch toàn bộ dự án vào thư mục bin
javac -encoding UTF-8 -d bin -sourcepath src src/Main.java src/util/BenchmarkRunner.java

# 3. Khởi chạy ứng dụng Console UI
java -Dfile.encoding=UTF-8 -cp bin Main

# 4. Chạy bài đo hiệu năng Benchmark
java -Dfile.encoding=UTF-8 -cp bin util.BenchmarkRunner
```

### Mở và chạy trên NetBeans 13
1. Mở **Apache NetBeans 13**.
2. Chọn `File` -> `Open Project...` -> Trỏ đến thư mục `StoreManager`.
3. Nhấn phím **F6** (hoặc nút **Run**) để chạy chương trình.

---

## 3. Cấu Trúc Dự Án

```text
StoreManager/
├── pom.xml                                  # Cấu hình Maven (Java 8)
├── build.xml                                # Cấu hình Ant build cho NetBeans
├── nbproject/                               # Thư mục cấu hình dự án NetBeans 13
├── CLAUDE.md                                # Tóm tắt kiến trúc cho AI / Developers
├── README.md                                # Tài liệu kỹ thuật chi tiết
├── CSD-Computational Thinking.pptx           # Slide bài giảng FPTU đã chèn Slide 6, 7, 8
├── StoreManagement_Presentation.pptx        # Slide thuyết trình hoàn chỉnh Topic 6
└── src/
    ├── Main.java                            # Entry point + Khởi tạo Mock Data
    │
    ├── model/                               # Data Models (Các thực thể dữ liệu)
    │   ├── Product.java                     # Thông tin sản phẩm (ID, tên, danh mục, giá, tồn kho)
    │   ├── CartItem.java                    # Mặt hàng trong giỏ (sản phẩm, số lượng, thành tiền)
    │   ├── Customer.java                    # Khách hàng (hạng VIP, SĐT, danh sách giỏ hàng)
    │   └── Invoice.java                     # Hóa đơn thanh toán (chiết khấu, VAT, biên lai)
    │
    ├── datastructure/                       # Cấu trúc dữ liệu tự lập trình (Custom DSA)
    │   ├── bst/                             # Cây tìm kiếm nhị phân
    │   │   ├── IProductBST.java             #   Interface (KHÔNG SỬA)
    │   │   ├── ProductNode.java             #   Node lưu trữ
    │   │   └── ProductBST.java              #   Logic cây BST O(log n)
    │   │
    │   ├── linkedlist/                      # Danh sách liên kết đôi
    │   │   ├── IInventoryList.java          #   Interface (KHÔNG SỬA)
    │   │   ├── ListNode.java                #   Node liên kết 2 chiều
    │   │   └── InventoryLinkedList.java     #   Logic DLL quản lý kho
    │   │
    │   ├── queue/                           # Hàng chờ thường
    │   │   ├── ICheckoutQueue.java          #   Interface (KHÔNG SỬA)
    │   │   ├── QueueNode.java               #   Node hàng đợi
    │   │   └── CheckoutQueueFIFO.java       #   Logic Queue FIFO O(1)
    │   │
    │   └── heap/                            # Hàng chờ ưu tiên VIP
    │       ├── IVIPQueue.java               #   Interface (KHÔNG SỬA)
    │       └── VIPCustomerHeap.java         #   Logic Binary Max-Heap O(log n)
    │
    ├── service/                             # Tầng nghiệp vụ xử lý logic
    │   ├── ProductService.java              #   Quản lý kho hàng & đồng bộ BST
    │   ├── CheckoutService.java             #   Điều phối 2 làn chờ, quét mã & xuất hóa đơn
    │   └── AnalyticsService.java            #   Báo cáo doanh thu & mặt hàng bán chạy
    │
    ├── ui/                                  # Giao diện điều khiển
    │   └── ConsoleUI.java                   #   Menu Console tương tác phân vai trò
    │
    └── util/                                # Thư viện tiện ích
        ├── Acceptable.java                  #   Biểu thức chính quy (Regex validation)
        ├── Inputter.java                    #   Nhập liệu an toàn từ bàn phím
        └── BenchmarkRunner.java             #   Đo đạc thời gian thực tế (System.nanoTime)
```

---

## 4. Kiến Trúc & Luồng Hoạt Động

### Sơ đồ tương tác luồng dữ liệu

```
[ Khách hàng ] ---> Đăng ký mua sắm ---> Thêm mặt hàng vào giỏ (CartItem)
                                                       |
                                                       v
                                          [ Xếp hàng chờ thanh toán ]
                                          /                         \
                          (Khách VIP / Gold)                       (Khách Regular)
                                  |                                       |
                                  v                                       v
                    [ VIPCustomerHeap (Max-Heap) ]          [ CheckoutQueueFIFO (FIFO) ]
                                  \                                       /
                                   -----> [ BÀN THU NGÂN (Cashier) ] <----
                                                      |
                                                      v
                                        Quét từng mã sản phẩm qua:
                                         [ ProductBST (BST O(log n)) ]
                                                      |
                                                      v
                                     - Kiểm tra & trừ tồn kho (Stock)
                                     - Áp dụng chiết khấu thành viên
                                     - In hóa đơn chi tiết (Invoice)
                                                      |
                                                      v
                                      [ AnalyticsService (Lịch sử) ]
```

---

## 5. Phân Công Nhiệm Vụ (Dành cho nhóm 4 thành viên)

| Thành viên | Trách nhiệm chính | Module phụ trách | File bàn giao |
|---|---|---|---|
| **Member 1** | Cây tìm kiếm nhị phân | `datastructure/bst/` | `ProductNode.java`, `ProductBST.java` |
| **Member 2** | Hàng đợi ưu tiên VIP | `datastructure/heap/` | `VIPCustomerHeap.java` |
| **Member 3** | Hàng chờ thanh toán thường | `datastructure/queue/` | `QueueNode.java`, `CheckoutQueueFIFO.java` |
| **Member 4** | Danh sách liên kết đôi kho | `datastructure/linkedlist/` | `ListNode.java`, `InventoryLinkedList.java` |

---

## 6. Hướng Dẫn Git Workflow

Mỗi thành viên làm việc trên một nhánh (branch) riêng biệt:
1. Tạo branch theo tên của mình:
   ```bash
   git checkout -b feature/member1-bst
   ```
2. Viết code và commit rõ ràng:
   ```bash
   git add .
   git commit -m "Implement ProductBST search and iterative insert"
   ```
3. Đẩy code lên GitHub và tạo Pull Request (PR):
   ```bash
   git push origin feature/member1-bst
   ```

---

## 7. Quy Tắc Viết Code (Coding Conventions)

* **Không sử dụng thư viện có sẵn của Java** (`java.util.TreeSet`, `java.util.LinkedList`, `java.util.PriorityQueue`) cho 4 cấu trúc dữ liệu cốt lõi trong package `datastructure/`. Phải tự viết con trỏ (`left`, `right`, `next`, `prev`) hoặc mảng heap.
* **Xử lý Null an toàn**: Kiểm tra tham số đầu vào (`null check`) ở tất cả các hàm public.
* **Format tiền tệ**: Sử dụng `Locale.US` để định dạng tiền tệ dấu chấm thập phân chuẩn quốc tế (ví dụ: `$25.50`).

---

## 8. Hướng Dẫn Implement Chi Tiết

### 8.1. Cây tìm kiếm nhị phân (`ProductBST`)
* Sử dụng vòng lặp duyệt `while (current != null)` để chèn và tìm kiếm theo mã `productId` (tránh đệ quy quá sâu gây `StackOverflowError` khi dữ liệu lớn).
* Khi xóa node có 2 con, tìm node nhỏ nhất bên nhánh phải (`In-order Successor`) để thay thế.

### 8.2. Danh sách liên kết đôi (`InventoryLinkedList`)
* Duy trì hai con trỏ `head` và `tail`.
* Thao tác `addLast` và `addFirst` thực hiện trong thời gian hằng số $O(1)$.
* Hỗ trợ duyệt tiến (`toForwardList`) và duyệt lùi (`toBackwardList`).

### 8.3. Hàng đợi thanh toán (`CheckoutQueueFIFO`)
* Duy trì con trỏ `front` và `rear`.
* `enqueue` chèn vào sau `rear`, `dequeue` lấy phần tử ra từ `front`.

### 8.4. Hàng đợi ưu tiên VIP (`VIPCustomerHeap`)
* Mảng heap động, tự động gấp đôi kích thước khi đầy.
* Thao tác `siftUp` đưa phần tử có độ ưu tiên cao nhất lên gốc (Root).
* Thao tác `siftDown` khôi phục tính chất heap sau khi `dequeue`.

---

## 9. Cách Test Riêng Phần Của Mình

Mỗi thành viên có thể tạo một file test riêng hoặc chạy trực tiếp bài đo hiệu năng trong `util/BenchmarkRunner.java`:
```powershell
java -Dfile.encoding=UTF-8 -cp bin util.BenchmarkRunner
```

---

## 10. Ghép Code & Tích Hợp

Khi các thành viên hoàn thành:
1. Leader kiểm tra các class cài đặt có tuân thủ đúng `interface` tương ứng không.
2. Chạy `Main.java` để xác nhận toàn bộ hệ thống hoạt động đồng bộ.
3. Chạy toàn bộ kịch bản kiểm thử:
   - Thêm sản phẩm mới $\rightarrow$ Kiểm tra hiển thị ở cả danh mục kho và cây BST.
   - Cho khách hàng vào hàng chờ $\rightarrow$ Kiểm tra thứ tự ưu tiên gọi tên.
   - Thực hiện thanh toán $\rightarrow$ Kiểm tra trừ tồn kho và in hóa đơn.

---

## 11. Xử Lý Lỗi Thường Gặp

| Mã lỗi / Hiện tượng | Nguyên nhân | Cách khắc phục |
|---|---|---|
| Lỗi hiển thị font tiếng Việt `??` trên Windows Console | Windows Command Prompt mặc định sử dụng Code Page 437 | Chạy `chcp 65001` và truyền tham số `-Dfile.encoding=UTF-8` khi chạy `java` |
| `StackOverflowError` khi chèn nhiều dữ liệu vào cây BST | Dữ liệu đầu vào bị sắp xếp sẵn khiến cây bị suy biến thành đường thẳng | Sử dụng cài đặt `insert` dạng vòng lặp lặp (iterative) có sẵn trong `ProductBST` |
| `NullPointerException` khi gọi khách hàng từ hàng chờ | Hàng đợi rỗng nhưng không kiểm tra `isEmpty()` trước | Luôn kiểm tra `if (!queue.isEmpty())` trước khi gọi `dequeue()` |

---

## 12. Đo Đạc Hiệu Năng (Benchmark) & Liên Hệ

### Kết quả đo thực nghiệm thực tế trên máy:

```text
==========================================================================================
                 SMART STORE MANAGER - PERFORMANCE BENCHMARK EXPERIMENT                   
==========================================================================================

[THỰC NGHIỆM 1] TỐC ĐỘ TÌM KIẾM SẢN PHẨM: BST O(log n) vs LINKED LIST O(n)
------------------------------------------------------------------------------------------
Kích thước (N) | BST Time (ms)      | Linked List Time (ms) | Tốc độ cải thiện    
------------------------------------------------------------------------------------------
100        | 1.6808 ms          | 9.3180 ms            | Nhanh hơn 5.5x
500        | 0.2901 ms          | 15.6478 ms           | Nhanh hơn 53.9x
1,000      | 0.3154 ms          | 18.9778 ms           | Nhanh hơn 60.2x
5,000      | 0.5892 ms          | 127.2380 ms          | Nhanh hơn 216.0x
10,000     | 0.5969 ms          | 188.4488 ms          | Nhanh hơn 315.7x
30,000     | 1.0039 ms          | 600.1714 ms          | Nhanh hơn 597.8x

[THỰC NGHIỆM 2] THÔNG LƯỢNG HÀNG ĐỢI: FIFO QUEUE vs BINARY MAX-HEAP
------------------------------------------------------------------------------------------
Kích thước (N) | Queue FIFO (Enqueue / Dequeue)   | Max-Heap VIP (Enqueue / Dequeue)
------------------------------------------------------------------------------------------
100        | 0.3122 ms / 0.0155 ms            | 0.1262 ms / 0.1507 ms
1,000      | 0.2124 ms / 0.1463 ms            | 0.2625 ms / 0.8408 ms
10,000     | 0.5445 ms / 0.0666 ms            | 0.7818 ms / 1.3111 ms
30,000     | 1.4522 ms / 0.2055 ms            | 1.7749 ms / 4.7825 ms
==========================================================================================
```

> **Đánh giá**:
> 1. Cây nhị phân tìm kiếm (**BST**) đạt hiệu năng vượt trội gấp **597.8 lần** so với duyệt tuyến tính trên Danh sách liên kết (**Linked List**) khi số lượng sản phẩm lớn ($N = 30,000$), minh chứng rõ ràng cho ưu thế lý thuyết $O(\log n)$ so với $O(n)$.
> 2. Hàng đợi **FIFO Queue** duy trì tốc độ hằng số $O(1)$ siêu nhanh cho hàng chờ thông thường, trong khi **Binary Max-Heap** $O(\log n)$ đảm bảo phân bổ thứ tự ưu tiên chính xác tuyệt đối cho các khách hàng VIP.
