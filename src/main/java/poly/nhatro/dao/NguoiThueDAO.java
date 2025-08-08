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
     * Kiểm tra người dùng có bất kỳ hợp đồng nào hay không (làm chủ hợp đồng hoặc là người ở chung)
     */
    boolean hasAnyContract(int idNguoiDung);

    /**
     * Kiểm tra người dùng có hợp đồng đang hoạt động (trangThai = 0) hay không
     */
    boolean kiemTraHopDongDangHoatDong(int idNguoiDung);

    /**
     * Kiểm tra phòng có hợp đồng còn hạn hay không
     */
    boolean kiemTraPhongCoHopDongConHan(String soPhong, int idChiNhanh);
}
