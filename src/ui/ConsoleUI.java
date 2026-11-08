package ui;

import model.Customer;
import model.Product;
import service.AnalyticsService;
import service.CheckoutService;
import service.ProductService;
import util.Acceptable;
import util.BenchmarkRunner;
import util.Inputter;

import java.util.List;
import java.util.Scanner;

/**
 * Giao diện người dùng dạng Console tương tác theo vai trò (Role-Based Interface):
 * 1. Store Manager (Quản lý cửa hàng & kho)
 * 2. Cashier (Thu ngân & quẹt thẻ tính tiền)
 * 3. Customer (Khách hàng mua sắm)
 * 4. Performance Benchmark (Đo đạc hiệu năng DSA cho báo cáo)
 */
public class ConsoleUI {

    private final Scanner scanner;
    private final ProductService productService;
    private final CheckoutService checkoutService;
    private final AnalyticsService analyticsService;

    public ConsoleUI(ProductService productService, CheckoutService checkoutService, AnalyticsService analyticsService) {
        this.scanner = new Scanner(System.in);
        this.productService = productService;
        this.checkoutService = checkoutService;
        this.analyticsService = analyticsService;
    }

    /**
     * Khởi động vòng lặp menu chính của hệ thống.
     */
    public void start() {
        while (true) {
            printMainMenu();
            int choice = Inputter.getInt("Lựa chọn của bạn [0-4]: ", 0, 4);
            switch (choice) {
                case 1:
                    handleManagerMenu();
                    break;
                case 2:
                    handleCashierMenu();
                    break;
                case 3:
                    handleCustomerMenu();
                    break;
                case 4:
                    handleBenchmarkMenu();
                    break;
                case 0:
                    System.out.println("\n[!] Đã đóng hệ thống Quản lý cửa hàng StoreManager. Tạm biệt!");
                    return;
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n==================================================================");
        System.out.println("          HỆ THỐNG QUẢN LÝ CỬA HÀNG - SMART STORE MANAGER         ");
        System.out.println("           (CSD201: CẤU TRÚC DỮ LIỆU & TƯ DUY TÍNH TOÁN)          ");
        System.out.println("==================================================================");
        System.out.println("  1. Giao diện Quản lý kho (Store Manager Interface)");
        System.out.println("  2. Giao diện Thu ngân (Cashier Interface)");
        System.out.println("  3. Giao diện Khách hàng mua sắm (Customer Interface)");
        System.out.println("  4. Đo đạc hiệu năng thuật toán & Cấu trúc dữ liệu (Benchmark)");
        System.out.println("  0. Thoát chương trình");
        System.out.println("==================================================================");
    }

    // ========================================================================
    // 1. GIAO DIỆN QUẢN LÝ KHO (STORE MANAGER)
    // ========================================================================
    private void handleManagerMenu() {
        while (true) {
            System.out.println("\n--------------------------------------------------");
            System.out.println("       GIAO DIỆN QUẢN LÝ KHO (STORE MANAGER)      ");
            System.out.println("--------------------------------------------------");
            System.out.println("  1. Xem danh mục kho hàng (Duyệt Doubly Linked List)");
            System.out.println("  2. Xem danh mục cây BST (Duyệt In-Order theo ID)");
            System.out.println("  3. Nhập sản phẩm mới vào kho");
            System.out.println("  4. Cập nhật giá bán / Số lượng tồn kho");
            System.out.println("  5. Xóa sản phẩm khỏi hệ thống");
            System.out.println("  6. Kiểm tra cảnh báo hàng tồn kho thấp");
            System.out.println("  7. Xem báo cáo doanh thu & Lịch sử hóa đơn");
            System.out.println("  0. Quay lại Menu chính");
            System.out.println("--------------------------------------------------");

            int choice = Inputter.getInt("Nhập lựa chọn [0-7]: ", 0, 7);
            switch (choice) {
                case 1:
                    productService.displayInventoryCatalog();
                    break;
                case 2:
                    productService.displayBSTInOrder();
                    break;
                case 3:
                    productService.addNewProductInteractive();
                    break;
                case 4:
                    handleUpdateProduct();
                    break;
                case 5:
                    handleDeleteProduct();
                    break;
                case 6:
                    int threshold = Inputter.getInt("Nhập mức ngưỡng tồn kho để cảnh báo: ", 1, 100);
                    productService.displayLowStockAlert(threshold);
                    break;
                case 7:
                    analyticsService.displayInvoiceHistory();
                    break;
                case 0:
                    return;
            }
        }
    }

    private void handleUpdateProduct() {
        System.out.println("\n--- [CẬP NHẬT SẢN PHẨM] ---");
        String id = Inputter.getString("Nhập Mã sản phẩm cần sửa: ", Acceptable.PRODUCT_ID_VALID, "Mã không đúng định dạng PRxxx.");
        Product p = productService.findProductById(id);
        if (p == null) {
            System.out.println("[!] Không tìm thấy sản phẩm có mã: " + id);
            return;
        }
        System.out.println("Thông tin hiện tại: " + p);
        double newPrice = Inputter.getDouble("Nhập Giá bán mới ($): ", 0.01);
        int newStock = Inputter.getInt("Nhập Số lượng tồn kho mới: ", 0, 10000);
        p.setPrice(newPrice);
        p.setStockQuantity(newStock);
        System.out.println("[+] Cập nhật thành công: " + p);
    }

    private void handleDeleteProduct() {
        System.out.println("\n--- [XÓA SẢN PHẨM] ---");
        String id = Inputter.getString("Nhập Mã sản phẩm cần xóa: ", Acceptable.PRODUCT_ID_VALID, "Mã không đúng định dạng PRxxx.");
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            System.out.println("[+] Đã xóa thành công sản phẩm mã " + id + " khỏi kho hàng và cây BST.");
        } else {
            System.out.println("[!] Không tìm thấy sản phẩm có mã " + id + " để xóa.");
        }
    }

    // ========================================================================
    // 2. GIAO DIỆN THU NGÂN (CASHIER)
    // ========================================================================
    private void handleCashierMenu() {
        while (true) {
            System.out.println("\n--------------------------------------------------");
            System.out.println("          GIAO DIỆN THU NGÂN (CASHIER)            ");
            System.out.println("--------------------------------------------------");
            System.out.println("  1. Gọi khách hàng tiếp theo để thanh toán (Ưu tiên VIP)");
            System.out.println("  2. Gọi khách hàng làn ưu tiên VIP (Max-Heap)");
            System.out.println("  3. Gọi khách hàng làn thường (FIFO Queue)");
            System.out.println("  4. Tra cứu nhanh giá sản phẩm theo ID (BST O(log n))");
            System.out.println("  5. Xem tình trạng các hàng chờ thanh toán");
            System.out.println("  0. Quay lại Menu chính");
            System.out.println("--------------------------------------------------");

            int choice = Inputter.getInt("Nhập lựa chọn [0-5]: ", 0, 5);
            switch (choice) {
                case 1:
                    Customer nextCust = checkoutService.callNextCustomer();
                    if (nextCust != null) {
                        checkoutService.processCustomerCheckout(nextCust);
                    } else {
                        System.out.println("[!] Hiện tại không có khách hàng nào đang chờ thanh toán.");
                    }
                    break;
                case 2:
                    Customer nextVIP = checkoutService.callNextVIPCustomer();
                    if (nextVIP != null) {
                        checkoutService.processCustomerCheckout(nextVIP);
                    } else {
                        System.out.println("[!] Làn ưu tiên VIP hiện đang trống.");
                    }
                    break;
                case 3:
                    Customer nextNormal = checkoutService.callNextNormalCustomer();
                    if (nextNormal != null) {
                        checkoutService.processCustomerCheckout(nextNormal);
                    } else {
                        System.out.println("[!] Làn thường hiện đang trống.");
                    }
                    break;
                case 4:
                    handleQuickLookupBST();
                    break;
                case 5:
                    checkoutService.displayAllQueues();
                    break;
                case 0:
                    return;
            }
        }
    }

    private void handleQuickLookupBST() {
        System.out.println("\n--- [QUÉT MÃ TRA CỨU GIÁ SẢN PHẨM - BST O(log n)] ---");
        String id = Inputter.getString("Nhập Mã sản phẩm cần quét (ví dụ PR001): ", Acceptable.PRODUCT_ID_VALID, "Mã PRxxx không hợp lệ.");
        long start = System.nanoTime();
        Product p = productService.findProductById(id);
        long end = System.nanoTime();
        if (p != null) {
            System.out.println("[V] KẾT QUẢ TÌM THẤY TRÊN CÂY BST: " + p);
            System.out.printf("    (Thời gian tìm kiếm BST: %.3f µs)%n", (end - start) / 1000.0);
        } else {
            System.out.println("[X] KHÔNG TÌM THẤY: Mã sản phẩm " + id + " không tồn tại trong danh mục.");
        }
    }

    // ========================================================================
    // 3. GIAO DIỆN KHÁCH HÀNG (CUSTOMER)
    // ========================================================================
    private void handleCustomerMenu() {
        while (true) {
            System.out.println("\n--------------------------------------------------");
            System.out.println("      GIAO DIỆN KHÁCH HÀNG MUA SẮM (CUSTOMER)     ");
            System.out.println("--------------------------------------------------");
            System.out.println("  1. Xem danh mục sản phẩm đang bày bán");
            System.out.println("  2. Tìm kiếm sản phẩm theo tên (Linked List)");
            System.out.println("  3. Tra cứu chi tiết sản phẩm theo ID (BST)");
            System.out.println("  4. Đăng ký thông tin mua hàng & Chọn đồ vào giỏ");
            System.out.println("  0. Quay lại Menu chính");
            System.out.println("--------------------------------------------------");

            int choice = Inputter.getInt("Nhập lựa chọn [0-4]: ", 0, 4);
            switch (choice) {
                case 1:
                    productService.displayInventoryCatalog();
                    break;
                case 2:
                    String kw = Inputter.getNonEmptyString("Nhập từ khóa tên sản phẩm: ");
                    List<Product> matches = productService.findProductsByName(kw);
                    if (matches.isEmpty()) {
                        System.out.println("[!] Không tìm thấy sản phẩm nào khớp với từ khóa: " + kw);
                    } else {
                        System.out.printf("Tìm thấy %d sản phẩm phù hợp:%n", matches.size());
                        for (Product m : matches) {
                            System.out.println("  * " + m);
                        }
                    }
                    break;
                case 3:
                    handleQuickLookupBST();
                    break;
                case 4:
                    handleCustomerShoppingFlow();
                    break;
                case 0:
                    return;
            }
        }
    }

    private void handleCustomerShoppingFlow() {
        System.out.println("\n--- [ĐĂNG KÝ MUA SẮM & CHỌN HÀNG VÀO GIỎ] ---");
        String custId = Inputter.getString("Nhập Mã khách hàng (ví dụ KH005): ", Acceptable.CUSTOMER_ID_VALID, "Mã KHxxx không hợp lệ.");
        String name = Inputter.getNonEmptyString("Nhập Họ tên khách hàng: ");
        String phone = Inputter.getString("Nhập Số điện thoại (10 chữ số): ", Acceptable.PHONE_VALID, "SĐT không đúng định dạng Việt Nam.");

        System.out.println("Chọn hạng thành viên:");
        System.out.println("  1. Regular (Khách thường - 0% ưu đãi)");
        System.out.println("  2. Silver  (Khách Bạc - 5% ưu đãi)");
        System.out.println("  3. Gold    (Khách Vàng - 10% ưu đãi)");
        System.out.println("  4. VIP     (Khách Kim Cương - 15% ưu đãi & Làn ưu tiên)");
        int tierChoice = Inputter.getInt("Chọn hạng [1-4]: ", 1, 4);

        Customer.MembershipTier tier = Customer.MembershipTier.REGULAR;
        if (tierChoice == 2) tier = Customer.MembershipTier.SILVER;
        else if (tierChoice == 3) tier = Customer.MembershipTier.GOLD;
        else if (tierChoice == 4) tier = Customer.MembershipTier.VIP;

        Customer customer = new Customer(custId, name, phone, tier);

        // Chọn hàng vào giỏ
        while (true) {
            System.out.println("\n[GIỎ HÀNG CỦA BẠN: " + customer.getCart().size() + " MÓN]");
            String pId = Inputter.getString("Nhập Mã sản phẩm muốn mua (hoặc 'DONE' để hoàn tất): ", "^(PR[0-9]{3,}|DONE|done)$", "Nhập PRxxx hoặc DONE.");
            if (pId.equalsIgnoreCase("DONE")) {
                break;
            }
            Product product = productService.findProductById(pId);
            if (product == null) {
                System.out.println("[!] Sản phẩm mã " + pId + " không tồn tại.");
                continue;
            }
            if (product.getStockQuantity() <= 0) {
                System.out.println("[!] Sản phẩm " + product.getName() + " hiện đã hết hàng.");
                continue;
            }
            int qty = Inputter.getInt("Nhập số lượng muốn mua (Tồn kho: " + product.getStockQuantity() + "): ", 1, product.getStockQuantity());
            customer.addToCart(product, qty);
            System.out.println("[+] Đã thêm " + qty + " chiếc " + product.getName() + " vào giỏ.");
        }

        if (customer.getCart().isEmpty()) {
            System.out.println("[!] Bạn chưa chọn sản phẩm nào vào giỏ hàng.");
            return;
        }

        // Tự động xếp hàng
        System.out.println("\n--- [XẾP HÀNG THANH TOÁN] ---");
        checkoutService.customerJoinsQueue(customer);
        System.out.println("[+] Bạn đã được xếp vào hàng chờ thanh toán. Vui lòng chuyển sang quầy Thu ngân để được phục vụ.");
    }

    // ========================================================================
    // 4. BENCHMARK PERFORMANCE RUNNER
    // ========================================================================
    private void handleBenchmarkMenu() {
        System.out.println("\n--------------------------------------------------");
        System.out.println("      ĐO ĐẠC HIỆU NĂNG THỰC NGHIỆM (BENCHMARK)     ");
        System.out.println("--------------------------------------------------");
        System.out.println("1. So sánh tốc độ tìm kiếm: Cây BST vs Duyệt Linked List");
        System.out.println("2. So sánh thông lượng: Queue FIFO vs Max-Heap");
        System.out.println("3. Chạy toàn bộ bài đo thực nghiệm Benchmark đầy đủ");
        System.out.println("0. Quay lại");
        System.out.println("--------------------------------------------------");
        int choice = Inputter.getInt("Nhập lựa chọn [0-3]: ", 0, 3);
        if (choice == 1 || choice == 2 || choice == 3) {
            BenchmarkRunner.runAllBenchmarks();
        }
    }
}

