
package poly.nhatro.entity;

import java.math.BigDecimal;

/**
 *
 * @author Admin
 */
public class Phong {
    private int idPhong;
    private BigDecimal giaPhong;
    private String trangThai; // "Đang thuê" or "Trống"
    private String soPhong;
    private String moTa;
    private String anhPhong;
    private int idChiNhanh;

    // Constructors
    public Phong() {
    }

    public Phong(BigDecimal giaPhong, String trangThai, String soPhong, String moTa, String anhPhong, int idChiNhanh) {
        this.giaPhong = giaPhong;
        this.trangThai = trangThai;
        this.soPhong = soPhong;
        this.moTa = moTa;
        this.anhPhong = anhPhong;
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

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
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

    public String getAnhPhong() {
        return anhPhong;
    }

    public void setAnhPhong(String anhPhong) {
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
                ", anhPhong='" + anhPhong + '\'' +
                ", idChiNhanh=" + idChiNhanh +
                '}';
    }
}

