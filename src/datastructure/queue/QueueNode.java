package datastructure.queue;

import model.Customer;

/**
 * Lớp đại diện cho một nút (Node) trong Hàng đợi CheckoutQueueFIFO.
 */
public class QueueNode {
    private Customer data;
    private QueueNode next;

    public QueueNode(Customer data) {
        this.data = data;
        this.next = null;
    }

    public Customer getData() {
        return data;
    }

    public void setData(Customer data) {
        this.data = data;
    }

    public QueueNode getNext() {
        return next;
    }

    public void setNext(QueueNode next) {
        this.next = next;
    }
}

