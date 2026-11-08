package datastructure.bst;

import model.Product;

/**
 * Lớp đại diện cho một nút (Node) trong Cây tìm kiếm nhị phân ProductBST.
 */
public class ProductNode {
    private Product data;
    private ProductNode left;
    private ProductNode right;

    public ProductNode(Product data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    public Product getData() {
        return data;
    }

    public void setData(Product data) {
        this.data = data;
    }

    public ProductNode getLeft() {
        return left;
    }

    public void setLeft(ProductNode left) {
        this.left = left;
    }

    public ProductNode getRight() {
        return right;
    }

    public void setRight(ProductNode right) {
        this.right = right;
    }
}

