package poly.nhatro.entity;

import java.util.Date; // Import for Date type

public class DienNuoc {
    private int idDienNuoc;
    private int soDienCu;
    private int soDienMoi;
    private int soNuocCu;
    private int soNuocMoi;
    private Date thangNam; // New field for month and year
    private int idPhong;

    // Fields for display/calculation (not directly from DienNuoc table)
    private String soPhong;
    private String tenChiNhanh;
    private double giaDienChiNhanh; // To store giaDien from ChiNhanh for calculation
    private double giaNuocChiNhanh; // To store giaNuoc from ChiNhanh for calculation
    private double giaPhong;

    public DienNuoc() {
    }

    // Constructor for creating new DienNuoc (ID_DienNuoc is auto-generated)
    public DienNuoc(int soDienCu, int soDienMoi, int soNuocCu, int soNuocMoi, Date thangNam, int idPhong) {
        this.soDienCu = soDienCu;
        this.soDienMoi = soDienMoi;
        this.soNuocCu = soNuocCu;
        this.soNuocMoi = soNuocMoi;
        this.thangNam = thangNam;
        this.idPhong = idPhong;
    }

    // Full constructor (including ID and joined fields for display/calculation)
    public DienNuoc(int idDienNuoc, int soDienCu, int soDienMoi, int soNuocCu, int soNuocMoi, Date thangNam, int idPhong, String soPhong, String tenChiNhanh, double giaDienChiNhanh, double giaNuocChiNhanh, double giaPhong) {
        this.idDienNuoc = idDienNuoc;
        this.soDienCu = soDienCu;
        this.soDienMoi = soDienMoi;
        this.soNuocCu = soNuocCu;
        this.soNuocMoi = soNuocMoi;
        this.thangNam = thangNam;
        this.idPhong = idPhong;
        this.soPhong = soPhong;
        this.tenChiNhanh = tenChiNhanh;
        this.giaDienChiNhanh = giaDienChiNhanh;
        this.giaNuocChiNhanh = giaNuocChiNhanh;
        this.giaPhong = giaPhong;
    }

    // Getters and Setters
    public int getIdDienNuoc() {
        return idDienNuoc;
    }

    public void setIdDienNuoc(int idDienNuoc) {
        this.idDienNuoc = idDienNuoc;
    }

    public int getSoDienCu() {
        return soDienCu;
    }

    public void setSoDienCu(int soDienCu) {
        this.soDienCu = soDienCu;
    }

    public int getSoDienMoi() {
        return soDienMoi;
    }

    public void setSoDienMoi(int soDienMoi) {
        this.soDienMoi = soDienMoi;
    }

    public int getSoNuocCu() {
        return soNuocCu;
    }

    public void setSoNuocCu(int soNuocCu) {
        this.soNuocCu = soNuocCu;
    }

    public int getSoNuocMoi() {
        return soNuocMoi;
    }

    public void setSoNuocMoi(int soNuocMoi) {
        this.soNuocMoi = soNuocMoi;
    }

    public Date getThangNam() {
        return thangNam;
    }

    public void setThangNam(Date thangNam) {
        this.thangNam = thangNam;
    }

    public int getIdPhong() {
        return idPhong;
    }

    public void setIdPhong(int idPhong) {
        this.idPhong = idPhong;
    }

    public String getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(String soPhong) {
        this.soPhong = soPhong;
    }

    public String getTenChiNhanh() {
        return tenChiNhanh;
    }

    public void setTenChiNhanh(String tenChiNhanh) {
        this.tenChiNhanh = tenChiNhanh;
    }

    public double getGiaDienChiNhanh() {
        return giaDienChiNhanh;
    }

    public void setGiaDienChiNhanh(double giaDienChiNhanh) {
        this.giaDienChiNhanh = giaDienChiNhanh;
    }

    public double getGiaNuocChiNhanh() {
        return giaNuocChiNhanh;
    }

    public void setGiaNuocChiNhanh(double giaNuocChiNhanh) {
        this.giaNuocChiNhanh = giaNuocChiNhanh;
    }
    
    public double getGiaPhong() {
        return giaPhong;
    }

    public void setGiaPhong(double giaPhong) {
        this.giaPhong = giaPhong;
    }

    // Calculated fields
    public double getThanhTienDien() {
        return (soDienMoi - soDienCu) * giaDienChiNhanh;
    }

    public double getThanhTienNuoc() {
        return (soNuocMoi - soNuocCu) * giaNuocChiNhanh;
    }

    public double getTongCong() {
        return getThanhTienDien() + getThanhTienNuoc() + giaPhong;
    }

    @Override
    public String toString() {
        return "DienNuoc{" +
                "idDienNuoc=" + idDienNuoc +
                ", soDienCu=" + soDienCu +
                ", soDienMoi=" + soDienMoi +
                ", soNuocCu=" + soNuocCu +
                ", soNuocMoi=" + soNuocMoi +
                ", thangNam=" + thangNam +
                ", idPhong=" + idPhong +
                ", soPhong='" + soPhong + '\'' +
                ", tenChiNhanh='" + tenChiNhanh + '\'' +
                ", giaDienChiNhanh=" + giaDienChiNhanh +
                ", giaNuocChiNhanh=" + giaNuocChiNhanh +
                ", giaPhong=" + giaPhong +
                '}';
    }
}
