package poly.nhatro.dao.impl;

import poly.nhatro.dao.GopYDAO;
import poly.nhatro.entity.GopY;
import poly.nhatro.util.XJdbc; // Import XJdbc của bạn
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GopYDAOImpl implements GopYDAO {

    // Định nghĩa các câu lệnh SQL
    String createSql = "INSERT INTO GopY (noiDung, ID_NguoiDung, ID_ChiNhanh) VALUES (?, ?, ?)";
    String updateSql = "UPDATE GopY SET noiDung = ?, ID_NguoiDung = ?, ID_ChiNhanh = ? WHERE ID_GopY = ?";
    String deleteSql = "DELETE FROM GopY WHERE ID_GopY = ?";
    String selectByIdSql = "SELECT ID_GopY, noiDung, ID_NguoiDung, ID_ChiNhanh FROM GopY WHERE ID_GopY = ?";
    String selectAllSql = "SELECT ID_GopY, noiDung, ID_NguoiDung, ID_ChiNhanh FROM GopY";

    @Override 
    public void create(GopY entity) {
        try {
            // Đã đổi XJdbc.update thành XJdbc.executeUpdate
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
            // Đã đổi XJdbc.update thành XJdbc.executeUpdate
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
            // Đã đổi XJdbc.update thành XJdbc.executeUpdate
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
        ResultSet rs = null; // Khởi tạo ResultSet
        try {
            // Đã đổi XJdbc.query thành XJdbc.executeQuery
            rs = XJdbc.executeQuery(sql, args); 
            while (rs.next()) {
                GopY entity = new GopY();
                entity.setIdGopY(rs.getInt("ID_GopY"));
                entity.setNoiDung(rs.getString("noiDung"));

                int idNguoiDungFromDb = rs.getInt("ID_NguoiDung");
                int idChiNhanhFromDb = rs.getInt("ID_ChiNhanh");

                System.out.println("DAO DEBUG: Đọc từ ResultSet - ID_GopY: " + rs.getInt("ID_GopY") +
                                   ", NoiDung: " + rs.getString("noiDung") +
                                   ", ID_NguoiDung: " + idNguoiDungFromDb +
                                   ", ID_ChiNhanh: " + idChiNhanhFromDb);

                entity.setIdNguoiDung(idNguoiDungFromDb);
                entity.setIdChiNhanh(idChiNhanhFromDb);
                
                list.add(entity);
            }
        } catch (SQLException ex) {
            System.err.println("DAO Error: Lỗi khi thực hiện truy vấn SQL trong selectBySql: " + ex.getMessage());
            throw new RuntimeException("Lỗi khi thực hiện truy vấn SQL: " + ex.getMessage(), ex);
        } finally {
            // Đảm bảo ResultSet được đóng
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.err.println("DAO Error: Lỗi khi đóng ResultSet: " + e.getMessage());
                }
            }
            // Không đóng Connection ở đây vì XJdbc quản lý static Connection
            // và Connection có thể cần cho các thao tác khác.
            // XJdbc.closeConnection() nên được gọi ở cuối phiên làm việc của ứng dụng.
        }
        return list;
    }

    @Override 
    public List<GopY> findByChiNhanh(Integer idChiNhanh) {
        String sql = "SELECT ID_GopY, noiDung, ID_NguoiDung, ID_ChiNhanh FROM GopY WHERE ID_ChiNhanh = ?";
        return selectBySql(sql, idChiNhanh);
    }
}