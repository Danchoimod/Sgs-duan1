package poly.nhatro.entity;

import java.util.Date; // For DATETIME in SQL

public class HopDong {
    private int ID_HopDong;
    private Date ngayTao; // Corresponds to ngayTao DATETIME NOT NULL
    private int thoiHan; // Corresponds to thoiHan INT NOT NULL (likely in months)
    private int tienCoc; // Corresponds to tienCoc INT NOT NULL
    private int nuocBanDau; // Corresponds to nuocBanDau INT NOT NULL
    private int dienBanDau; // Corresponds to dienBanDau INT NOT NULL
    private int ID_NguoiDung; // Corresponds to ID_NguoiDung INT NOT NULL
    private int ID_Phong; // Corresponds to ID_Phong INT NOT NULL

    // Constructors

    public HopDong() {
    }

    public HopDong(Date ngayTao, int thoiHan, int tienCoc, int nuocBanDau, int dienBanDau, int ID_NguoiDung, int ID_Phong) {
        this.ngayTao = ngayTao;
        this.thoiHan = thoiHan;
        this.tienCoc = tienCoc;
        this.nuocBanDau = nuocBanDau;
        this.dienBanDau = dienBanDau;
        this.ID_NguoiDung = ID_NguoiDung;
        this.ID_Phong = ID_Phong;
    }

    public HopDong(int ID_HopDong, Date ngayTao, int thoiHan, int tienCoc, int nuocBanDau, int dienBanDau, int ID_NguoiDung, int ID_Phong) {
        this.ID_HopDong = ID_HopDong;
        this.ngayTao = ngayTao;
        this.thoiHan = thoiHan;
        this.tienCoc = tienCoc;
        this.nuocBanDau = nuocBanDau;
        this.dienBanDau = dienBanDau;
        this.ID_NguoiDung = ID_NguoiDung;
        this.ID_Phong = ID_Phong;
    }

    // Getters and Setters

    public int getID_HopDong() {
        return ID_HopDong;
    }

    public void setID_HopDong(int ID_HopDong) {
        this.ID_HopDong = ID_HopDong;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public int getThoiHan() {
        return thoiHan;
    }

    public void setThoiHan(int thoiHan) {
        this.thoiHan = thoiHan;
    }

    public int getTienCoc() {
        return tienCoc;
    }

    public void setTienCoc(int tienCoc) {
        this.tienCoc = tienCoc;
    }

    public int getNuocBanDau() {
        return nuocBanDau;
    }

    public void setNuocBanDau(int nuocBanDau) {
        this.nuocBanDau = nuocBanDau;
    }

    public int getDienBanDau() {
        return dienBanDau;
    }

    public void setDienBanDau(int dienBanDau) {
        this.dienBanDau = dienBanDau;
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

    // Optional: Override toString() for easy debugging
    @Override
    public String toString() {
        return "HopDong{" +
               "ID_HopDong=" + ID_HopDong +
               ", ngayTao=" + ngayTao +
               ", thoiHan=" + thoiHan +
               ", tienCoc=" + tienCoc +
               ", nuocBanDau=" + nuocBanDau +
               ", dienBanDau=" + dienBanDau +
               ", ID_NguoiDung=" + ID_NguoiDung +
               ", ID_Phong=" + ID_Phong +
               '}';
    }
}