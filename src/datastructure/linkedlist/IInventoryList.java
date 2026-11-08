package datastructure.linkedlist;

import model.Product;
import java.util.List;

/**
 * Interface định nghĩa các thao tác trên Danh sách liên kết đôi (Doubly Linked List)
 * dùng để quản lý toàn bộ danh mục sản phẩm kho hàng theo thứ tự thời gian nhập kho.
 */
public interface IInventoryList {

    /**
     * Thêm sản phẩm vào đầu danh sách liên kết.
     */
    void addFirst(Product product);

    /**
     * Thêm sản phẩm vào cuối danh sách liên kết.
     */
    void addLast(Product product);

    /**
     * Xóa sản phẩm khỏi danh sách theo mã productId.
     * @return true nếu xóa thành công, false nếu không tìm thấy
     */
    boolean removeById(String productId);

    /**
     * Tìm sản phẩm theo tên (hỗ trợ tìm kiếm tương đối không phân biệt hoa thường).
     */
    List<Product> searchByName(String keyword);

    /**
     * Lấy danh sách sản phẩm theo thứ tự xuôi (từ đầu đến cuối danh sách).
     */
    List<Product> toForwardList();

    /**
     * Lấy danh sách sản phẩm theo thứ tự ngược (từ cuối lên đầu danh sách).
     */
    List<Product> toBackwardList();

    /**
     * Trả về số lượng sản phẩm trong kho.
     */
    int size();

    /**
     * Kiểm tra kho có rỗng không.
     */
    boolean isEmpty();

    /**
     * Xóa toàn bộ danh sách kho hàng.
     */
    void clear();
}

