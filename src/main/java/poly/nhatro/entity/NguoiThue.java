package poly.nhatro.entity;

import lombok.*;


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
    private String tenNguoiDung;
    private String matKhau;
    private String email;
    private String soDienThoai; // Match database column
    private String cccdCmnn; // Match database column
    private String anhTruocCccd; // Match database column
    private String anhSauCccd; // Match database column      
    private String trangThai; // Match database column (String, not boolean)
    private String diaChi;
    private String vaiTro; // Match database column (String, not Boolean)
    
}