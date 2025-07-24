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
    private int ID_NguoiDung;  // Phù hợp với database NGUOI_DUNG
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
    
    // Compatibility methods cho code cũ
    public int getID_NguoiThue() {
        return ID_NguoiDung;
    }
    
    public void setID_NguoiThue(int id) {
        this.ID_NguoiDung = id;
    }
    
    public String getHoTen() {
        return hoVaTen;
    }
    
    public void setHoTen(String hoTen) {
        this.hoVaTen = hoTen;
    }
    
    public String getSoDienThoai() {
        return sdt;
    }
    
    public void setSoDienThoai(String sdt) {
        this.sdt = sdt;
    }
}
