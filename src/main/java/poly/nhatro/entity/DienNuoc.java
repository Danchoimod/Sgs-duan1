package poly.nhatro.entity;

import lombok.*;
import java.util.Date;

/**
 * Entity class representing electricity and water consumption data
 * @author Generated with Lombok
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DienNuoc {
    private int idDienNuoc;
    private int soDienCu;
    private int soDienMoi;
    private int soNuocCu;
    private int soNuocMoi;
    private Date thangNam;
    private int idPhong;

    // Fields for display/calculation (joined from other tables)
    private String soPhong;
    private String tenChiNhanh;
    private double giaDienChiNhanh;
    private double giaNuocChiNhanh;
    private double giaPhong;

    // Constructor for creating new DienNuoc (idDienNuoc is auto-generated)
    public DienNuoc(int soDienCu, int soDienMoi, int soNuocCu, int soNuocMoi, Date thangNam, int idPhong) {
        this.soDienCu = soDienCu;
        this.soDienMoi = soDienMoi;
        this.soNuocCu = soNuocCu;
        this.soNuocMoi = soNuocMoi;
        this.thangNam = thangNam;
        this.idPhong = idPhong;
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
}
