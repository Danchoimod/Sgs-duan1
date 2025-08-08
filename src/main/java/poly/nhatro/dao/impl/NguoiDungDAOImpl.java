package poly.nhatro.dao.impl;

import poly.nhatro.dao.NguoiDungDAO;
import poly.nhatro.entity.NguoiDung;
import poly.nhatro.util.XJdbc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NguoiDungDAOImpl implements NguoiDungDAO {

    @Override
    public List<NguoiDung> findAll() {
        String sql = "SELECT * FROM NguoiDung WHERE ISNULL(trangThai, '') <> N'Đã xóa' ORDER BY tenNguoiDung";
        return select(sql);
    }

    @Override
    public NguoiDung findById(int id) {
        String sql = "SELECT * FROM NguoiDung WHERE ID_NguoiDung = ? AND ISNULL(trangThai, '') <> N'Đã xóa'";
        List<NguoiDung> list = select(sql, id);
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public void insert(NguoiDung nguoiDung) {
        String sql = "INSERT INTO NguoiDung (tenNguoiDung, soDienThoai, email, matKhau, namSinh, diaChi, cccdCmnn, anhTruocCccd, anhSauCccd, vaiTro, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        XJdbc.executeUpdate(sql, 
            nguoiDung.getTenNguoiDung(),
            nguoiDung.getSoDienThoai(),
            nguoiDung.getEmail(),
            nguoiDung.getMatKhau(),
            nguoiDung.getNamSinh(),
            nguoiDung.getDiaChi(),
            nguoiDung.getCccdCmnn(),
            nguoiDung.getAnhTruocCccd(),
            nguoiDung.getAnhSauCccd(),
            nguoiDung.getVaiTro(),
            nguoiDung.getTrangThai()
        );
    }

    @Override
    public void update(NguoiDung nguoiDung) {
        String sql = "UPDATE NguoiDung SET tenNguoiDung=?, soDienThoai=?, email=?, matKhau=?, namSinh=?, diaChi=?, cccdCmnn=?, anhTruocCccd=?, anhSauCccd=?, vaiTro=?, trangThai=? WHERE ID_NguoiDung=?";
        XJdbc.executeUpdate(sql,
            nguoiDung.getTenNguoiDung(),
            nguoiDung.getSoDienThoai(),
            nguoiDung.getEmail(),
            nguoiDung.getMatKhau(),
            nguoiDung.getNamSinh(),
            nguoiDung.getDiaChi(),
            nguoiDung.getCccdCmnn(),
            nguoiDung.getAnhTruocCccd(),
            nguoiDung.getAnhSauCccd(),
            nguoiDung.getVaiTro(),
            nguoiDung.getTrangThai(),
            nguoiDung.getID_NguoiDung()
        );
    }

    @Override
    public void delete(int id) {
        // Soft delete
        String sql = "UPDATE NguoiDung SET trangThai = N'Đã xóa' WHERE ID_NguoiDung = ?";
        XJdbc.executeUpdate(sql, id);
    }

    @Override
    public List<NguoiDung> findByVaiTro(String vaiTro) {
        String sql = "SELECT * FROM NguoiDung WHERE vaiTro = ? AND ISNULL(trangThai, '') <> N'Đã xóa' ORDER BY tenNguoiDung";
        return select(sql, vaiTro);
    }

    @Override
    public List<NguoiDung> findByTrangThai(String trangThai) {
        String sql = "SELECT * FROM NguoiDung WHERE trangThai = ? ORDER BY tenNguoiDung";
        return select(sql, trangThai);
    }

    @Override
    public List<NguoiDung> findAvailableForContract() {
        // Tìm những người dùng chưa có hợp đồng đang hoạt động
        // Một hợp đồng được coi là đang hoạt động nếu ngày hiện tại <= ngày tạo + thời hạn (tháng)
        String sql = "SELECT DISTINCT nd.* FROM NguoiDung nd " +
                    "WHERE nd.trangThai = N'Hoạt động' " +
                    "AND nd.ID_NguoiDung NOT IN (" +
                        "SELECT DISTINCT hd.ID_NguoiDung " +
                        "FROM HopDong hd " +
                        "WHERE DATEADD(MONTH, hd.thoiHan, hd.ngayTao) >= GETDATE() " +
                        "AND ISNULL(hd.trangThai, 0) = 0" +
                    ") " +
                    "ORDER BY nd.tenNguoiDung";
        return select(sql);
    }

    private List<NguoiDung> select(String sql, Object... args) {
        List<NguoiDung> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(sql, args);
            while (rs.next()) {
                NguoiDung nguoiDung = new NguoiDung();
                nguoiDung.setID_NguoiDung(rs.getInt("ID_NguoiDung"));
                nguoiDung.setTenNguoiDung(rs.getString("tenNguoiDung"));
                nguoiDung.setSoDienThoai(rs.getString("soDienThoai"));
                nguoiDung.setEmail(rs.getString("email"));
                nguoiDung.setMatKhau(rs.getString("matKhau"));
                nguoiDung.setNamSinh(rs.getDate("namSinh"));
                nguoiDung.setDiaChi(rs.getString("diaChi"));
                nguoiDung.setCccdCmnn(rs.getString("cccdCmnn"));
                nguoiDung.setAnhTruocCccd(rs.getString("anhTruocCccd"));
                nguoiDung.setAnhSauCccd(rs.getString("anhSauCccd"));
                nguoiDung.setVaiTro(rs.getString("vaiTro"));
                nguoiDung.setTrangThai(rs.getString("trangThai"));
                list.add(nguoiDung);
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }
}
