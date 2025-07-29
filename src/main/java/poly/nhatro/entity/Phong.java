package poly.nhatro.entity;

import java.math.BigDecimal;

/**
 *
 * @author Admin (Đã điều chỉnh bởi Gemini)
 */
public class Phong {
    private int idPhong; // ID tự tăng trong DB, không cần set từ code
    private BigDecimal giaPhong; // DECIMAL hoặc NUMERIC trong SQL
    private boolean trangThai; // BIT hoặc BOOLEAN trong SQL
    private String soPhong;
    private String moTa;
    private String anhPhong; // Đã đổi tên từ hinhAnh -> anhPhong để khớp với tên cột trong DB
    private int idChiNhanh; // Khóa ngoại, nên là kiểu dữ liệu phù hợp với khóa chính của ChiNhanh

    // Constructors
    public Phong() {
    }

    // Constructor khi thêm mới (không có idPhong vì là tự tăng)
    public Phong(BigDecimal giaPhong, boolean trangThai, String soPhong, String moTa, String anhPhong, int idChiNhanh) {
        this.giaPhong = giaPhong;
        this.trangThai = trangThai;
        this.soPhong = soPhong;
        this.moTa = moTa;
        this.anhPhong = anhPhong; // Đã sửa hinhAnh -> anhPhong
        this.idChiNhanh = idChiNhanh;
    }

    // Constructor đầy đủ (khi đọc từ DB lên)
    public Phong(int idPhong, BigDecimal giaPhong, boolean trangThai, String soPhong, String moTa, String anhPhong, int idChiNhanh) {
        this.idPhong = idPhong;
        this.giaPhong = giaPhong;
        this.trangThai = trangThai;
        this.soPhong = soPhong;
        this.moTa = moTa;
        this.anhPhong = anhPhong; // Đã sửa hinhAnh -> anhPhong
        this.idChiNhanh = idChiNhanh;
    }

    // Getters and Setters
    public int getIdPhong() {
        return idPhong;
    }

    public void setIdPhong(int idPhong) {
        this.idPhong = idPhong;
    }

    public BigDecimal getGiaPhong() {
        return giaPhong;
    }

    public void setGiaPhong(BigDecimal giaPhong) {
        this.giaPhong = giaPhong;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public String getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(String soPhong) {
        this.soPhong = soPhong;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getAnhPhong() { // Đã đổi tên getter từ getHinhAnh() -> getAnhPhong()
        return anhPhong;
    }

    public void setAnhPhong(String anhPhong) { // Đã đổi tên setter từ setHinhAnh() -> setAnhPhong()
        this.anhPhong = anhPhong;
    }

    public int getIdChiNhanh() {
        return idChiNhanh;
    }

    public void setIdChiNhanh(int idChiNhanh) {
        this.idChiNhanh = idChiNhanh;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "Phong{" +
                "idPhong=" + idPhong +
                ", giaPhong=" + giaPhong +
                ", trangThai=" + trangThai +
                ", soPhong='" + soPhong + '\'' +
                ", moTa='" + moTa + '\'' +
                ", anhPhong='" + anhPhong + '\'' + // Đã sửa hinhAnh -> anhPhong
                ", idChiNhanh=" + idChiNhanh +
                '}';
    }
}
