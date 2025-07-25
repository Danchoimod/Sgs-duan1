
package poly.nhatro.entity;

import java.math.BigDecimal;

/**
 *
 * @author Admin
 */
public class Phong {
    private int idPhong;
    private BigDecimal giaPhong;
    private boolean trangThai; // true: đã cho thuê, false: còn trống
    private String soPhong;
    private String moTa;
    private String hinhAnh;
    private int idChiNhanh;

    // Constructors
    public Phong() {
    }

    public Phong(BigDecimal giaPhong, boolean trangThai, String soPhong, String moTa, String hinhAnh, int idChiNhanh) {
        this.giaPhong = giaPhong;
        this.trangThai = trangThai;
        this.soPhong = soPhong;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
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

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
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
                ", hinhAnh='" + hinhAnh + '\'' +
                ", idChiNhanh=" + idChiNhanh +
                '}';
    }
}

