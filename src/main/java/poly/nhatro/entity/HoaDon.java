package poly.nhatro.entity;

import java.math.BigDecimal;
import java.util.Date;
import lombok.*;

/**
 *
 * @author tranthithuyngan
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class HoaDon {
    private int ID_HoaDon;
    private String trangThai;
    private Date ngayTao;
    private int ID_NguoiDung;
    private int ID_Phong;
    private int ID_HopDong;
    private int ID_ChiNhanh;

    // Các trường được join từ bảng khác
    private String tenNguoiDung;
    private String soPhong;
    private int soDienCu;
    private int soDienMoi;
    private int soNuocCu;
    private int soNuocMoi;
    private int soDien;
    private int soNuoc;
    private double tienDien;
    private double tienNuoc;
    private double tienPhong;
    private double tongTien;
    private String trangThaiThanhToan;
}
