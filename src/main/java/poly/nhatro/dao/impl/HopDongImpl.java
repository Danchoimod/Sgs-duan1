package poly.nhatro.dao.impl;

import poly.nhatro.dao.HopDongDAO;
import poly.nhatro.entity.HopDong;
import poly.nhatro.util.XJdbc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HopDongImpl implements HopDongDAO {

    private final String INSERT_SQL = "INSERT INTO HOP_DONG (NgayBatDau, NgayKetThuc, SoTienCoc, ID_NguoiDung, ID_Phong, ID_ChiNhanh) VALUES (?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE HOP_DONG SET NgayBatDau = ?, NgayKetThuc = ?, SoTienCoc = ?, ID_NguoiDung = ?, ID_Phong = ?, ID_ChiNhanh = ? WHERE ID_HopDong = ?";
    private final String DELETE_SQL = "DELETE FROM HOP_DONG WHERE ID_HopDong = ?";
    private final String SELECT_ALL_SQL = "SELECT * FROM HOP_DONG";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM HOP_DONG WHERE ID_HopDong = ?";

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

    @Override
    public void update(HopDong entity) {
        try {
            XJdbc.executeUpdate(UPDATE_SQL,
                    entity.getNgayBatDau(),
                    entity.getNgayKetThuc(),
                    entity.getSoTienCoc(),
                    entity.getID_NguoiDung(),
                    entity.getID_Phong(), // Là int
                    entity.getID_ChiNhanh(), // Là int
                    entity.getID_HopDong()); // Là int
        } catch (RuntimeException e) {
            System.err.println("Lỗi khi cập nhật hợp đồng: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
            throw new RuntimeException("Cập nhật hợp đồng thất bại.", e);
        }
    }

    @Override
    public void delete(String id) { 
        try {
            XJdbc.executeUpdate(DELETE_SQL, id);
        } catch (RuntimeException e) {
            System.err.println("Lỗi khi xóa hợp đồng: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
            throw new RuntimeException("Xóa hợp đồng thất bại.", e);
        }
    }

    @Override
    public HopDong getById(String id) {
        List<HopDong> list = selectBySql(SELECT_BY_ID_SQL, id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<HopDong> getAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    private List<HopDong> selectBySql(String sql, Object... args) {
        List<HopDong> list = new ArrayList<>();
        try (ResultSet rs = XJdbc.executeQuery(sql, args)) {
            while (rs.next()) {
                HopDong entity = new HopDong();
                entity.setID_HopDong(rs.getInt("ID_HopDong")); // Đọc là int
                entity.setNgayBatDau(rs.getDate("NgayBatDau"));
                entity.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                entity.setSoTienCoc(rs.getDouble("SoTienCoc"));
                entity.setID_NguoiDung(rs.getInt("ID_NguoiDung"));
                entity.setID_Phong(rs.getInt("ID_Phong"));    // Đọc là int
                entity.setID_ChiNhanh(rs.getInt("ID_ChiNhanh")); // Đọc là int
                list.add(entity);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi truy vấn dữ liệu: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public boolean existsHopDongById(int id) {
        try {
            String sql = "SELECT COUNT(*) FROM HOP_DONG WHERE ID_HopDong = ?";
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
}
