package poly.nhatro.entity;

public class DienNuoc {
    private int idDienNuoc;
    private int giaDien;
    private int giaNuoc;
    private int idPhong;
    private String soPhong;
    private String tenChiNhanh;

    public DienNuoc() {
    }

    public DienNuoc(int idDienNuoc, int giaDien, int giaNuoc, int idPhong) {
        this.idDienNuoc = idDienNuoc;
        this.giaDien = giaDien;
        this.giaNuoc = giaNuoc;
        this.idPhong = idPhong;
    }

    public DienNuoc(int giaDien, int giaNuoc, int idPhong) {
        this.giaDien = giaDien;
        this.giaNuoc = giaNuoc;
        this.idPhong = idPhong;
    }

    public DienNuoc(int idDienNuoc, int giaDien, int giaNuoc, int idPhong, String soPhong, String tenChiNhanh) {
        this.idDienNuoc = idDienNuoc;
        this.giaDien = giaDien;
        this.giaNuoc = giaNuoc;
        this.idPhong = idPhong;
        this.soPhong = soPhong;
        this.tenChiNhanh = tenChiNhanh;
    }

    public int getIdDienNuoc() {
        return idDienNuoc;
    }

    public void setIdDienNuoc(int idDienNuoc) {
        this.idDienNuoc = idDienNuoc;
    }

    public int getGiaDien() {
        return giaDien;
    }

    public void setGiaDien(int giaDien) {
        this.giaDien = giaDien;
    }

    public int getGiaNuoc() {
        return giaNuoc;
    }

    public void setGiaNuoc(int giaNuoc) {
        this.giaNuoc = giaNuoc;
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

    @Override
    public String toString() {
        return "DienNuoc{" +
                "idDienNuoc=" + idDienNuoc +
                ", giaDien=" + giaDien +
                ", giaNuoc=" + giaNuoc +
                ", idPhong=" + idPhong +
                ", soPhong='" + soPhong + '\'' +
                ", tenChiNhanh='" + tenChiNhanh + '\'' +
                '}';
    }
}