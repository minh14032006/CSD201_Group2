package service;

import datastructure.bst.IProductBST;
import datastructure.bst.ProductBST;
import datastructure.linkedlist.IInventoryList;
import datastructure.linkedlist.InventoryLinkedList;
import model.Product;
import util.Acceptable;
import util.Inputter;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp dịch vụ quản lý sản phẩm và kho hàng.
 * Đồng bộ hóa giữa Danh sách liên kết đôi (IInventoryList) và Cây tìm kiếm nhị phân (IProductBST).
 */
public class ProductService {

    private final IProductBST productBST;
    private final IInventoryList inventoryList;

    public ProductService() {
        this.productBST = new ProductBST();
        this.inventoryList = new InventoryLinkedList();
    }

    public ProductService(IProductBST productBST, IInventoryList inventoryList) {
        this.productBST = productBST;
        this.inventoryList = inventoryList;
    }

    public IProductBST getProductBST() {
        return productBST;
    }

    public IInventoryList getInventoryList() {
        return inventoryList;
    }

    /**
     * Thêm sản phẩm mới vào kho hàng (tự động cập nhật cả BST và Linked List).
     */
    public boolean addProduct(Product product) {
        if (product == null) return false;
        // Kiểm tra xem ID đã tồn tại trong BST chưa
        if (productBST.search(product.getProductId()) != null) {
            return false;
        }
        productBST.insert(product);
        inventoryList.addLast(product);
        return true;
    }

    /**
     * Thêm sản phẩm thông qua giao diện nhập liệu console tương tác.
     */
    public void addNewProductInteractive() {
        System.out.println("\n--- [THÊM SẢN PHẨM MỚI VÀO KHO HÀNG] ---");
        String id;
        while (true) {
            id = Inputter.getString("Nhập Mã sản phẩm (ví dụ PR001, PR105): ",
                    Acceptable.PRODUCT_ID_VALID,
                    "Mã sản phẩm không đúng định dạng PRxxx (ví dụ: PR001).");
            if (productBST.search(id) != null) {
                System.out.println("[!] Mã sản phẩm " + id + " đã tồn tại trong kho. Vui lòng nhập mã khác.");
            } else {
                break;
            }
        }

        String name = Inputter.getNonEmptyString("Nhập Tên sản phẩm: ");
        String category = Inputter.getNonEmptyString("Nhập Danh mục sản phẩm (Điện tử, Gia dụng, Phụ kiện...): ");
        double price = Inputter.getDouble("Nhập Đơn giá ($): ", 0.01);
        int stock = Inputter.getInt("Nhập Số lượng tồn kho: ", 1, 10000);

        Product p = new Product(id, name, category, price, stock);
        boolean success = addProduct(p);
        if (success) {
            System.out.println("[+] Đã nhập kho thành công sản phẩm: " + p.getName() + " (Mã: " + p.getProductId() + ")");
        } else {
            System.out.println("[!] Có lỗi xảy ra khi thêm sản phẩm.");
        }
    }

    /**
     * Tìm kiếm sản phẩm siêu tốc O(log n) theo mã ID bằng cây BST.
     */
    public Product findProductById(String productId) {
        return productBST.search(productId);
    }

    /**
     * Tìm kiếm sản phẩm theo từ khóa tên (dùng Linked List duyệt tuyến tính).
     */
    public List<Product> findProductsByName(String keyword) {
        return inventoryList.searchByName(keyword);
    }

    /**
     * Cập nhật thông tin giá hoặc số lượng tồn kho theo ID.
     */
    public boolean updateProduct(String productId, double newPrice, int newStock) {
        return productBST.update(productId, newPrice, newStock);
    }

    /**
     * Xóa sản phẩm khỏi kho (cập nhật cả BST và Linked List).
     */
    public boolean deleteProduct(String productId) {
        boolean bstDeleted = productBST.delete(productId);
        boolean listDeleted = inventoryList.removeById(productId);
        return bstDeleted || listDeleted;
    }

    /**
     * Hiển thị danh mục toàn bộ sản phẩm theo thứ tự nhập kho (Duyệt Linked List).
     */
    public void displayInventoryCatalog() {
        System.out.println("\n==========================================================================================");
        System.out.println("                     DANH MỤC SẢN PHẨM KHO HÀNG (DOUBLY LINKED LIST)                      ");
        System.out.println("==========================================================================================");
        List<Product> list = inventoryList.toForwardList();
        if (list.isEmpty()) {
            System.out.println("  (Kho hàng hiện đang rỗng)");
            System.out.println("==========================================================================================");
            return;
        }
        int index = 1;
        for (Product p : list) {
            System.out.printf("  %2d. %s%n", index++, p);
        }
        System.out.println("==========================================================================================");
        System.out.printf("Tổng số mặt hàng trong kho: %d sản phẩm.%n", list.size());
    }

    /**
     * Hiển thị sản phẩm đã được sắp xếp tăng dần theo ID (Duyệt cây BST In-Order).
     */
    public void displayBSTInOrder() {
        System.out.println("\n==========================================================================================");
        System.out.println("             DANH MỤC SẢN PHẨM TRÊN CÂY BST (SẮP XẾP THEO ID TĂNG DẦN - IN-ORDER)         ");
        System.out.println("==========================================================================================");
        List<Product> list = productBST.getInOrderList();
        if (list.isEmpty()) {
            System.out.println("  (Cây BST hiện đang rỗng)");
            System.out.println("==========================================================================================");
            return;
        }
        int index = 1;
        for (Product p : list) {
            System.out.printf("  %2d. %s%n", index++, p);
        }
        System.out.println("==========================================================================================");
        System.out.printf("Tổng số node trên cây BST: %d node (Chiều cao cây: %d)%n", productBST.size(), productBST.height());
    }

    /**
     * Hiển thị các mặt hàng sắp hết (tồn kho dưới mức ngưỡng threshold).
     */
    public void displayLowStockAlert(int threshold) {
        System.out.printf("%n--- [CẢNH BÁO TỒN KHO THẤP (Dưới %d sản phẩm)] ---%n", threshold);
        List<Product> all = inventoryList.toForwardList();
        List<Product> lowStock = new ArrayList<>();
        for (Product p : all) {
            if (p.getStockQuantity() <= threshold) {
                lowStock.add(p);
            }
        }
        if (lowStock.isEmpty()) {
            System.out.println("  [V] Toàn bộ mặt hàng trong kho đều đủ số lượng an toàn.");
        } else {
            for (Product p : lowStock) {
                System.out.printf("  [!] CẢNH BÁO: [%s] %-20s - Chỉ còn %d sản phẩm trong kho!%n",
                        p.getProductId(), p.getName(), p.getStockQuantity());
            }
        }
    }
}

