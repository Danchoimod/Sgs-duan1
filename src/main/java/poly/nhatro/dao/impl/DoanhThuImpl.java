package poly.nhatro.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import poly.nhatro.dao.DoanhThuDao;
import poly.nhatro.entity.DoanhThu;
import poly.nhatro.util.XJdbc;

/**
 * DoanhThuImpl - Implementation của DoanhThuDao
 * Xử lý các thao tác liên quan đến doanh thu
 */
public class DoanhThuImpl implements DoanhThuDao {
    
    // CrudDao methods - chỉ implement những method cần thiết
    @Override
    public DoanhThu create(DoanhThu entity) {
        throw new UnsupportedOperationException("DoanhThu được tạo từ HoaDon, không thể tạo trực tiếp");
    }
    
    @Override
    public void update(DoanhThu entity) {
        if (entity == null || entity.getIdHoaDon() == 0) {
            throw new IllegalArgumentException("Entity hoặc ID hóa đơn không hợp lệ");
        }
        updateStatus(entity.getIdHoaDon(), entity.getTrangThaiThanhToan());
    }
    
    @Override
    public void deleteById(Integer id) {
        throw new UnsupportedOperationException("Không thể xóa DoanhThu");
    }
    
    @Override
    public DoanhThu findById(Integer id) {
        return getById(id);
    }
    
    @Override
    public List<DoanhThu> findAll() {
        return getAllSimple();
    }
    
    // Query đầy đủ cho các thao tác chi tiết
    private final String QUERY_FULL = """
        SELECT
            hdon.ID_HoaDon,
            p.soPhong AS tenPhong,
            nd.tenNguoiDung,
            cn.tenChiNhanh,
            dn.soDienCu,
            dn.soDienMoi,
            dn.soNuocCu,
            dn.soNuocMoi,
            (dn.soDienMoi - dn.soDienCu) AS soDienDaDung,
            (dn.soNuocMoi - dn.soNuocCu) AS soNuocDaDung,
            (dn.soDienMoi - dn.soDienCu) * cn.giaDien AS tienDien,
            (dn.soNuocMoi - dn.soNuocCu) * cn.giaNuoc AS tienNuoc,
            p.giaPhong AS tienPhong,
            ((dn.soDienMoi - dn.soDienCu) * cn.giaDien + 
             (dn.soNuocMoi - dn.soNuocCu) * cn.giaNuoc + 
             p.giaPhong) AS tongTien,
            hdon.trangThai AS trangThaiThanhToan,
            hdon.ngayTao AS ngayThanhToan,
            dn.thangNam AS thangNamDienNuoc,
            cn.giaDien,
            cn.giaNuoc
        FROM HoaDon hdon
            JOIN HopDong hd ON hdon.ID_HopDong = hd.ID_HopDong
            JOIN NguoiDung nd ON hd.ID_NguoiDung = nd.ID_NguoiDung
            JOIN Phong p ON hd.ID_Phong = p.ID_Phong
            JOIN DienNuoc dn ON p.ID_Phong = dn.ID_Phong 
                AND dn.thangNam = (
                    SELECT MAX(dn2.thangNam) 
                    FROM DienNuoc dn2 
                    WHERE dn2.ID_Phong = p.ID_Phong 
                    AND dn2.thangNam <= hdon.ngayTao
                )
            JOIN ChiNhanh cn ON p.ID_ChiNhanh = cn.ID_ChiNhanh
        WHERE hdon.trangThai IS NOT NULL
        ORDER BY hdon.ngayTao DESC
        """;
    
    // Query đơn giản cho hiển thị bảng
    private final String QUERY_SIMPLE = """
        SELECT
            hdon.ID_HoaDon,
            p.soPhong AS tenPhong,
            COALESCE((dn.soDienMoi - dn.soDienCu) * cn.giaDien, 0) AS tienDien,
            COALESCE((dn.soNuocMoi - dn.soNuocCu) * cn.giaNuoc, 0) AS tienNuoc,
            p.giaPhong AS tienPhong,
            (COALESCE((dn.soDienMoi - dn.soDienCu) * cn.giaDien, 0) + 
             COALESCE((dn.soNuocMoi - dn.soNuocCu) * cn.giaNuoc, 0) + 
             p.giaPhong) AS tongTien,
            hdon.ngayTao AS ngayThanhToan,
            hdon.trangThai AS trangThaiThanhToan,
            dn.soDienCu,
            dn.soDienMoi,
            dn.soNuocCu,
            dn.soNuocMoi,
            cn.giaDien,
            cn.giaNuoc
        FROM HoaDon hdon
            JOIN HopDong hd ON hdon.ID_HopDong = hd.ID_HopDong
            JOIN Phong p ON hd.ID_Phong = p.ID_Phong
            JOIN ChiNhanh cn ON p.ID_ChiNhanh = cn.ID_ChiNhanh
            LEFT JOIN (
                SELECT 
                    dn1.ID_Phong,
                    dn1.soDienCu,
                    dn1.soDienMoi,
                    dn1.soNuocCu,
                    dn1.soNuocMoi,
                    dn1.thangNam
                FROM DienNuoc dn1
                WHERE dn1.thangNam = (
                    SELECT MAX(dn2.thangNam)
                    FROM DienNuoc dn2
                    WHERE dn2.ID_Phong = dn1.ID_Phong
                )
            ) dn ON p.ID_Phong = dn.ID_Phong
        WHERE hdon.trangThai IS NOT NULL
        ORDER BY hdon.ngayTao DESC
        """;
    
