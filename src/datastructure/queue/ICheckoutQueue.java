package datastructure.queue;

import model.Customer;
import java.util.List;

/**
 * Interface định nghĩa hàng đợi thanh toán của khách hàng (Checkout Queue).
 * Tuân thủ nguyên tắc First-In, First-Out (FIFO).
 */
public interface ICheckoutQueue {

    /**
     * Thêm khách hàng vào cuối hàng chờ thanh toán.
     */
    void enqueue(Customer customer);

    /**
     * Lấy khách hàng ở đầu hàng ra thanh toán và xóa khỏi hàng đợi.
     * @return Khách hàng đầu hàng, hoặc null nếu hàng đợi rỗng
     */
    Customer dequeue();

    /**
     * Xem khách hàng ở đầu hàng mà không xóa khỏi hàng đợi.
     */
    Customer peek();

    /**
     * Trả về số lượng khách hàng đang chờ trong hàng đợi.
     */
    int size();

    /**
     * Kiểm tra hàng đợi có rỗng không.
     */
    boolean isEmpty();

    /**
     * Lấy danh sách toàn bộ khách hàng đang xếp hàng theo đúng thứ tự FIFO.
     */
    List<Customer> toList();

    /**
     * Xóa toàn bộ hàng đợi.
     */
    void clear();
}

