
package poly.nhatro.dao;


import poly.nhatro.entity.NguoiThue;

/**
 *
 * @author tranthuyngan
 */
public interface NguoiThueDAO extends CrudDao<NguoiThue, Integer> {
    // Các method bổ sung nếu cần
    public boolean kiemTraNguoiDungHopLe(String ten, String cccd, String email);
    boolean updatePasswordByEmail(String email, String newPassword);
}
