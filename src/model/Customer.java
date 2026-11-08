package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp đại diện cho khách hàng đến mua sắm tại cửa hàng.
 */
public class Customer {

    public enum MembershipTier {
        REGULAR(1, "Khách thường", 0.0),
        SILVER(2, "Khách Bạc (Silver)", 0.05),
        GOLD(3, "Khách Vàng (Gold)", 0.10),
        VIP(4, "Khách VIP (Kim Cương)", 0.15);

        private final int priority;
        private final String displayName;
        private final double discountRate;

        MembershipTier(int priority, String displayName, double discountRate) {
            this.priority = priority;
            this.displayName = displayName;
            this.discountRate = discountRate;
        }

        public int getPriority() {
            return priority;
        }

        public String getDisplayName() {
            return displayName;
        }

        public double getDiscountRate() {
            return discountRate;
        }
    }

    private String customerId;          // Mã khách hàng (ví dụ: KH001)
    private String name;                // Họ và tên khách hàng
    private String phone;               // Số điện thoại
    private MembershipTier tier;        // Hạng thành viên (ảnh hưởng đến ưu tiên hàng chờ và giảm giá)
    private List<CartItem> cart;        // Danh sách các mặt hàng trong giỏ
    private long arrivalTimestamp;      // Thời điểm xếp hàng (dùng khi so sánh độ ưu tiên)

    public Customer(String customerId, String name, String phone, MembershipTier tier) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.tier = tier;
        this.cart = new ArrayList<>();
        this.arrivalTimestamp = System.currentTimeMillis();
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public MembershipTier getTier() {
        return tier;
    }

    public void setTier(MembershipTier tier) {
        this.tier = tier;
    }

    public List<CartItem> getCart() {
        return cart;
    }

    public long getArrivalTimestamp() {
        return arrivalTimestamp;
    }

    public void setArrivalTimestamp(long arrivalTimestamp) {
        this.arrivalTimestamp = arrivalTimestamp;
    }

    /**
     * Thêm sản phẩm vào giỏ hàng. Nếu sản phẩm đã có trong giỏ thì tăng số lượng.
     */
    public void addToCart(Product product, int quantity) {
        for (CartItem item : cart) {
            if (item.getProduct().getProductId().equalsIgnoreCase(product.getProductId())) {
                item.addQuantity(quantity);
                return;
            }
        }
        cart.add(new CartItem(product, quantity));
    }

    public void clearCart() {
        cart.clear();
    }

    public double calculateCartSubtotal() {
        double total = 0.0;
        for (CartItem item : cart) {
            total += item.getSubtotal();
        }
        return total;
    }

    @Override
    public String toString() {
        return String.format("Customer [ID: %-6s | Name: %-18s | Tier: %-16s | Items: %2d]",
                customerId, name, tier.getDisplayName(), cart.size());
    }
}

