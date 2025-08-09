package poly.nhatro.dao;

import java.util.List;
import poly.nhatro.entity.NguoiThue;

/**
 *
 * @author tranthuyngan
 */
public interface NguoiThueDAO extends CrudDao<NguoiThue, Integer> {

    // Các method bổ sung nếu cần
    public boolean kiemTraNguoiDungHopLe(String ten, String cccd, String email);

    boolean updatePasswordByEmail(String email, String newPassword);

    List<Object[]> laySoLuongNguoiOTheoChiNhanh(int idChiNhanh);

    public List<Object[]> layDanhSachNguoiDangO(String soPhong, int idChiNhanh);

    public List<Object[]> layDanhSachNguoiChuaO(String soPhong, int idChiNhanh);

    boolean themNguoiOChung(int idNguoiDung, String soPhong, int idChiNhanh);

    boolean xoaNguoiOChung(int idNguoiDung, String soPhong, int idChiNhanh);

    int timIdTheoTen(String tenNguoi);
    
    boolean laNguoiKyHopDong(String tenNguoi, String maPhong);

    /**
     * Kiểm tra người thuê có bất kỳ hợp đồng liên quan nào không (là người ký hoặc người ở chung)
     * @param nguoiThueId ID người thuê (ID_NguoiDung)
     * @return true nếu tồn tại liên kết ở bảng HopDong hoặc NguoiThue_HopDong
     */
    boolean hasAnyContract(int nguoiThueId);
}
