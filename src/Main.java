import model.Customer;
import model.Product;
import service.AnalyticsService;
import service.CheckoutService;
import service.ProductService;
import ui.ConsoleUI;

/**
 * Điểm vào (Entry Point) chính của Hệ thống Smart Store Management System.
 * Khởi tạo Mock Data mẫu và bắt đầu giao diện điều khiển Console tương tác.
 */
public class Main {

    public static void main(String[] args) {
        // 1. Khởi tạo các tầng Service
        ProductService productService = new ProductService();
        AnalyticsService analyticsService = new AnalyticsService();
        CheckoutService checkoutService = new CheckoutService(productService, analyticsService);

        // 2. Nạp dữ liệu mẫu (Mock Data)
        initMockData(productService, checkoutService);

        // 3. Khởi chạy Giao diện điều khiển Console
        ConsoleUI consoleUI = new ConsoleUI(productService, checkoutService, analyticsService);
        consoleUI.start();
    }

    /**
     * Khởi tạo dữ liệu mẫu thực tế cho kho hàng và hàng chờ mua sắm.
     */
    private static void initMockData(ProductService productService, CheckoutService checkoutService) {
        System.out.println("[*] Đang khởi tạo dữ liệu mẫu (Mock Data) cho cửa hàng...");

        // Khởi tạo các sản phẩm trong kho hàng (Tự động lưu vào Linked List & Cây BST)
        Product p1 = new Product("PR001", "Bàn phím cơ Akko 3087", "Bàn phím", 65.00, 25);
        Product p2 = new Product("PR002", "Chuột Logitech G102",    "Chuột",     22.50, 40);
        Product p3 = new Product("PR003", "Cáp sạc USB-C Anker",    "Phụ kiện",  12.00, 50);
        Product p4 = new Product("PR004", "Màn hình Dell 24 inch",  "Màn hình", 145.00, 15);
        Product p5 = new Product("PR005", "Tai nghe Sony WH-1000",  "Tai nghe", 280.00, 10);
        Product p6 = new Product("PR006", "Webcam FHD Logitech",    "Phụ kiện",  48.00, 18);
        Product p7 = new Product("PR007", "Giá đỡ Laptop nhôm",     "Phụ kiện",  18.50, 30);
        Product p8 = new Product("PR008", "Củ sạc nhanh Baseus 65W", "Phụ kiện", 26.00,  8);

        productService.addProduct(p1);
        productService.addProduct(p2);
        productService.addProduct(p3);
        productService.addProduct(p4);
        productService.addProduct(p5);
        productService.addProduct(p6);
        productService.addProduct(p7);
        productService.addProduct(p8);

        // Khởi tạo các khách hàng mẫu đang xếp hàng tại quầy thanh toán
        Customer c1 = new Customer("KH001", "Nguyen Van An", "0981234567", Customer.MembershipTier.VIP);
        c1.addToCart(p5, 1); // Tai nghe Sony ($280)
        c1.addToCart(p3, 2); // 2 Cáp sạc USB-C ($24)

        Customer c2 = new Customer("KH002", "Tran Thi Mai", "0912345678", Customer.MembershipTier.GOLD);
        c2.addToCart(p1, 1); // Bàn phím cơ ($65)
        c2.addToCart(p2, 1); // Chuột Logitech ($22.50)

        Customer c3 = new Customer("KH003", "Le Hoang Phuc", "0977889900", Customer.MembershipTier.REGULAR);
        c3.addToCart(p4, 1); // Màn hình Dell ($145)
        c3.addToCart(p8, 1); // Củ sạc nhanh Baseus ($26)

        Customer c4 = new Customer("KH004", "Pham Thu Huong", "0903334455", Customer.MembershipTier.SILVER);
        c4.addToCart(p6, 1); // Webcam Logitech ($48)

        // Khách hàng xếp hàng vào các làn thanh toán
        checkoutService.customerJoinsQueue(c1); // Vào làn VIP (Max-Heap)
        checkoutService.customerJoinsQueue(c2); // Vào làn VIP (Max-Heap)
        checkoutService.customerJoinsQueue(c4); // Vào làn VIP (Max-Heap)
        checkoutService.customerJoinsQueue(c3); // Vào làn thường (FIFO Queue)

        System.out.println("[*] Đã nạp thành công 8 sản phẩm vào kho & 4 khách hàng mẫu vào hàng chờ thanh toán.\n");
    }
}

