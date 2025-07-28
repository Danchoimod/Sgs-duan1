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
        // Đã sửa tên bảng từ PHONG -> Phong
        // Đã sửa tên cột từ hinhAnh -> anhPhong
        String sql = "INSERT INTO Phong(giaPhong, trangThai, soPhong, moTa, anhPhong, ID_ChiNhanh) VALUES(?,?,?,?,?,?)";
        XJdbc.executeUpdate(sql,
                entity.getGiaPhong(),
                entity.isTrangThai(),
                entity.getSoPhong(),
                entity.getMoTa(),
                entity.getAnhPhong(), // Đã sửa getHinhAnh() -> getAnhPhong()
                entity.getIdChiNhanh());
        return entity;
    }

    @Override
    public void update(Phong entity) {
        // Đã sửa tên bảng từ PHONG -> Phong
        // Đã sửa tên cột từ hinhAnh -> anhPhong
        String sql = "UPDATE Phong SET giaPhong=?, trangThai=?, soPhong=?, moTa=?, anhPhong=?, ID_ChiNhanh=? WHERE ID_Phong=?";
        XJdbc.executeUpdate(sql,
                entity.getGiaPhong(),
                entity.isTrangThai(),
                entity.getSoPhong(),
                entity.getMoTa(),
                entity.getAnhPhong(), // Đã sửa getHinhAnh() -> getAnhPhong()
                entity.getIdChiNhanh(),
                entity.getIdPhong());
    }

    @Override
    public void deleteById(Integer id) {
        // Đã sửa tên bảng từ PHONG -> Phong
        String sql = "DELETE FROM Phong WHERE ID_Phong=?";
        XJdbc.executeUpdate(sql, id);
    }

    @Override
    public List<Phong> findAll() {
        // Đã sửa tên bảng từ PHONG -> Phong
        String sql = "SELECT * FROM Phong";
        return select(sql);
    }

    @Override
    public Phong findById(Integer id) {
        // Đã sửa tên bảng từ PHONG -> Phong
        String sql = "SELECT * FROM Phong WHERE ID_Phong=?";
        List<Phong> list = select(sql, id);
        return list.size() > 0 ? list.get(0) : null;
    }

    // Phương thức mới: Tìm phòng theo số phòng
    public List<Phong> findBySoPhong(String soPhong) {
        // Đã sửa tên bảng từ PHONG -> Phong
        String sql = "SELECT * FROM Phong WHERE soPhong LIKE ?";
        return select(sql, "%" + soPhong + "%");
    }

    // Phương thức mới: Tìm phòng theo chi nhánh
    public List<Phong> findByChiNhanh(Integer idChiNhanh) {
        // Đã sửa tên bảng từ PHONG -> Phong
        String sql = "SELECT * FROM Phong WHERE ID_ChiNhanh=?";
        return select(sql, idChiNhanh);
    }

    // Phương thức mới: Đếm số lượng phòng
    public int countPhong() {
        // Đã sửa tên bảng từ PHONG -> Phong
        String sql = "SELECT COUNT(*) FROM Phong";
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
                phong.setTrangThai(rs.getBoolean("trangThai"));
                phong.setSoPhong(rs.getString("soPhong"));

                // Xử lý các trường có thể NULL
                String moTa = rs.getString("moTa");
                phong.setMoTa(rs.wasNull() ? null : moTa);

                String anhPhong = rs.getString("anhPhong"); // Đã sửa tên cột từ hinhAnh -> anhPhong
                phong.setAnhPhong(rs.wasNull() ? null : anhPhong); // Đã sửa setHinhAnh() -> setAnhPhong()

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
