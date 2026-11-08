package datastructure.bst;

import model.Product;
import java.util.List;

/**
 * Interface định nghĩa các hành vi của Cây tìm kiếm nhị phân (Binary Search Tree)
 * quản lý và tra cứu sản phẩm theo Product ID.
 */
public interface IProductBST {

    /**
     * Chèn một sản phẩm mới vào cây BST theo mã productId.
     * @param product Sản phẩm cần chèn
     * @return true nếu chèn thành công, false nếu ID đã tồn tại
     */
    boolean insert(Product product);

    /**
     * Tìm kiếm sản phẩm theo mã productId trong O(log n).
     * @param productId Mã sản phẩm cần tìm
     * @return Đối tượng Product nếu tìm thấy, null nếu không tồn tại
     */
    Product search(String productId);

    /**
     * Xóa sản phẩm khỏi cây BST theo mã productId.
     * @param productId Mã sản phẩm cần xóa
     * @return true nếu xóa thành công, false nếu không tìm thấy
     */
    boolean delete(String productId);

    /**
     * Cập nhật thông tin sản phẩm (giá và số lượng tồn kho) theo ID.
     */
    boolean update(String productId, double newPrice, int newStock);

    /**
     * Duyệt cây theo thứ tự In-Order (Left - Root - Right) để lấy danh sách sản phẩm
     * đã sắp xếp tăng dần theo Product ID.
     * @return Danh sách sản phẩm
     */
    List<Product> getInOrderList();

    /**
     * Trả về tổng số lượng sản phẩm hiện có trong cây BST.
     */
    int size();

    /**
     * Trả về chiều cao của cây BST.
     */
    int height();

    /**
     * Kiểm tra cây có rỗng không.
     */
    boolean isEmpty();

    /**
     * Xóa toàn bộ node trên cây.
     */
    void clear();
}

