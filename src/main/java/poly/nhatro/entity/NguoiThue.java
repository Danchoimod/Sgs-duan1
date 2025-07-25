package poly.nhatro.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.Date;

/**
 *
 * @author Phu Pham
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NguoiThue {
    private int ID_NguoiDung;  
    private String hoVaTen;
    private String matKhau;
    private String email;
    private String sdt;
    private String soCCCD;
    private boolean trangThai;
    private boolean gioiTinh;
    private String queQuan;
    private Date ngaySinh;
    private Integer ID_Phong;
    
}
