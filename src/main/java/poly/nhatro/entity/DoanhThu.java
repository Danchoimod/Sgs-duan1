package poly.nhatro.entity;

import java.math.BigDecimal;
import java.sql.Date;

public class DoanhThu {
    private int idHoaDon;
    private int soDienMoi;
    private int soNuocMoi;
    private int soDienCu;
    private int soNuocCu;
    private BigDecimal tienDien;
    private BigDecimal tienNuoc;
    private BigDecimal tienPhong;
    private BigDecimal tongTien;
    private boolean trangThai;
    private Date ngayThanhToan;
    private int idHopDong;

    // Getter và Setter
    public int getIdHoaDon() {
        return idHoaDon;
    }

    public void setIdHoaDon(int idHoaDon) {
        this.idHoaDon = idHoaDon;
    }

    public int getSoDienMoi() {
        return soDienMoi;
    }

    public void setSoDienMoi(int soDienMoi) {
        this.soDienMoi = soDienMoi;
    }

    public int getSoNuocMoi() {
        return soNuocMoi;
    }

    public void setSoNuocMoi(int soNuocMoi) {
        this.soNuocMoi = soNuocMoi;
    }

    public int getSoDienCu() {
        return soDienCu;
    }

    public void setSoDienCu(int soDienCu) {
        this.soDienCu = soDienCu;
    }

    public int getSoNuocCu() {
        return soNuocCu;
    }

    public void setSoNuocCu(int soNuocCu) {
        this.soNuocCu = soNuocCu;
    }

    public BigDecimal getTienDien() {
        return tienDien;
    }

    public void setTienDien(BigDecimal tienDien) {
        this.tienDien = tienDien;
    }

    public BigDecimal getTienNuoc() {
        return tienNuoc;
    }

    public void setTienNuoc(BigDecimal tienNuoc) {
        this.tienNuoc = tienNuoc;
    }

    public BigDecimal getTienPhong() {
        return tienPhong;
    }

    public void setTienPhong(BigDecimal tienPhong) {
        this.tienPhong = tienPhong;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public Date getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(Date ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }

    public int getIdHopDong() {
        return idHopDong;
    }

    public void setIdHopDong(int idHopDong) {
        this.idHopDong = idHopDong;
    }
}
