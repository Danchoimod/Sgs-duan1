package poly.nhatro.dao.impl;

import poly.nhatro.dao.ChiNhanhDAO;
import poly.nhatro.entity.ChiNhanh;
import poly.nhatro.util.XJdbc;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChiNhanhDAOImpl implements ChiNhanhDAO {

    @Override
    public List<ChiNhanh> getAll() {
        String sql = "SELECT * FROM ChiNhanh"; // Đã đúng
        return this.getBySql(sql);
    }

    @Override
    public boolean add(ChiNhanh chiNhanh) {
        String sql = "INSERT INTO ChiNhanh(tenChiNhanh, diaChi, giaDien, giaNuoc) VALUES(?,?,?,?)"; // Đã sửa CHI_NHANH -> ChiNhanh
        return XJdbc.executeUpdate(sql,
                chiNhanh.getTenChiNhanh(),
                chiNhanh.getDiaChi(),
                chiNhanh.getGiaDien(),
                chiNhanh.getGiaNuoc()) > 0;
    }

    @Override
    public boolean update(ChiNhanh chiNhanh) {
        String sql = "UPDATE ChiNhanh SET tenChiNhanh=?, diaChi=?, giaDien=?, giaNuoc=? WHERE ID_ChiNhanh=?"; // Đã sửa CHI_NHANH -> ChiNhanh
        return XJdbc.executeUpdate(sql,
                chiNhanh.getTenChiNhanh(),
                chiNhanh.getDiaChi(),
                chiNhanh.getGiaDien(),
                chiNhanh.getGiaNuoc(),
                chiNhanh.getID_ChiNhanh()) > 0;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM ChiNhanh WHERE ID_ChiNhanh=?"; // Đã sửa CHI_NHANH -> ChiNhanh
        return XJdbc.executeUpdate(sql, id) > 0;
    }

    @Override
    public List<ChiNhanh> search(String keyword) {
        String sql = "SELECT * FROM ChiNhanh WHERE tenChiNhanh LIKE ? OR diaChi LIKE ?"; // Đã sửa CHI_NHANH -> ChiNhanh
        return this.getBySql(sql, "%" + keyword + "%", "%" + keyword + "%");
    }

    private List<ChiNhanh> getBySql(String sql, Object... args) {
        List<ChiNhanh> list = new ArrayList<>();
        try (ResultSet rs = XJdbc.executeQuery(sql, args)) {
            while (rs.next()) {
                ChiNhanh chiNhanh = new ChiNhanh();
                chiNhanh.setID_ChiNhanh(rs.getInt("ID_ChiNhanh"));
                chiNhanh.setTenChiNhanh(rs.getString("tenChiNhanh"));
                chiNhanh.setDiaChi(rs.getString("diaChi"));
                // Chú ý: trong SQL script, giaDien và giaNuoc là INT,
                // nhưng ở đây bạn đang đọc là BigDecimal.
                // Đảm bảo kiểu dữ liệu khớp hoặc xử lý chuyển đổi.
                // Nếu trong DB là INT, bạn nên dùng getInt() và sau đó chuyển sang BigDecimal nếu cần.
                chiNhanh.setGiaDien(BigDecimal.valueOf(rs.getInt("giaDien"))); // Sửa getBigDecimal -> getInt
                chiNhanh.setGiaNuoc(BigDecimal.valueOf(rs.getInt("giaNuoc"))); // Sửa getBigDecimal -> getInt
                list.add(chiNhanh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ChiNhanh getById(int id) {
        String sql = "SELECT ID_ChiNhanh, tenChiNhanh, diaChi, giaDien, giaNuoc FROM ChiNhanh WHERE ID_ChiNhanh = ?"; // Đã sửa CHI_NHANH -> ChiNhanh
        List<ChiNhanh> list = this.getBySql(sql, id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public BigDecimal getGiaDienById(int id) {
        String sql = "SELECT giaDien FROM ChiNhanh WHERE ID_ChiNhanh = ?"; // Đã sửa CHI_NHANH -> ChiNhanh
        try (ResultSet rs = XJdbc.executeQuery(sql, id)) {
            if (rs.next()) {
                return BigDecimal.valueOf(rs.getInt("giaDien")); // Sửa getBigDecimal -> getInt
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getGiaNuocById(int id) {
        String sql = "SELECT giaNuoc FROM ChiNhanh WHERE ID_ChiNhanh = ?"; // Đã sửa CHI_NHANH -> ChiNhanh
        try (ResultSet rs = XJdbc.executeQuery(sql, id)) {
            if (rs.next()) {
                return BigDecimal.valueOf(rs.getInt("giaNuoc")); // Sửa getBigDecimal -> getInt
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    @Override
    public List<String> getAllBranchNames() {
        List<String> branchNames = new ArrayList<>();
        String sql = "SELECT tenChiNhanh FROM ChiNhanh ORDER BY tenChiNhanh"; // Đã sửa CHI_NHANH -> ChiNhanh
        try (ResultSet rs = XJdbc.executeQuery(sql)) {
            while (rs.next()) {
                branchNames.add(rs.getString("tenChiNhanh"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return branchNames;
    }
    
    @Override
    public int layIDTheoTen(String tenChiNhanh) {
        String sql = "SELECT ID_ChiNhanh FROM ChiNhanh WHERE tenChiNhanh=?"; // Đã sửa CHI_NHANH -> ChiNhanh
        try (ResultSet rs = XJdbc.executeQuery(sql, tenChiNhanh)) {
            if (rs.next()) {
                return rs.getInt("ID_ChiNhanh");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
