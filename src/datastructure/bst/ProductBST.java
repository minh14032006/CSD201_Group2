package datastructure.bst;

import model.Product;
import java.util.ArrayList;
import java.util.List;

/**
 * Cài đặt Cây tìm kiếm nhị phân (Binary Search Tree - BST) cho sản phẩm.
 * Khóa tìm kiếm và sắp xếp là Product ID (chuỗi ký tự, so sánh không phân biệt hoa thường).
 * Độ phức tạp tìm kiếm/chèn/xóa trung bình: O(log n).
 */
public class ProductBST implements IProductBST {

    private ProductNode root;
    private int size;

    public ProductBST() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public boolean insert(Product product) {
        if (product == null || product.getProductId() == null) {
            return false;
        }
        ProductNode newNode = new ProductNode(product);
        if (root == null) {
            root = newNode;
            size++;
            return true;
        }

        ProductNode current = root;
        ProductNode parent = null;
        int cmp = 0;

        while (current != null) {
            parent = current;
            cmp = product.getProductId().compareToIgnoreCase(current.getData().getProductId());
            if (cmp < 0) {
                current = current.getLeft();
            } else if (cmp > 0) {
                current = current.getRight();
            } else {
                // Trùng ID: không chèn thêm, giữ nguyên
                return false;
            }
        }

        if (cmp < 0) {
            parent.setLeft(newNode);
        } else {
            parent.setRight(newNode);
        }
        size++;
        return true;
    }

    @Override
    public Product search(String productId) {
        if (productId == null || root == null) {
            return null;
        }
        String target = productId.trim();
        ProductNode current = root;
        while (current != null) {
            int cmp = target.compareToIgnoreCase(current.getData().getProductId());
            if (cmp == 0) {
                return current.getData();
            } else if (cmp < 0) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }
        return null;
    }

    @Override
    public boolean delete(String productId) {
        if (productId == null || root == null) {
            return false;
        }
        int initialSize = size;
        root = deleteRec(root, productId.trim());
        return size < initialSize;
    }

    private ProductNode deleteRec(ProductNode current, String productId) {
        if (current == null) {
            return null;
        }

        int cmp = productId.compareToIgnoreCase(current.getData().getProductId());
        if (cmp < 0) {
            current.setLeft(deleteRec(current.getLeft(), productId));
        } else if (cmp > 0) {
            current.setRight(deleteRec(current.getRight(), productId));
        } else {
            // Đã tìm thấy node cần xóa
            size--;

            // Trường hợp 1: Node lá (không có con)
            if (current.getLeft() == null && current.getRight() == null) {
                return null;
            }

            // Trường hợp 2: Node có 1 con
            if (current.getLeft() == null) {
                return current.getRight();
            }
            if (current.getRight() == null) {
                return current.getLeft();
            }

            // Trường hợp 3: Node có đủ 2 con
            // Tìm In-order Successor (phần tử nhỏ nhất bên cây con phải)
            ProductNode successor = findMin(current.getRight());
            current.setData(successor.getData());
            // Xóa node successor khỏi cây con phải
            // Lưu ý: bù lại biến size vì hàm đệ quy sẽ trừ size lần nữa
            size++;
            current.setRight(deleteRec(current.getRight(), successor.getData().getProductId()));
        }
        return current;
    }

    private ProductNode findMin(ProductNode node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    @Override
    public boolean update(String productId, double newPrice, int newStock) {
        Product p = search(productId);
        if (p != null) {
            if (newPrice >= 0) p.setPrice(newPrice);
            if (newStock >= 0) p.setStockQuantity(newStock);
            return true;
        }
        return false;
    }

    @Override
    public List<Product> getInOrderList() {
        List<Product> result = new ArrayList<>();
        inOrderRec(root, result);
        return result;
    }

    private void inOrderRec(ProductNode node, List<Product> list) {
        if (node != null) {
            inOrderRec(node.getLeft(), list);
            list.add(node.getData());
            inOrderRec(node.getRight(), list);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int height() {
        return heightRec(root);
    }

    private int heightRec(ProductNode node) {
        if (node == null) return 0;
        return 1 + Math.max(heightRec(node.getLeft()), heightRec(node.getRight()));
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }
}
