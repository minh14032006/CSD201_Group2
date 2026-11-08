package model;

/**
 * Lớp đại diện cho một sản phẩm trong hệ thống quản lý cửa hàng (StoreManager).
 */
public class Product {
    private String productId;       // Mã sản phẩm (ví dụ: PR001, PR002)
    private String name;            // Tên sản phẩm
    private String category;        // Danh mục sản phẩm (Điện tử, Phụ kiện, v.v.)
    private double price;           // Đơn giá ($)
    private int stockQuantity;      // Số lượng tồn kho

    public Product(String productId, String name, String category, double price, int stockQuantity) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    /**
     * Giảm số lượng tồn kho khi xuất bán.
     * @param quantity Số lượng mua
     * @return true nếu đủ hàng để trừ, ngược lại false
     */
    public boolean deductStock(int quantity) {
        if (this.stockQuantity >= quantity) {
            this.stockQuantity -= quantity;
            return true;
        }
        return false;
    }

    /**
     * Tăng số lượng tồn kho khi nhập thêm hàng.
     * @param quantity Số lượng nhập thêm
     */
    public void addStock(int quantity) {
        if (quantity > 0) {
            this.stockQuantity += quantity;
        }
    }

    @Override
    public String toString() {
        return String.format(java.util.Locale.US,
                "Product [ID: %-7s | Name: %-22s | Cat: %-12s | Price: $%7.2f | Stock: %3d]",
                productId, name, category, price, stockQuantity);
    }
}

