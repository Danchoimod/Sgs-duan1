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
    List<Object[]> layDanhSachNguoiDungTheoSoPhongVaChiNhanh(String soPhong, int idChiNhanh);
    public void themNguoiO(String tenNguoi, String soPhong, int idChiNhanh);
}
