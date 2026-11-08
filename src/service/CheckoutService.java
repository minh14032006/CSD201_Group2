package service;

import datastructure.heap.IVIPQueue;
import datastructure.heap.VIPCustomerHeap;
import datastructure.queue.CheckoutQueueFIFO;
import datastructure.queue.ICheckoutQueue;
import model.CartItem;
import model.Customer;
import model.Invoice;
import model.Product;

import java.util.List;

/**
 * Lớp dịch vụ quản lý hàng chờ thanh toán và nghiệp vụ thu ngân.
 * Hỗ trợ 2 làn thanh toán:
 * 1. Làn thanh toán thường: Sử dụng cấu trúc FIFO Queue (CheckoutQueueFIFO).
 * 2. Làn ưu tiên VIP: Sử dụng cấu trúc Binary Max-Heap (VIPCustomerHeap) theo hạng thành viên.
 */
public class CheckoutService {

    private final ICheckoutQueue normalQueue;
    private final IVIPQueue vipPriorityQueue;
    private final ProductService productService;
    private final AnalyticsService analyticsService;
    private int invoiceCounter = 1000;

    public CheckoutService(ProductService productService, AnalyticsService analyticsService) {
        this.normalQueue = new CheckoutQueueFIFO();
        this.vipPriorityQueue = new VIPCustomerHeap();
        this.productService = productService;
        this.analyticsService = analyticsService;
    }

    public ICheckoutQueue getNormalQueue() {
        return normalQueue;
    }

    public IVIPQueue getVipPriorityQueue() {
        return vipPriorityQueue;
    }

    /**
     * Khách hàng xếp hàng vào quầy thanh toán.
     * Tự động điều hướng: Khách có hạng thành viên VIP/Gold/Silver vào hàng ưu tiên Max-Heap,
     * khách Regular vào hàng FIFO thông thường.
     */
    public void customerJoinsQueue(Customer customer) {
        if (customer == null) return;
        customer.setArrivalTimestamp(System.currentTimeMillis());

        if (customer.getTier() != Customer.MembershipTier.REGULAR) {
            vipPriorityQueue.enqueue(customer);
            System.out.printf("[Hàng chờ] -> Khách hàng %s (%s) đã vào [LÀN ƯU TIÊN VIP] (Max-Heap).%n",
                    customer.getName(), customer.getTier().getDisplayName());
        } else {
            normalQueue.enqueue(customer);
            System.out.printf("[Hàng chờ] -> Khách hàng %s đã vào [HÀNG CHỜ THƯỜNG] (Queue FIFO).%n",
                    customer.getName());
        }
    }

    /**
     * Thu ngân gọi khách hàng tiếp theo để thanh toán:
     * Ưu tiên phục vụ khách trong hàng VIP trước; khi hàng VIP trống sẽ phục vụ hàng thường.
     * @return Khách hàng được gọi ra quầy
     */
    public Customer callNextCustomer() {
        if (!vipPriorityQueue.isEmpty()) {
            Customer c = vipPriorityQueue.dequeue();
            System.out.printf("[Thu ngân] <- Mời khách hàng VIP: %s (%s) đến quầy thanh toán!%n",
                    c.getName(), c.getTier().getDisplayName());
            return c;
        }
        if (!normalQueue.isEmpty()) {
            Customer c = normalQueue.dequeue();
            System.out.printf("[Thu ngân] <- Mời khách hàng: %s đến quầy thanh toán!%n",
                    c.getName());
            return c;
        }
        return null;
    }

    /**
     * Gọi khách hàng từ hàng thường (FIFO).
     */
    public Customer callNextNormalCustomer() {
        if (normalQueue.isEmpty()) return null;
        Customer c = normalQueue.dequeue();
        System.out.printf("[Thu ngân] <- Mời khách hàng thường: %s đến quầy!%n", c.getName());
        return c;
    }

    /**
     * Gọi khách hàng từ hàng VIP (Max-Heap).
     */
    public Customer callNextVIPCustomer() {
        if (vipPriorityQueue.isEmpty()) return null;
        Customer c = vipPriorityQueue.dequeue();
        System.out.printf("[Thu ngân] <- Mời khách hàng VIP: %s (%s) đến quầy!%n",
                c.getName(), c.getTier().getDisplayName());
        return c;
    }

