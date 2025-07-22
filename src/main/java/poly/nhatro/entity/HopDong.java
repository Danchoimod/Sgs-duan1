package poly.nhatro.entity;

import java.util.Date;
// import lombok.Data; // Nếu đang dùng Lombok, hãy giữ @Data

// @Data // Bỏ comment nếu dùng Lombok
public class HopDong {
    private int ID_HopDong; // Là int, tự động tăng trong DB
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private double soTienCoc;
    private int ID_NguoiDung;
    private int ID_Phong;    // Là int
    private int ID_ChiNhanh; // Là int

    // Constructor (nếu không dùng Lombok, có thể tạo constructor rỗng)
    public HopDong() {}

    // Getters and Setters (nếu không dùng Lombok, hãy tạo thủ công hoặc dùng IDE)
    public int getID_HopDong() {
        return ID_HopDong;
    }

    public void setID_HopDong(int ID_HopDong) {
        this.ID_HopDong = ID_HopDong;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public double getSoTienCoc() {
        return soTienCoc;
    }

    public void setSoTienCoc(double soTienCoc) {
        this.soTienCoc = soTienCoc;
    }

    public int getID_NguoiDung() {
        return ID_NguoiDung;
    }

    public void setID_NguoiDung(int ID_NguoiDung) {
        this.ID_NguoiDung = ID_NguoiDung;
    }

    public int getID_Phong() {
        return ID_Phong;
    }

    public void setID_Phong(int ID_Phong) {
        this.ID_Phong = ID_Phong;
    }

    public int getID_ChiNhanh() {
        return ID_ChiNhanh;
    }

    public void setID_ChiNhanh(int ID_ChiNhanh) {
        this.ID_ChiNhanh = ID_ChiNhanh;
    }
}