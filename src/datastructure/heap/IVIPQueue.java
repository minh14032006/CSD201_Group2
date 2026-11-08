package datastructure.heap;

import model.Customer;
import java.util.List;

/**
 * Interface cho Hàng đợi ưu tiên VIP (Priority Queue) sử dụng cấu trúc Binary Max-Heap.
 * Khách hàng có hạng thành viên cao hơn (VIP > GOLD > SILVER > REGULAR) sẽ được ưu tiên phục vụ trước.
 */
public interface IVIPQueue {

    /**
     * Thêm khách hàng vào hàng đợi ưu tiên.
     * Độ phức tạp: O(log n) do thao tác sift-up.
     */
    void enqueue(Customer customer);

    /**
     * Lấy khách hàng có mức ưu tiên cao nhất ra phục vụ.
     * Độ phức tạp: O(log n) do thao tác sift-down.
     */
    Customer dequeue();

    /**
     * Xem khách hàng có mức ưu tiên cao nhất mà không xóa khỏi hàng đợi.
     */
    Customer peek();

    /**
     * Trả về số lượng khách hàng trong hàng đợi ưu tiên.
     */
    int size();

    /**
     * Kiểm tra hàng đợi có rỗng không.
     */
    boolean isEmpty();

    /**
     * Lấy danh sách khách hàng trong heap (theo mảng).
     */
    List<Customer> toList();

    /**
     * Xóa toàn bộ hàng đợi.
     */
    void clear();
}

