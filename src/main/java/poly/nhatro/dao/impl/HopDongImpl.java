package poly.nhatro.dao.impl;

import poly.nhatro.dao.HopDongDAO;
import poly.nhatro.entity.HopDong;
import poly.nhatro.util.XJdbc; // Using the provided XJdbc utility
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Calendar; // For date calculations

public class HopDongImpl implements HopDongDAO {

    private final String INSERT_SQL = "INSERT INTO HopDong (NgayBatDau, NgayKetThuc, SoTienCoc, ID_NguoiDung, ID_Phong, ID_ChiNhanh) VALUES (?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE HopDong SET NgayBatDau = ?, NgayKetThuc = ?, SoTienCoc = ?, ID_NguoiDung = ?, ID_Phong = ?, ID_ChiNhanh = ? WHERE ID_HopDong = ?";
    private final String DELETE_SQL = "DELETE FROM HopDong WHERE ID_HopDong = ?";
    private final String SELECT_ALL_SQL = "SELECT * FROM HopDong";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM HopDong WHERE ID_HopDong = ?";
    private final String SELECT_BY_USER_ID_SQL = "SELECT * FROM HopDong WHERE ID_NguoiDung = ?";

    @Override
    public void add(HopDong entity) {
        try {
            XJdbc.executeUpdate(INSERT_SQL,
                    entity.getNgayBatDau(),
                    entity.getNgayKetThuc(),
                    entity.getSoTienCoc(),
                    entity.getID_NguoiDung(),
                    entity.getID_Phong(), 
                    entity.getID_ChiNhanh()); 
        } catch (RuntimeException e) {
            System.err.println("Lỗi khi thêm hợp đồng: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
            throw new RuntimeException("Thêm hợp đồng thất bại.", e);
        }
    }

    // Generic select method using XJdbc
    private List<HopDong> select(String sql, Object... args) {
        List<HopDong> list = new ArrayList<>();
        try (ResultSet rs = XJdbc.executeQuery(sql, args)) { // Using XJdbc.executeQuery
            while (rs.next()) {
                list.add(readFromResultSet(rs));
            }
        } catch (SQLException e) {
            // It's good practice to log the error, and re-throw a runtime exception
            // or a custom checked exception.
            throw new RuntimeException("Error executing SQL query: " + sql, e);
        }
        return list;
    }

    @Override
    public boolean existsHopDongById(int id) {
        try {
            String sql = "SELECT COUNT(*) FROM HopDong WHERE ID_HopDong = ?";
            try (ResultSet rs = XJdbc.executeQuery(sql, id)) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra tồn tại hợp đồng: " + e.getMessage());
            throw new RuntimeException("Kiểm tra hợp đồng thất bại.", e);
        }
        return false;
    }

    @Override
    public List<HopDong> findByUserId(int userId) {
        return selectBySql(SELECT_BY_USER_ID_SQL, userId);
    }
}
