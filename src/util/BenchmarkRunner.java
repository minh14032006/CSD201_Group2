package util;

import datastructure.bst.ProductBST;
import datastructure.heap.VIPCustomerHeap;
import datastructure.linkedlist.InventoryLinkedList;
import datastructure.queue.CheckoutQueueFIFO;
import model.Customer;
import model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Tiện ích đo đạc hiệu năng thực nghiệm (Performance Benchmark Runner)
 * phục vụ cho việc phân tích độ phức tạp thuật toán và lập bảng biểu báo cáo CSD201.
 * 
 * 1. Đo lường tốc độ tìm kiếm: Binary Search Tree O(log n) vs Linear Search Linked List O(n).
 * 2. Đo lường thông lượng hàng đợi: Queue FIFO O(1) vs Binary Max-Heap O(log n).
 */
public class BenchmarkRunner {

    private static final Random random = new Random(42); // Seed cố định để kết quả có tính tái lập

    public static void main(String[] args) {
        runAllBenchmarks();
    }

    public static void runAllBenchmarks() {
        System.out.println("\n==========================================================================================");
        System.out.println("                 SMART STORE MANAGER - PERFORMANCE BENCHMARK EXPERIMENT                   ");
        System.out.println("==========================================================================================\n");

        int[] inputSizes = {100, 500, 1000, 5000, 10000, 30000};

        // --- BÀI ĐO 1: TỐC ĐỘ TÌM KIẾM SẢN PHẨM (BST vs LINKED LIST) ---
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println(" [THỰC NGHIỆM 1] SO SÁNH TỐC ĐỘ TÌM KIẾM: BST O(log n) vs LINKED LIST O(n)");
        System.out.println(" (Thực hiện 1,000 lượt tìm kiếm ngẫu nhiên trên tập dữ liệu kích thước N)");
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.printf("%-10s | %-18s | %-20s | %-20s%n", "Kích thước (N)", "BST Time (ms)", "Linked List Time (ms)", "Tốc độ cải thiện");
        System.out.println("------------------------------------------------------------------------------------------");

        for (int n : inputSizes) {
            List<Product> dataset = generateMockProducts(n);

            ProductBST bst = new ProductBST();
            InventoryLinkedList list = new InventoryLinkedList();

            for (Product p : dataset) {
                bst.insert(p);
                list.addLast(p);
            }

            // Chọn 1,000 ID ngẫu nhiên để tìm kiếm
            List<String> searchIds = new ArrayList<>(1000);
            for (int i = 0; i < 1000; i++) {
                int targetIdx = random.nextInt(n);
                searchIds.add(dataset.get(targetIdx).getProductId());
            }

            // Đo BST
            long startBST = System.nanoTime();
            for (String id : searchIds) {
                bst.search(id);
            }
            long endBST = System.nanoTime();
            double bstTimeMs = (endBST - startBST) / 1e6;

            // Đo Linked List
            long startLL = System.nanoTime();
            for (String id : searchIds) {
                // Tuyến tính qua Linked List
                for (Product p : list.toForwardList()) {
                    if (p.getProductId().equalsIgnoreCase(id)) {
                        break;
                    }
                }
            }
            long endLL = System.nanoTime();
            double llTimeMs = (endLL - startLL) / 1e6;

            double speedup = (bstTimeMs > 0) ? (llTimeMs / bstTimeMs) : 1.0;
            System.out.printf("%-10d | %-18.4f | %-20.4f | Nhanh hơn %.1fx%n",
                    n, bstTimeMs, llTimeMs, speedup);
        }

        // --- BÀI ĐO 2: THÔNG LƯỢNG HÀNG ĐỢI (QUEUE FIFO vs MAX-HEAP VIP) ---
        System.out.println("\n------------------------------------------------------------------------------------------");
        System.out.println(" [THỰC NGHIỆM 2] SO SÁNH THÔNG LƯỢNG HÀNG ĐỢI: FIFO QUEUE vs BINARY MAX-HEAP");
        System.out.println(" (Thực hiện Enqueue N phần tử và Dequeue toàn bộ N phần tử)");
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.printf("%-10s | %-25s | %-25s%n", "Kích thước (N)", "Queue FIFO (Enqueue+Dequeue)", "Max-Heap (Enqueue+Dequeue)");
        System.out.printf("%-10s | %-12s %-12s | %-12s %-12s%n", "", "Enq (ms)", "Deq (ms)", "Enq (ms)", "Deq (ms)");
        System.out.println("------------------------------------------------------------------------------------------");

        for (int n : inputSizes) {
            List<Customer> customers = generateMockCustomers(n);

            // 1. Đo FIFO Queue
            CheckoutQueueFIFO fifo = new CheckoutQueueFIFO();
            long s1 = System.nanoTime();
            for (Customer c : customers) fifo.enqueue(c);
            long s2 = System.nanoTime();
            while (!fifo.isEmpty()) fifo.dequeue();
            long s3 = System.nanoTime();

            double fifoEnqMs = (s2 - s1) / 1e6;
            double fifoDeqMs = (s3 - s2) / 1e6;

            // 2. Đo Binary Max-Heap
            VIPCustomerHeap heap = new VIPCustomerHeap();
            long h1 = System.nanoTime();
            for (Customer c : customers) heap.enqueue(c);
            long h2 = System.nanoTime();
            while (!heap.isEmpty()) heap.dequeue();
            long h3 = System.nanoTime();

            double heapEnqMs = (h2 - h1) / 1e6;
            double heapDeqMs = (h3 - h2) / 1e6;

            System.out.printf("%-10d | %-12.4f %-12.4f | %-12.4f %-12.4f%n",
                    n, fifoEnqMs, fifoDeqMs, heapEnqMs, heapDeqMs);
        }

        System.out.println("==========================================================================================");
        System.out.println("Thực nghiệm đo đạc hoàn tất! Kết quả cho thấy BST vượt trội gấp nhiều lần so với duyệt tuyến tính.");
        System.out.println("==========================================================================================\n");
    }

    private static List<Product> generateMockProducts(int count) {
        List<Product> list = new ArrayList<>(count);
        String[] categories = {"Điện tử", "Phụ kiện", "Gia dụng", "Đồ ăn", "Thời trang"};
        // Tạo các chỉ số ngẫu nhiên để cây BST không bị suy biến thành đường thẳng
        int[] ids = new int[count];
        for (int i = 0; i < count; i++) ids[i] = i + 1;
        // Fisher-Yates shuffle
        for (int i = count - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = ids[i];
            ids[i] = ids[j];
            ids[j] = temp;
        }

        for (int i = 0; i < count; i++) {
            String id = String.format("PR%05d", ids[i]);
            String name = "Item Mock " + ids[i];
            String cat = categories[i % categories.length];
            double price = 5.0 + random.nextDouble() * 200.0;
            int stock = 10 + random.nextInt(100);
            list.add(new Product(id, name, cat, price, stock));
        }
        return list;
    }

    private static List<Customer> generateMockCustomers(int count) {
        List<Customer> list = new ArrayList<>(count);
        Customer.MembershipTier[] tiers = Customer.MembershipTier.values();
        for (int i = 1; i <= count; i++) {
            String id = String.format("KH%05d", i);
            String name = "Customer Mock " + i;
            String phone = "098" + String.format("%07d", i % 10000000);
            Customer.MembershipTier tier = tiers[random.nextInt(tiers.length)];
            list.add(new Customer(id, name, phone, tier));
        }
        return list;
    }
}
