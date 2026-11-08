package service;

import model.CartItem;
import model.Invoice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lớp dịch vụ quản lý lịch sử hóa đơn, báo cáo doanh thu và thống kê hoạt động bán hàng.
 */
public class AnalyticsService {

    private final List<Invoice> invoiceHistory;

    public AnalyticsService() {
        this.invoiceHistory = new ArrayList<>();
    }

    /**
     * Ghi nhận một hóa đơn đã thanh toán thành công vào lịch sử.
     */
    public void recordInvoice(Invoice invoice) {
        if (invoice != null) {
            invoiceHistory.add(invoice);
        }
    }

    public List<Invoice> getInvoiceHistory() {
        return invoiceHistory;
    }

    /**
     * Hiển thị danh sách toàn bộ các hóa đơn đã thanh toán.
     */
    public void displayInvoiceHistory() {
        System.out.println("\n==========================================================================================");
        System.out.println("                             LỊCH SỬ HÓA ĐƠN ĐÃ THANH TOÁN                                ");
        System.out.println("==========================================================================================");
        if (invoiceHistory.isEmpty()) {
            System.out.println("  (Chưa có giao dịch nào được ghi nhận)");
            System.out.println("==========================================================================================");
            return;
        }

        int index = 1;
        for (Invoice inv : invoiceHistory) {
            System.out.printf("  %2d. %s%n", index++, inv);
        }
        System.out.println("==========================================================================================");
        printRevenueSummary();
    }

    /**
     * In bảng tổng kết doanh thu và thống kê bán hàng.
     */
    public void printRevenueSummary() {
        double totalRevenue = 0.0;
        double totalDiscounts = 0.0;
        int totalItemsSold = 0;
        Map<String, Integer> productSales = new HashMap<>();

        for (Invoice inv : invoiceHistory) {
            totalRevenue += inv.getFinalTotal();
            totalDiscounts += inv.getDiscountAmount();
            for (CartItem item : inv.getPurchasedItems()) {
                totalItemsSold += item.getQuantity();
                String pName = item.getProduct().getName();
                productSales.put(pName, productSales.getOrDefault(pName, 0) + item.getQuantity());
            }
        }

        System.out.println("\n--- [BÁO CÁO DOANH THU & HIỆU SUẤT BÁN HÀNG] ---");
        System.out.printf(java.util.Locale.US, "• Tổng số hóa đơn đã xử lý : %d đơn%n", invoiceHistory.size());
        System.out.printf(java.util.Locale.US, "• Tổng số sản phẩm đã bán  : %d món%n", totalItemsSold);
        System.out.printf(java.util.Locale.US, "• Tổng tiền giảm giá (VIP) : $%.2f%n", totalDiscounts);
        System.out.printf(java.util.Locale.US, "• TỔNG DOANH THU THỰC TẾ   : $%.2f%n", totalRevenue);

        if (!productSales.isEmpty()) {
            System.out.println("\n• Sản phẩm bán chạy:");
            for (Map.Entry<String, Integer> entry : productSales.entrySet()) {
                System.out.printf("  - %-22s: %d chiếc%n", entry.getKey(), entry.getValue());
            }
        }
        System.out.println("--------------------------------------------------");
    }
}

