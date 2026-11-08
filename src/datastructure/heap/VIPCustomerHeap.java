package datastructure.heap;

import model.Customer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Cài đặt Hàng đợi ưu tiên bằng Cấu trúc Binary Max-Heap cho quầy phục vụ VIP.
 * - Khóa ưu tiên chính: Điểm hạng thành viên (VIP: 4, GOLD: 3, SILVER: 2, REGULAR: 1).
 * - Khóa phụ: Khách hàng nào đến trước (arrivalTimestamp nhỏ hơn) thì ưu tiên hơn khi cùng hạng.
 */
public class VIPCustomerHeap implements IVIPQueue {

    private static final int DEFAULT_CAPACITY = 16;
    private Customer[] heap;
    private int size;

    public VIPCustomerHeap() {
        this.heap = new Customer[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @Override
    public void enqueue(Customer customer) {
        if (customer == null) return;
        ensureCapacity();
        heap[size] = customer;
        siftUp(size);
        size++;
    }

    @Override
    public Customer dequeue() {
        if (isEmpty()) return null;

        Customer top = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;

        if (size > 0) {
            siftDown(0);
        }
        return top;
    }

    @Override
    public Customer peek() {
        return isEmpty() ? null : heap[0];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public List<Customer> toList() {
        List<Customer> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(heap[i]);
        }
        return list;
    }

    @Override
    public void clear() {
        Arrays.fill(heap, 0, size, null);
        size = 0;
    }

    private void siftUp(int index) {
        int current = index;
        while (current > 0) {
            int parent = (current - 1) / 2;
            if (compare(heap[current], heap[parent]) > 0) {
                swap(current, parent);
                current = parent;
            } else {
                break;
            }
        }
    }

    private void siftDown(int index) {
        int current = index;
        while (current * 2 + 1 < size) {
            int leftChild = current * 2 + 1;
            int rightChild = current * 2 + 2;
            int highestPriorityChild = leftChild;

            if (rightChild < size && compare(heap[rightChild], heap[leftChild]) > 0) {
                highestPriorityChild = rightChild;
            }

            if (compare(heap[highestPriorityChild], heap[current]) > 0) {
                swap(current, highestPriorityChild);
                current = highestPriorityChild;
            } else {
                break;
            }
        }
    }

    /**
     * So sánh mức độ ưu tiên giữa 2 khách hàng:
     * 1. Hạng thành viên cao hơn -> Ưu tiên hơn (> 0).
     * 2. Cùng hạng thành viên -> Đến trước (timestamp nhỏ hơn) ưu tiên hơn.
     */
    private int compare(Customer a, Customer b) {
        int prioA = a.getTier().getPriority();
        int prioB = b.getTier().getPriority();
        if (prioA != prioB) {
            return Integer.compare(prioA, prioB);
        }
        // Timestamp đến trước sẽ nhỏ hơn, nên -compare để timestamp nhỏ có độ ưu tiên cao hơn
        return -Long.compare(a.getArrivalTimestamp(), b.getArrivalTimestamp());
    }

    private void swap(int i, int j) {
        Customer temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    private void ensureCapacity() {
        if (size >= heap.length) {
            heap = Arrays.copyOf(heap, heap.length * 2);
        }
    }
}

