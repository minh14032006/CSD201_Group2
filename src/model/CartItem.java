package model;

/**
 * Lớp đại diện cho một mục hàng trong giỏ hàng của khách hàng.
 */
public class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    public double getSubtotal() {
        return product.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return String.format(java.util.Locale.US,
                "%-7s | %-22s | Qty: %2d | Unit: $%6.2f | Subtotal: $%7.2f",
                product.getProductId(), product.getName(), quantity, product.getPrice(), getSubtotal());
    }
}

