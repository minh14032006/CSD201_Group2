package datastructure.queue;

import model.Customer;
import java.util.ArrayList;
import java.util.List;

/**
 * Cài đặt Hàng đợi thanh toán (FIFO Queue) thủ công bằng danh sách liên kết.
 * Thao tác enqueue và dequeue đều đạt độ phức tạp O(1) nhờ duy trì con trỏ front và rear.
 */
public class CheckoutQueueFIFO implements ICheckoutQueue {

    private QueueNode front;
    private QueueNode rear;
    private int size;

    public CheckoutQueueFIFO() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    @Override
    public void enqueue(Customer customer) {
        if (customer == null) return;
        QueueNode newNode = new QueueNode(customer);
        if (isEmpty()) {
            front = rear = newNode;
        } else {
            rear.setNext(newNode);
            rear = newNode;
        }
        size++;
    }

    @Override
    public Customer dequeue() {
        if (isEmpty()) {
            return null;
        }
        Customer customer = front.getData();
        front = front.getNext();
        if (front == null) {
            rear = null;
        }
        size--;
        return customer;
    }

    @Override
    public Customer peek() {
        return isEmpty() ? null : front.getData();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return front == null;
    }

    @Override
    public List<Customer> toList() {
        List<Customer> list = new ArrayList<>();
        QueueNode current = front;
        while (current != null) {
            list.add(current.getData());
            current = current.getNext();
        }
        return list;
    }

    @Override
    public void clear() {
        front = rear = null;
        size = 0;
    }
}

