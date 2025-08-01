package poly.nhatro.dao.impl;

import poly.nhatro.dao.GopYDAO;
import poly.nhatro.entity.GopY;
import poly.nhatro.util.XJdbc; // Import XJdbc của bạn
import poly.nhatro.util.XQuery; // Import XQuery
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GopYDAOImpl implements GopYDAO {
    // Định nghĩa các câu lệnh SQL
    String createSql = "INSERT INTO GopY (noiDung, ID_NguoiDung, ID_ChiNhanh) VALUES (?, ?, ?)";
    String updateSql = "UPDATE GopY SET noiDung = ?, ID_NguoiDung = ?, ID_ChiNhanh = ? WHERE ID_GopY = ?";
    String deleteSql = "DELETE FROM GopY WHERE ID_GopY = ?";
    
    // Cập nhật câu lệnh SELECT để JOIN với NguoiDung và ChiNhanh để lấy tên
    String selectByIdSql = "SELECT gy.ID_GopY, gy.noiDung, gy.ID_NguoiDung, gy.ID_ChiNhanh, " +
                           "nd.tenNguoiDung, cn.tenChiNhanh " +
                           "FROM GopY gy " +
                           "JOIN NguoiDung nd ON gy.ID_NguoiDung = nd.ID_NguoiDung " +
                           "JOIN ChiNhanh cn ON gy.ID_ChiNhanh = cn.ID_ChiNhanh " +
                           "WHERE gy.ID_GopY = ?";
    
    // Cập nhật câu lệnh SELECT ALL để JOIN với NguoiDung và ChiNhanh để lấy tên
    String selectAllSql = "SELECT gy.ID_GopY, gy.noiDung, gy.ID_NguoiDung, gy.ID_ChiNhanh, " +
                          "nd.tenNguoiDung, cn.tenChiNhanh " +
                          "FROM GopY gy " +
                          "JOIN NguoiDung nd ON gy.ID_NguoiDung = nd.ID_NguoiDung " +
                          "JOIN ChiNhanh cn ON gy.ID_ChiNhanh = cn.ID_ChiNhanh";

    @Override 
    public void create(GopY entity) {
        try {
            XJdbc.executeUpdate(createSql, entity.getNoiDung(), entity.getIdNguoiDung(), entity.getIdChiNhanh());
            System.out.println("DAO: Thêm GopY thành công.");
        } catch (Exception ex) {
            System.err.println("DAO Error: Lỗi khi thêm GopY: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override 
    public void update(GopY entity) {
        try {
            XJdbc.executeUpdate(updateSql, entity.getNoiDung(), entity.getIdNguoiDung(), entity.getIdChiNhanh(), entity.getIdGopY());
            System.out.println("DAO: Cập nhật GopY thành công. ID: " + entity.getIdGopY());
        } catch (Exception ex) {
            System.err.println("DAO Error: Lỗi khi cập nhật GopY: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override 
    public void deleteById(Integer id) {
        try {
            XJdbc.executeUpdate(deleteSql, id);
            System.out.println("DAO: Xóa GopY thành công. ID: " + id);
        } catch (Exception ex) {
            System.err.println("DAO Error: Lỗi khi xóa GopY: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override 
    public GopY findById(Integer id) {
        List<GopY> list = selectBySql(selectByIdSql, id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override 
    public List<GopY> findAll() {
        return selectBySql(selectAllSql);
    }

    protected List<GopY> selectBySql(String sql, Object... args) {
        List<GopY> list = new java.util.ArrayList<>();
        ResultSet rs = null; 
        try {
            rs = XJdbc.executeQuery(sql, args); 
            while (rs.next()) {
                GopY entity = new GopY();
                entity.setIdGopY(rs.getInt("ID_GopY"));
                entity.setNoiDung(rs.getString("noiDung"));
                entity.setIdNguoiDung(rs.getInt("ID_NguoiDung"));
                entity.setIdChiNhanh(rs.getInt("ID_ChiNhanh"));
                // Set the new fields (tenNguoiDung, tenChiNhanh)
                entity.setTenNguoiDung(rs.getString("tenNguoiDung"));
                entity.setTenChiNhanh(rs.getString("tenChiNhanh"));
                
                list.add(entity);
            }
        } catch (SQLException ex) {
            System.err.println("DAO Error: Lỗi khi thực hiện truy vấn SQL trong selectBySql: " + ex.getMessage());
            throw new RuntimeException("Lỗi khi thực hiện truy vấn SQL: " + ex.getMessage(), ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.err.println("DAO Error: Lỗi khi đóng ResultSet: " + e.getMessage());
                }
            }
        }
        return list;
    }

    @Override 
    public List<GopY> findByChiNhanh(Integer idChiNhanh) {
        // Cập nhật câu lệnh SELECT để JOIN và lấy tên khi lọc theo chi nhánh
        String sql = "SELECT gy.ID_GopY, gy.noiDung, gy.ID_NguoiDung, gy.ID_ChiNhanh, " +
                     "nd.tenNguoiDung, cn.tenChiNhanh " +
                     "FROM GopY gy " +
                     "JOIN NguoiDung nd ON gy.ID_NguoiDung = nd.ID_NguoiDung " +
                     "JOIN ChiNhanh cn ON gy.ID_ChiNhanh = cn.ID_ChiNhanh " +
                     "WHERE gy.ID_ChiNhanh = ?";
        return selectBySql(sql, idChiNhanh);
    }

    @Override
    public Integer findNguoiDungIdByTen(String tenNguoiDung) {
        String sql = "SELECT ID_NguoiDung FROM NguoiDung WHERE tenNguoiDung = ?";
        try {
            return XJdbc.getValue(sql, tenNguoiDung);
        } catch (RuntimeException ex) {
            System.err.println("Lỗi khi tìm ID_NguoiDung theo tenNguoiDung: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public Integer findChiNhanhIdByTen(String tenChiNhanh) {
        String sql = "SELECT ID_ChiNhanh FROM ChiNhanh WHERE tenChiNhanh = ?";
        try {
            return XJdbc.getValue(sql, tenChiNhanh);
        } catch (RuntimeException ex) {
            System.err.println("Lỗi khi tìm ID_ChiNhanh theo tenChiNhanh: " + ex.getMessage());
            throw ex;
        }
    }
}
