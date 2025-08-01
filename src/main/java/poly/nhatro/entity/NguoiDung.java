package poly.nhatro.entity;

import java.util.Date;

public class NguoiDung {
    private int ID_NguoiDung;
    private String tenNguoiDung;
    private String soDienThoai;
    private String email;
    private String matKhau;
    private Date namSinh;
    private String diaChi;
    private String cccdCmnn;
    private String anhTruocCccd;
    private String anhSauCccd;
    private String vaiTro;
    private String trangThai;

    // Default constructor
    public NguoiDung() {}

    // Constructor with all fields
    public NguoiDung(int ID_NguoiDung, String tenNguoiDung, String soDienThoai, String email, 
                     String matKhau, Date namSinh, String diaChi, String cccdCmnn, 
                     String anhTruocCccd, String anhSauCccd, String vaiTro, String trangThai) {
        this.ID_NguoiDung = ID_NguoiDung;
        this.tenNguoiDung = tenNguoiDung;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.matKhau = matKhau;
        this.namSinh = namSinh;
        this.diaChi = diaChi;
        this.cccdCmnn = cccdCmnn;
        this.anhTruocCccd = anhTruocCccd;
        this.anhSauCccd = anhSauCccd;
        this.vaiTro = vaiTro;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public int getID_NguoiDung() {
        return ID_NguoiDung;
    }

    public void setID_NguoiDung(int ID_NguoiDung) {
        this.ID_NguoiDung = ID_NguoiDung;
    }

    public String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public Date getNamSinh() {
        return namSinh;
    }

    public void setNamSinh(Date namSinh) {
        this.namSinh = namSinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getCccdCmnn() {
        return cccdCmnn;
    }

    public void setCccdCmnn(String cccdCmnn) {
        this.cccdCmnn = cccdCmnn;
    }

    public String getAnhTruocCccd() {
        return anhTruocCccd;
    }

    public void setAnhTruocCccd(String anhTruocCccd) {
        this.anhTruocCccd = anhTruocCccd;
    }

    public String getAnhSauCccd() {
        return anhSauCccd;
    }

    public void setAnhSauCccd(String anhSauCccd) {
        this.anhSauCccd = anhSauCccd;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return ID_NguoiDung + " (" + tenNguoiDung + ")";
    }
}
