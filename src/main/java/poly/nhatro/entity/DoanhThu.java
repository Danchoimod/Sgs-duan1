package poly.nhatro.entity;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class DoanhThu {
    private int idHoaDon;
    private String tenPhong;
    private String tenNguoiDung;
    private String tenChiNhanh;
    private int soDienCu;
    private int soDienMoi;
    private int soNuocCu;
    private int soNuocMoi;
    private int soDienDaDung;
    private int soNuocDaDung;
    private BigDecimal tienDien;
    private BigDecimal tienNuoc;
    private BigDecimal tienPhong;
    private BigDecimal tongTien;
    private String trangThaiThanhToan;
    private Date ngayThanhToan;
    private Date thangNamDienNuoc;
    private int giaDien;
    private int giaNuoc;
}
