package util;

/**
 * Lớp chứa các hằng số biểu thức chính quy (Regex) và phương thức kiểm tra hợp lệ dữ liệu.
 */
public class Acceptable {

    /** Định dạng mã sản phẩm: PR theo sau bởi ít nhất 3 chữ số (ví dụ: PR001, PR105) */
    public static final String PRODUCT_ID_VALID = "^PR[0-9]{3,}$";

    /** Định dạng mã khách hàng: KH theo sau bởi ít nhất 3 chữ số (ví dụ: KH001, KH010) */
    public static final String CUSTOMER_ID_VALID = "^KH[0-9]{3,}$";

    /** Định dạng số điện thoại Việt Nam (10 chữ số bắt đầu bằng số 0) */
    public static final String PHONE_VALID = "^0[3|5|7|8|9][0-9]{8}$";

    /** Định dạng số nguyên dương */
    public static final String POSITIVE_INT_VALID = "^[1-9][0-9]*$";

    /** Định dạng số thực dương (tiền tệ) */
    public static final String POSITIVE_DOUBLE_VALID = "^[0-9]+(\\.[0-9]{1,2})?$";

    /**
     * Kiểm tra chuỗi dữ liệu có khớp với biểu thức chính quy hay không.
     * @param data Chuỗi cần kiểm tra
     * @param regex Biểu thức chính quy
     * @return true nếu hợp lệ, ngược lại false
     */
    public static boolean isValid(String data, String regex) {
        if (data == null || regex == null) {
            return false;
        }
        return data.matches(regex);
    }
}