    private DoanhThu mapFullResult(ResultSet rs) throws SQLException {
        DoanhThu dt = new DoanhThu();
        dt.setIdHoaDon(rs.getInt("ID_HoaDon"));
        dt.setTenPhong(rs.getString("tenPhong"));
        dt.setTenNguoiDung(rs.getString("tenNguoiDung"));
        dt.setTenChiNhanh(rs.getString("tenChiNhanh"));
        dt.setSoDienCu(rs.getInt("soDienCu"));
        dt.setSoDienMoi(rs.getInt("soDienMoi"));
        dt.setSoNuocCu(rs.getInt("soNuocCu"));
        dt.setSoNuocMoi(rs.getInt("soNuocMoi"));
        dt.setSoDienDaDung(rs.getInt("soDienDaDung"));
        dt.setSoNuocDaDung(rs.getInt("soNuocDaDung"));
        dt.setTienDien(rs.getBigDecimal("tienDien"));
        dt.setTienNuoc(rs.getBigDecimal("tienNuoc"));
        dt.setTienPhong(rs.getBigDecimal("tienPhong"));
        dt.setTongTien(rs.getBigDecimal("tongTien"));
        dt.setTrangThaiThanhToan(rs.getString("trangThaiThanhToan"));
        dt.setNgayThanhToan(rs.getTimestamp("ngayThanhToan"));
        dt.setThangNamDienNuoc(rs.getDate("thangNamDienNuoc"));
        dt.setGiaDien(rs.getInt("giaDien"));
        dt.setGiaNuoc(rs.getInt("giaNuoc"));
        return dt;
    }
    
    private DoanhThu mapSimpleResult(ResultSet rs) throws SQLException {
        DoanhThu dt = new DoanhThu();
        dt.setIdHoaDon(rs.getInt("ID_HoaDon"));
        dt.setTenPhong(rs.getString("tenPhong"));
        dt.setTienDien(rs.getBigDecimal("tienDien"));
        dt.setTienNuoc(rs.getBigDecimal("tienNuoc"));
        dt.setTienPhong(rs.getBigDecimal("tienPhong"));
        dt.setTongTien(rs.getBigDecimal("tongTien"));
        dt.setNgayThanhToan(rs.getTimestamp("ngayThanhToan"));
        dt.setTrangThaiThanhToan(rs.getString("trangThaiThanhToan"));
        return dt;
    }

    @Override
    public List<DoanhThu> getAll() {
        List<DoanhThu> list = new ArrayList<>();
        try (ResultSet rs = XJdbc.executeQuery(QUERY_FULL)) {
            while (rs.next()) {
                list.add(mapFullResult(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lấy tất cả doanh thu: " + e.getMessage(), e);
        }
        return list;
    }
    
    @Override
    public List<DoanhThu> getAllSimple() {
        List<DoanhThu> list = new ArrayList<>();
        try (ResultSet rs = XJdbc.executeQuery(QUERY_SIMPLE)) {
            while (rs.next()) {
                list.add(mapSimpleResult(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lấy doanh thu đơn giản: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<DoanhThu> getByDateRange(Date tuNgay, Date denNgay) {
        List<DoanhThu> list = new ArrayList<>();
        // Dùng QUERY_SIMPLE (LEFT JOIN) để không làm mất bản ghi khi thiếu dòng Điện/Nước phù hợp
        String sql = QUERY_SIMPLE.replace("WHERE hdon.trangThai IS NOT NULL",
            "WHERE hdon.trangThai IS NOT NULL AND hdon.ngayTao BETWEEN ? AND ?");
        
        try {
            java.sql.Timestamp start = new java.sql.Timestamp(tuNgay.getTime());
            java.sql.Timestamp end = new java.sql.Timestamp(denNgay.getTime());
            
            try (ResultSet rs = XJdbc.executeQuery(sql, start, end)) {
                while (rs.next()) {
                    list.add(mapSimpleResult(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi truy vấn theo ngày: " + e.getMessage(), e);
        }
        return list;
    }
    
    @Override
    public List<DoanhThu> getByStatus(String trangThai) {
        List<DoanhThu> list = new ArrayList<>();
        String sql = QUERY_FULL + " AND hdon.trangThai = ?";
        
        try (ResultSet rs = XJdbc.executeQuery(sql, trangThai)) {
            while (rs.next()) {
                list.add(mapFullResult(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi truy vấn theo trạng thái: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public DoanhThu getById(int idHoaDon) {
        String sql = QUERY_FULL + " AND hdon.ID_HoaDon = ?";
        try (ResultSet rs = XJdbc.executeQuery(sql, idHoaDon)) {
            if (rs.next()) {
                return mapFullResult(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lấy doanh thu theo ID: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean updateStatus(int idHoaDon, String trangThai) {
        String sql = "UPDATE HoaDon SET trangThai = ? WHERE ID_HoaDon = ?";
        int rows = XJdbc.executeUpdate(sql, trangThai, idHoaDon);
        return rows > 0;
    }
}
