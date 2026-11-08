package util;

import java.util.Scanner;

/**
 * Lớp tiện ích hỗ trợ nhập liệu an toàn từ bàn phím, tự động bẫy lỗi và kiểm tra hợp lệ.
 */
public class Inputter {

    private static final Scanner sc = new Scanner(System.in);

    /**
     * Nhập chuỗi ký tự không rỗng và kiểm tra theo regex.
     */
    public static String getString(String prompt, String regex, String errorMsg) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("[!] Dữ liệu không được để trống. Vui lòng nhập lại.");
                continue;
            }
            if (Acceptable.isValid(input, regex)) {
                return input;
            } else {
                System.out.println("[!] " + errorMsg);
            }
        }
    }

    /**
     * Nhập chuỗi ký tự thông thường (chỉ cần không rỗng).
     */
    public static String getNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("[!] Dữ liệu không được để trống. Vui lòng nhập lại.");
        }
    }

    /**
     * Nhập số nguyên trong khoảng [min, max].
     */
    public static int getInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("[!] Giá trị phải nằm trong khoảng [%d, %d]. Vui lòng nhập lại.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("[!] Vui lòng nhập một số nguyên hợp lệ.");
            }
        }
    }

    /**
     * Nhập số thực dương (giá tiền) tối thiểu là min.
     */
    public static double getDouble(String prompt, double min) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value >= min) {
                    return value;
                }
                System.out.printf(java.util.Locale.US, "[!] Giá trị tối thiểu phải là $%.2f. Vui lòng nhập lại.%n", min);
            } catch (NumberFormatException e) {
                System.out.println("[!] Vui lòng nhập một số thực hợp lệ.");
            }
        }
    }
}

