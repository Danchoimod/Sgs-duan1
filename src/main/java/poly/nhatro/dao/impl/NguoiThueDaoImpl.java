package poly.nhatro.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import poly.nhatro.dao.CrudDao;
import poly.nhatro.dao.NguoiThueDAO;
import poly.nhatro.entity.NguoiThue;
import poly.nhatro.util.XQuery;
import poly.nhatro.util.XJdbc;

/**
 *
 * @author tranthuyngan
 */
public class NguoiThueDaoImpl implements NguoiThueDAO, CrudDao<NguoiThue, Integer> {
    
    String createSql = "INSERT INTO NGUOI_DUNG(hoVaTen, matKhau, email, sdt, soCCCD, trangThai, gioiTinh, queQuan, ngaySinh, ID_Phong) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String updateSql = "UPDATE NGUOI_DUNG SET hoVaTen = ?, matKhau = ?, email = ?, sdt = ?, soCCCD = ?, trangThai = ?, gioiTinh = ?, queQuan = ?, ngaySinh = ?, ID_Phong = ? WHERE ID_NguoiDung = ?";
    String deleteSql = "DELETE FROM NGUOI_DUNG WHERE ID_NguoiDung = ?";
    String findAllSql = "SELECT * FROM NGUOI_DUNG";
    String findByIdSql = "SELECT * FROM NGUOI_DUNG WHERE ID_NguoiDung = ?";
    
    String getTenPhongSql = "SELECT p.soPhong FROM NGUOI_DUNG nd " +
                           "JOIN PHONG p ON nd.ID_Phong = p.ID_Phong " +
                           "WHERE nd.ID_NguoiDung = ?";
    
    @Override
    public List<NguoiThue> findAll() {
        return XQuery.getBeanList(NguoiThue.class, findAllSql);
    }
    
    public NguoiThue findById(int id) {
        return XQuery.getSingleBean(NguoiThue.class, findByIdSql, id);
    }
    
    @Override
    public NguoiThue findById(Integer id) {
        return findById(id.intValue());
    }
    
    @Override
    public NguoiThue create(NguoiThue entity) {
        XJdbc.executeUpdate(createSql,
                entity.getHoVaTen(),
                entity.getMatKhau(),
                entity.getEmail(),
                entity.getSdt(),
                entity.getSoCCCD(),
                entity.isTrangThai(),
                entity.isGioiTinh(),
                entity.getQueQuan(),
                entity.getNgaySinh(),
                entity.getID_Phong());
        return entity;
    }
    
    @Override
    public void update(NguoiThue entity) {
        XJdbc.executeUpdate(updateSql,
                entity.getHoVaTen(),
                entity.getMatKhau(),
                entity.getEmail(),
                entity.getSdt(),
                entity.getSoCCCD(),
                entity.isTrangThai(),
                entity.isGioiTinh(),
                entity.getQueQuan(),
                entity.getNgaySinh(),
                entity.getID_Phong(),
                entity.getID_NguoiDung());
    }
    
    @Override
    public void deleteById(Integer id) {
        XJdbc.executeUpdate(deleteSql, id);
    }
    
    @Override
    public String getTenPhongByNguoiThueId(int nguoiThueId) {
        try (ResultSet rs = XJdbc.executeQuery(getTenPhongSql, nguoiThueId)) {
            if (rs.next()) {
                return rs.getString("soPhong");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy tên phòng: " + e.getMessage(), e);
        }
        return null;
    }
}
