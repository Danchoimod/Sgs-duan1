package poly.nhatro.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

    // Helper method to read a single HopDong from a ResultSet
    private HopDong readFromResultSet(ResultSet rs) throws SQLException {
        HopDong hopDong = new HopDong();
        hopDong.setID_HopDong(rs.getInt("ID_HopDong"));
        hopDong.setNgayTao(rs.getTimestamp("ngayTao")); // Use getTimestamp for DATETIME
        hopDong.setThoiHan(rs.getInt("thoiHan"));
        hopDong.setTienCoc(rs.getInt("tienCoc"));
        hopDong.setNuocBanDau(rs.getInt("nuocBanDau"));
        hopDong.setDienBanDau(rs.getInt("dienBanDau"));
        hopDong.setID_NguoiDung(rs.getInt("ID_NguoiDung"));
        hopDong.setID_Phong(rs.getInt("ID_Phong"));
        return hopDong;
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
    public List<HopDong> selectExpiredContracts() {
        // Contracts are expired if their end date (ngayTao + thoiHan months) is in the past
        String sql = "SELECT * FROM HopDong WHERE DATEADD(month, thoiHan, ngayTao) <= GETDATE()";
        return select(sql);
    }

    @Override
    public List<HopDong> searchContracts(String keyword) {
        String sql = "SELECT hd.* FROM HopDong hd "
                + "JOIN NguoiDung nd ON hd.ID_NguoiDung = nd.ID_NguoiDung "
                + "JOIN Phong p ON hd.ID_Phong = p.ID_Phong "
                + "WHERE hd.ID_HopDong LIKE ? OR nd.tenNguoiDung LIKE ? OR p.soPhong LIKE ?";
        String searchPattern = "%" + keyword + "%";
        return select(sql, searchPattern, searchPattern, searchPattern);
    }

    @Override
    public List<HopDong> selectByChiNhanh(int ID_ChiNhanh) {
        String sql = "SELECT hd.* FROM HopDong hd JOIN Phong p ON hd.ID_Phong = p.ID_Phong WHERE p.ID_ChiNhanh = ?";
        return select(sql, ID_ChiNhanh);
    }

    @Override
    public List<HopDong> selectByNguoiKiHopDong(int ID_NguoiDung) {
        String sql = "SELECT * FROM HopDong WHERE ID_NguoiDung = ?";
        return select(sql, ID_NguoiDung);
    }

    @Override
    public List<HopDong> selectByRoom(int ID_Phong) {
        String sql = "SELECT * FROM HopDong WHERE ID_Phong = ?";
        return select(sql, ID_Phong);
    }

    @Override
    public int timIdHopDongTheoSoPhongVaChiNhanh(String soPhong, int idChiNhanh) {
        int idHopDong = -1;
        String sql = """
        SELECT hd.ID_HopDong
        FROM HopDong hd
        JOIN Phong p ON p.ID_Phong = hd.ID_Phong
        WHERE p.soPhong = ? AND p.ID_ChiNhanh = ?
    """;

        try (Connection con = XJdbc.openConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, soPhong);
            ps.setInt(2, idChiNhanh);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                idHopDong = rs.getInt("ID_HopDong");
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return idHopDong;
    }

}
