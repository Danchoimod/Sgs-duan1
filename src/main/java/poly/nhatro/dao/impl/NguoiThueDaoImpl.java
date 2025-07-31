package poly.nhatro.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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

    String createSql = "INSERT INTO NguoiDung(tenNguoiDung, soDienThoai, email, matKhau, diaChi, cccdCmnn, anhTruocCccd, anhSauCccd, vaiTro, trangThai) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String updateSql = "UPDATE NguoiDung SET tenNguoiDung = ?, soDienThoai = ?, email = ?, matKhau = ?, diaChi = ?, cccdCmnn = ?, anhTruocCccd = ?, anhSauCccd = ?, vaiTro = ?, trangThai = ? WHERE ID_NguoiDung = ?";
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

    @Override
    public List<Object[]> laySoLuongNguoiOTheoChiNhanh(int idChiNhanh) {
        List<Object[]> list = new ArrayList<>();
        String sql = """
        SELECT P.soPhong, COUNT(NTHD.ID_NguoiDung)
        FROM Phong P
        JOIN HopDong HD ON P.ID_Phong = HD.ID_Phong
        JOIN NguoiThue_HopDong NTHD ON HD.ID_HopDong = NTHD.ID_HopDong
        WHERE P.ID_ChiNhanh = ?
        GROUP BY P.soPhong
        """;
        try (ResultSet rs = XJdbc.executeQuery(sql, idChiNhanh)) {
            while (rs.next()) {
                list.add(new Object[]{rs.getString(1), rs.getInt(2)});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Object[]> layDanhSachNguoiDungTheoSoPhongVaChiNhanh(String soPhong, int idChiNhanh) {
        List<Object[]> list = new ArrayList<>();
        String sql = """
        SELECT ND.tenNguoiDung, P.soPhong
        FROM NguoiDung ND
        JOIN NguoiThue_HopDong NTHD ON ND.ID_NguoiDung = NTHD.ID_NguoiDung
        JOIN HopDong HD ON HD.ID_HopDong = NTHD.ID_HopDong
        JOIN Phong P ON P.ID_Phong = HD.ID_Phong
        WHERE P.soPhong = ? AND P.ID_ChiNhanh = ?
        """;
        try (ResultSet rs = XJdbc.executeQuery(sql, soPhong, idChiNhanh)) {
            while (rs.next()) {
                list.add(new Object[]{rs.getString(1), rs.getString(2)});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void themNguoiO(String tenNguoi, String soPhong, int idChiNhanh) {
        try {
            // Bắt đầu transaction
            XJdbc.beginTransaction();

            // 1. Thêm người vào bảng NguoiDung
            String sql1 = "INSERT INTO NguoiDung (tenNguoiDung) VALUES (?)";
            XJdbc.executeUpdate(sql1, tenNguoi);

            // 2. Lấy ID người dùng vừa thêm
            String sql2 = "SELECT TOP 1 ID_NguoiDung FROM NguoiDung ORDER BY ID_NguoiDung DESC";
            int idNguoiDung = XJdbc.getValue(sql2);

            // 3. Tìm ID_HopDong từ soPhong và chi nhánh
            String sql3 = """
            SELECT HD.ID_HopDong FROM HopDong HD
            JOIN Phong P ON P.ID_Phong = HD.ID_Phong
            WHERE P.soPhong = ? AND P.ID_ChiNhanh = ?
        """;
            Integer idHopDong = XJdbc.getValue(sql3, soPhong, idChiNhanh);
            if (idHopDong == null) {
                throw new RuntimeException("Không tìm thấy hợp đồng.");
            }

            // 4. Ghi vào bảng liên kết
            String sql4 = "INSERT INTO NguoiThue_HopDong (ID_NguoiDung, ID_HopDong) VALUES (?, ?)";
            XJdbc.executeUpdate(sql4, idNguoiDung, idHopDong);

            // Thành công thì commit
            XJdbc.commitTransaction();
        } catch (Exception ex) {
            ex.printStackTrace();
            XJdbc.rollbackTransaction();
            throw new RuntimeException("Thêm người ở thất bại: " + ex.getMessage());
        }
    }
}
