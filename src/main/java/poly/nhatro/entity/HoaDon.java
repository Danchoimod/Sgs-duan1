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
    private int soDienMoi;
    private int soDienCu;
    private int soNuocMoi;
    private int soNuocCu;
    private BigDecimal tienDien;
    private BigDecimal tienNuoc;
    private BigDecimal tienPhong;
    private BigDecimal tongTien;
    private boolean trangThaiThanhToan;
    private Date ngayThanhToan;
    private int ID_HopDong;
    
}
