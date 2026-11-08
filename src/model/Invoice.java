package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Lớp đại diện cho hóa đơn thanh toán hoàn chỉnh của khách hàng.
 */
public class Invoice {
    private String invoiceId;
    private Customer customer;
    private List<CartItem> purchasedItems;
    private double subtotal;
    private double discountAmount;
    private double finalTotal;
    private Date creationDate;

    public Invoice(String invoiceId, Customer customer) {
        this.invoiceId = invoiceId;
        this.customer = customer;
        this.purchasedItems = new ArrayList<>(customer.getCart());
        this.creationDate = new Date();
        calculateBill();
    }

    private void calculateBill() {
        this.subtotal = 0.0;
        for (CartItem item : purchasedItems) {
            this.subtotal += item.getSubtotal();
        }
        double discountRate = customer.getTier().getDiscountRate();
        this.discountAmount = this.subtotal * discountRate;
        this.finalTotal = this.subtotal - this.discountAmount;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<CartItem> getPurchasedItems() {
        return purchasedItems;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getFinalTotal() {
        return finalTotal;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String printFormattedReceipt() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("\n==============================================================\n");
        sb.append(String.format("                     HÓA ĐƠN BÁN HÀNG (%s)\n", invoiceId));
        sb.append("==============================================================\n");
        sb.append(String.format("Ngày giờ   : %s\n", sdf.format(creationDate)));
        sb.append(String.format("Khách hàng : %s (Mã: %s)\n", customer.getName(), customer.getCustomerId()));
        sb.append(String.format("Hạng thành viên: %s (Ưu đãi: %.0f%%)\n",
                customer.getTier().getDisplayName(), customer.getTier().getDiscountRate() * 100));
        sb.append("--------------------------------------------------------------\n");
        sb.append(String.format("%-7s | %-20s | %-4s | %-8s | %-9s\n", "MÃ SP", "TÊN SẢN PHẨM", "SL", "ĐƠN GIÁ", "THÀNH TIỀN"));
        sb.append("--------------------------------------------------------------\n");
        for (CartItem item : purchasedItems) {
            sb.append(String.format(java.util.Locale.US,
                    "%-7s | %-20s | %-4d | $%7.2f | $%8.2f\n",
                    item.getProduct().getProductId(),
                    truncate(item.getProduct().getName(), 20),
                    item.getQuantity(),
                    item.getProduct().getPrice(),
                    item.getSubtotal()));
        }
        sb.append("--------------------------------------------------------------\n");
        sb.append(String.format(java.util.Locale.US, "Tổng tiền hàng (Subtotal)     : $%10.2f\n", subtotal));
        sb.append(String.format(java.util.Locale.US, "Chiết khấu thành viên         : -$%9.2f\n", discountAmount));
        sb.append(String.format(java.util.Locale.US, "TỔNG CỘNG THANH TOÁN          : $%10.2f\n", finalTotal));
        sb.append("==============================================================\n");
        return sb.toString();
    }

    private String truncate(String text, int maxLen) {
        if (text == null) return "";
        return text.length() <= maxLen ? text : text.substring(0, maxLen - 3) + "...";
    }

    @Override
    public String toString() {
        return String.format(java.util.Locale.US,
                "Invoice [%s | Cust: %-15s | Items: %2d | Total: $%8.2f]",
                invoiceId, customer.getName(), purchasedItems.size(), finalTotal);
    }
}

