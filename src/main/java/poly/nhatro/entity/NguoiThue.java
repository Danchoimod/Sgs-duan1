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
    private int ID_NguoiDung; // ID tự tăng trong DB, không cần set từ code
    private String hoVaTen;
    private String matKhau;
    private String email;
    private String sdt;
    private String soCCCD;
    private boolean trangThai; // BIT hoặc BOOLEAN trong SQL
    private boolean gioiTinh; // BIT hoặc BOOLEAN trong SQL (ví dụ: true là nam, false là nữ)
    private String queQuan;
    private Date ngaySinh; // DATE trong SQL
    private Integer ID_Phong; // Khóa ngoại, có thể null nếu người thuê chưa được gán phòng
}