    /**
     * Thực hiện quy trình thanh toán đầy đủ cho khách hàng:
     * 1. Tra cứu giá và thông tin từng sản phẩm trong giỏ qua cây BST O(log n).
     * 2. Kiểm tra tồn kho và trừ tồn kho thực tế.
     * 3. Áp dụng chính sách giảm giá theo hạng thành viên.
     * 4. Sinh hóa đơn, in hóa đơn và lưu vào lịch sử giao dịch.
     */
    public Invoice processCustomerCheckout(Customer customer) {
        if (customer == null) {
            System.out.println("[!] Không có khách hàng tại quầy.");
            return null;
        }

        if (customer.getCart().isEmpty()) {
            System.out.println("[!] Giỏ hàng của khách trống, không thể xuất hóa đơn.");
            return null;
        }

        System.out.printf("%n[Thu ngân] Đang quét mã sản phẩm và tính tiền cho khách hàng %s...%n", customer.getName());

        // Quét từng mã hàng và cập nhật tồn kho
        for (CartItem item : customer.getCart()) {
            Product scanned = productService.findProductById(item.getProduct().getProductId());
            if (scanned != null) {
                // Kiểm tra và trừ tồn kho
                if (scanned.getStockQuantity() < item.getQuantity()) {
                    System.out.printf("  [!] Sản phẩm %s chỉ còn %d trong kho, khách muốn mua %d.%n",
                            scanned.getName(), scanned.getStockQuantity(), item.getQuantity());
                    item.setQuantity(Math.max(0, scanned.getStockQuantity()));
                }
                scanned.deductStock(item.getQuantity());
                System.out.printf("  [Quét BST] Đã quét: [%s] %-20s x %d = $%6.2f (Tồn kho còn: %d)%n",
                        scanned.getProductId(), scanned.getName(), item.getQuantity(),
                        scanned.getPrice() * item.getQuantity(), scanned.getStockQuantity());
            } else {
                System.out.printf("  [!] Mã sản phẩm %s không tồn tại trong danh mục BST!%n",
                        item.getProduct().getProductId());
            }
        }

        // Tạo hóa đơn
        String invoiceId = "INV" + (++invoiceCounter);
        Invoice invoice = new Invoice(invoiceId, customer);

        // Lưu vào sổ giao dịch
        analyticsService.recordInvoice(invoice);

        // In hóa đơn ra màn hình
        System.out.print(invoice.printFormattedReceipt());

        // Làm sạch giỏ hàng của khách sau khi thanh toán thành công
        customer.clearCart();

        return invoice;
    }

    /**
     * Hiển thị trạng thái các hàng chờ thanh toán hiện tại.
     */
    public void displayAllQueues() {
        System.out.println("\n==========================================================================================");
        System.out.println("                         TRẠNG THÁI CÁC HÀNG CHỜ THANH TOÁN                               ");
        System.out.println("==========================================================================================");

        // 1. Hàng đợi VIP (Max-Heap)
        System.out.println("--- 1. LÀN ƯU TIÊN VIP (Binary Max-Heap) ---");
        List<Customer> vipList = vipPriorityQueue.toList();
        if (vipList.isEmpty()) {
            System.out.println("  (Không có khách nào đang chờ trong làn VIP)");
        } else {
            int pos = 1;
            for (Customer c : vipList) {
                System.out.printf("  Ưu tiên #%d: %s%n", pos++, c);
            }
        }

        // 2. Hàng đợi thường (FIFO)
        System.out.println("\n--- 2. HÀNG CHỜ THƯỜNG (Queue FIFO) ---");
        List<Customer> normalList = normalQueue.toList();
        if (normalList.isEmpty()) {
            System.out.println("  (Không có khách nào đang chờ trong hàng thường)");
        } else {
            int pos = 1;
            for (Customer c : normalList) {
                System.out.printf("  Vị trí #%d: %s%n", pos++, c);
            }
        }
        System.out.println("==========================================================================================");
    }
}

