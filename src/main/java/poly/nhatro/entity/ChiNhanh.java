package poly.nhatro.entity;

public class ChiNhanh {
    private Integer id_ChiNhanh;
    private String tenChiNhanh;
    private String diaChi;
    private Double giaDien;
    private Double giaNuoc;

    public ChiNhanh() {
    }

    public ChiNhanh(String tenChiNhanh, String diaChi, Double giaDien, Double giaNuoc) {
        this.tenChiNhanh = tenChiNhanh;
        this.diaChi = diaChi;
        this.giaDien = giaDien;
        this.giaNuoc = giaNuoc;
    }

    // Getters and Setters
    public Integer getId_ChiNhanh() {
        return id_ChiNhanh;
    }

    public void setId_ChiNhanh(Integer id_ChiNhanh) {
        this.id_ChiNhanh = id_ChiNhanh;
    }

    public String getTenChiNhanh() {
        return tenChiNhanh;
    }

    public void setTenChiNhanh(String tenChiNhanh) {
        this.tenChiNhanh = tenChiNhanh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public Double getGiaDien() {
        return giaDien;
    }

    public void setGiaDien(Double giaDien) {
        this.giaDien = giaDien;
    }

    public Double getGiaNuoc() {
        return giaNuoc;
    }

    public void setGiaNuoc(Double giaNuoc) {
        this.giaNuoc = giaNuoc;
    }
}