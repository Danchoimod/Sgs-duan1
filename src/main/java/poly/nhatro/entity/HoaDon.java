package poly.nhatro.entity;

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
   
    
}
