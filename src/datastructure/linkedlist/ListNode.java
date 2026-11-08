package datastructure.linkedlist;

import model.Product;

/**
 * Lớp đại diện cho một nút (Node) trong Danh sách liên kết đôi InventoryLinkedList.
 */
public class ListNode {
    private Product data;
    private ListNode prev;
    private ListNode next;

    public ListNode(Product data) {
        this.data = data;
        this.prev = null;
        this.next = null;
    }

    public Product getData() {
        return data;
    }

    public void setData(Product data) {
        this.data = data;
    }

    public ListNode getPrev() {
        return prev;
    }

    public void setPrev(ListNode prev) {
        this.prev = prev;
    }

    public ListNode getNext() {
        return next;
    }

    public void setNext(ListNode next) {
        this.next = next;
    }
}

