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
    public HopDong insert(HopDong hopDong) {
        String sql = "INSERT INTO HopDong (ngayTao, thoiHan, tienCoc, nuocBanDau, dienBanDau, ID_NguoiDung, ID_Phong) VALUES (?, ?, ?, ?, ?, ?, ?)";
        XJdbc.executeUpdate(sql, // Using XJdbc.executeUpdate
                hopDong.getNgayTao(),
                hopDong.getThoiHan(),
                hopDong.getTienCoc(),
                hopDong.getNuocBanDau(),
                hopDong.getDienBanDau(),
                hopDong.getID_NguoiDung(),
                hopDong.getID_Phong());

        // Note: XJdbc.executeUpdate does not return generated keys directly.
        // If you need the generated ID, you would typically use a separate SELECT SCOPE_IDENTITY()
        // query after the insert, or modify XJdbc to support Statement.RETURN_GENERATED_KEYS.
        return hopDong;
    }

    @Override
    public HopDong update(HopDong hopDong) {
        String sql = "UPDATE HopDong SET ngayTao=?, thoiHan=?, tienCoc=?, nuocBanDau=?, dienBanDau=?, ID_NguoiDung=?, ID_Phong=? WHERE ID_HopDong=?";
        XJdbc.executeUpdate(sql, // Using XJdbc.executeUpdate
                hopDong.getNgayTao(),
                hopDong.getThoiHan(),
                hopDong.getTienCoc(),
                hopDong.getNuocBanDau(),
                hopDong.getDienBanDau(),
                hopDong.getID_NguoiDung(),
                hopDong.getID_Phong(),
                hopDong.getID_HopDong());
        return hopDong;
    }

    @Override
    public void delete(int ID_HopDong) {
        String sql = "DELETE FROM HopDong WHERE ID_HopDong=?";
        XJdbc.executeUpdate(sql, ID_HopDong); // Using XJdbc.executeUpdate
    }

    @Override
    public HopDong selectById(int ID_HopDong) {
        String sql = "SELECT * FROM HopDong WHERE ID_HopDong=?";
        List<HopDong> list = select(sql, ID_HopDong);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<HopDong> selectAll() {
        String sql = "SELECT * FROM HopDong";
        return select(sql);
    }

    @Override
    public List<HopDong> selectActiveContracts() {
        // Contracts are active if their end date (ngayTao + thoiHan months) is in the future
        String sql = "SELECT * FROM HopDong WHERE DATEADD(month, thoiHan, ngayTao) > GETDATE()";
        return select(sql);
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
