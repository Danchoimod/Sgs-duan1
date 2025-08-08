package poly.nhatro.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import poly.nhatro.dao.PhongDao;
import poly.nhatro.entity.Phong;
import poly.nhatro.util.XJdbc;

public class PhongDaoImpl implements PhongDao {

    @Override
    public Phong create(Phong entity) {
        String sql = "INSERT INTO PHONG(giaPhong, trangThai, soPhong, moTa, anhPhong, ID_ChiNhanh) VALUES(?,?,?,?,?,?)";
        XJdbc.executeUpdate(sql,
                entity.getGiaPhong(),
                entity.getTrangThai(),
                entity.getSoPhong(),
                entity.getMoTa(),
                entity.getAnhPhong(),
                entity.getIdChiNhanh());
        return entity;
    }

    @Override
    public void update(Phong entity) {
        String sql = "UPDATE PHONG SET giaPhong=?, trangThai=?, soPhong=?, moTa=?, anhPhong=?, ID_ChiNhanh=? WHERE ID_Phong=?";
        XJdbc.executeUpdate(sql,
                entity.getGiaPhong(),
                entity.getTrangThai(),
                entity.getSoPhong(),
                entity.getMoTa(),
                entity.getAnhPhong(),
                entity.getIdChiNhanh(),
                entity.getIdPhong());
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM PHONG WHERE ID_Phong=?";
        XJdbc.executeUpdate(sql, id);
    }

    @Override
    public List<Phong> findAll() {
        String sql = "SELECT * FROM PHONG";
        return select(sql);
    }

    @Override
    public Phong findById(Integer id) {
        String sql = "SELECT * FROM PHONG WHERE ID_Phong=?";
        List<Phong> list = select(sql, id);
        return list.size() > 0 ? list.get(0) : null;
    }

    // Phương thức mới: Tìm phòng theo số phòng
    public List<Phong> findBySoPhong(String soPhong) {
        String sql = "SELECT * FROM PHONG WHERE soPhong LIKE ?";
        return select(sql, "%" + soPhong + "%");
    }

    // Phương thức mới: Tìm phòng theo chi nhánh
    public List<Phong> findByChiNhanh(Integer idChiNhanh) {
        String sql = "SELECT * FROM PHONG WHERE ID_ChiNhanh=?";
        return select(sql, idChiNhanh);
    }

    // Phòng trống theo chi nhánh: không có hợp đồng đang hoạt động và trạng thái phòng là 'Trống'
    public List<Phong> findAvailableByChiNhanh(Integer idChiNhanh) {
        String sql =
            "SELECT p.* FROM PHONG p " +
            "LEFT JOIN HopDong hd ON hd.ID_Phong = p.ID_Phong AND ISNULL(hd.trangThai, 0) = 0 " +
            "WHERE p.ID_ChiNhanh = ? AND p.trangThai = N'Trống' AND hd.ID_HopDong IS NULL";
        return select(sql, idChiNhanh);
    }

    // Tất cả phòng trống: không có hợp đồng đang hoạt động và trạng thái phòng là 'Trống'
    public List<Phong> findAllAvailable() {
        String sql =
            "SELECT p.* FROM PHONG p " +
            "LEFT JOIN HopDong hd ON hd.ID_Phong = p.ID_Phong AND ISNULL(hd.trangThai, 0) = 0 " +
            "WHERE p.trangThai = N'Trống' AND hd.ID_HopDong IS NULL";
        return select(sql);
    }

    // Phương thức mới: Đếm số lượng phòng
    public int countPhong() {
        String sql = "SELECT COUNT(*) FROM PHONG";
        try {
            ResultSet rs = XJdbc.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return 0;
    }

    private List<Phong> select(String sql, Object... args) {
        List<Phong> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(sql, args);
            while (rs.next()) {
                Phong phong = new Phong();
                phong.setIdPhong(rs.getInt("ID_Phong"));
                phong.setGiaPhong(rs.getBigDecimal("giaPhong"));
                phong.setTrangThai(rs.getString("trangThai"));
                phong.setSoPhong(rs.getString("soPhong"));

                // Xử lý các trường có thể NULL
                String moTa = rs.getString("moTa");
                phong.setMoTa(rs.wasNull() ? null : moTa);

                String anhPhong = rs.getString("anhPhong");
                phong.setAnhPhong(rs.wasNull() ? null : anhPhong);

                phong.setIdChiNhanh(rs.getInt("ID_ChiNhanh"));
                list.add(phong);
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }
}
