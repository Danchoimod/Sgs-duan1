package poly.nhatro.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    
    String createSql = "INSERT INTO NguoiDung(tenNguoiDung, soDienThoai, email, matKhau, namSinh, diaChi, cccdCmnn, anhTruocCccd, anhSauCccd, vaiTro, trangThai) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String updateSql = "UPDATE NguoiDung SET tenNguoiDung = ?, soDienThoai = ?, email = ?, matKhau = ?, namSinh = ?, diaChi = ?, cccdCmnn = ?, anhTruocCccd = ?, anhSauCccd = ?, vaiTro = ?, trangThai = ? WHERE ID_NguoiDung = ?";
    String deleteSql = "DELETE FROM NguoiDung WHERE ID_NguoiDung = ?";
    String findAllSql = "SELECT * FROM NguoiDung";
    String findByIdSql = "SELECT * FROM NguoiDung WHERE ID_NguoiDung = ?";

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
                entity.getTenNguoiDung(),
                entity.getSoDienThoai(),
                entity.getEmail(),
                entity.getMatKhau(),
                entity.getNamSinh(),
                entity.getDiaChi(),
                entity.getCccdCmnn(),
                entity.getAnhTruocCccd(),
                entity.getAnhSauCccd(),
                entity.getVaiTro(),
                entity.getTrangThai()
        );
        return entity;
    }

    @Override
    public void update(NguoiThue entity) {
        XJdbc.executeUpdate(updateSql,
                entity.getTenNguoiDung(),
                entity.getSoDienThoai(),
                entity.getEmail(),
                entity.getMatKhau(),
                entity.getNamSinh(),
                entity.getDiaChi(),
                entity.getCccdCmnn(),
                entity.getAnhTruocCccd(),
                entity.getAnhSauCccd(),
                entity.getVaiTro(),
                entity.getTrangThai(),
                entity.getID_NguoiDung());
    }

    @Override
    public void deleteById(Integer id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public boolean kiemTraNguoiDungHopLe(String ten, String cccd, String email) {
        String sql = "SELECT * FROM NguoiDung WHERE tenNguoiDung = ? AND cccdCmnn = ? AND email = ?";
        try (Connection con = XJdbc.openConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ten);
            ps.setString(2, cccd);
            ps.setString(3, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePasswordByEmail(String email, String newPassword) {
        String sql = "UPDATE NguoiDung SET matKhau = ? WHERE email = ?";
        try (Connection con = XJdbc.openConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setString(2, email);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
