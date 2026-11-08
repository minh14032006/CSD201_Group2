package datastructure.linkedlist;

import model.Product;
import java.util.ArrayList;
import java.util.List;

/**
 * Cài đặt Danh sách liên kết đôi (Doubly Linked List) cho quản lý kho hàng.
 * Hỗ trợ duyệt tiến/lùi, chèn đầu/đuôi và xóa phần tử linh hoạt.
 */
public class InventoryLinkedList implements IInventoryList {

    private ListNode head;
    private ListNode tail;
    private int size;

    public InventoryLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public void addFirst(Product product) {
        if (product == null) return;
        ListNode newNode = new ListNode(product);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.setNext(head);
            head.setPrev(newNode);
            head = newNode;
        }
        size++;
    }

    @Override
    public void addLast(Product product) {
        if (product == null) return;
        ListNode newNode = new ListNode(product);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = newNode;
        }
        size++;
    }

    @Override
    public boolean removeById(String productId) {
        if (productId == null || isEmpty()) return false;

        ListNode current = head;
        while (current != null) {
            if (current.getData().getProductId().equalsIgnoreCase(productId.trim())) {
                // Xóa node current
                if (current == head && current == tail) {
                    head = tail = null;
                } else if (current == head) {
                    head = head.getNext();
                    head.setPrev(null);
                } else if (current == tail) {
                    tail = tail.getPrev();
                    tail.setNext(null);
                } else {
                    current.getPrev().setNext(current.getNext());
                    current.getNext().setPrev(current.getPrev());
                }
                size--;
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    @Override
    public List<Product> searchByName(String keyword) {
        List<Product> matches = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return matches;
        }
        String lowerKey = keyword.trim().toLowerCase();
        ListNode current = head;
        while (current != null) {
            if (current.getData().getName().toLowerCase().contains(lowerKey)) {
                matches.add(current.getData());
            }
            current = current.getNext();
        }
        return matches;
    }

    @Override
    public List<Product> toForwardList() {
        List<Product> list = new ArrayList<>();
        ListNode current = head;
        while (current != null) {
            list.add(current.getData());
            current = current.getNext();
        }
        return list;
    }

    @Override
    public List<Product> toBackwardList() {
        List<Product> list = new ArrayList<>();
        ListNode current = tail;
        while (current != null) {
            list.add(current.getData());
            current = current.getPrev();
        }
        return list;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }
}

