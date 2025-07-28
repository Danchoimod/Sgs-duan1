package poly.nhatro.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import poly.nhatro.dao.CrudDao;
import poly.nhatro.dao.HoaDonDAO;
import poly.nhatro.entity.HoaDon;
import poly.nhatro.util.*;

/**
 *
 * @author tranthithuyngan
 */
public class hoaDonDAOImpl implements HoaDonDAO, CrudDao<HoaDon, Integer> {
    // Đã sửa tên bảng từ HOA_DON -> HoaDon
    // Đã sửa tên cột từ trangThaiThanhToan -> trangThai, ngayThanhToan -> ngayTao
    String createSql = "INSERT INTO HoaDon(soDienMoi, soNuocMoi, soDienCu, soNuocCu, tienDien, tienNuoc, tienPhong, tongTien, trangThai, ngayTao, ID_HopDong) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String updateSql = "UPDATE HoaDon SET soDienMoi = ?, soNuocMoi = ?, soDienCu = ?, soNuocCu = ?, tienDien = ?, tienNuoc = ?, tienPhong = ?, tongTien = ?, trangThai = ?, ngayTao = ?, ID_HopDong = ? WHERE ID_HoaDon = ?";
    String deleteSql = "DELETE FROM HoaDon WHERE ID_HoaDon = ?";
    String findAllSql = "SELECT * FROM HoaDon";
    String findById = "SELECT * FROM HoaDon WHERE ID_HoaDon = ?";
    String findByTrangThai = "SELECT * FROM HoaDon WHERE trangThai = ?"; // Đã sửa tên cột
    
    // Đã sửa tên bảng từ HOA_DON, HOP_DONG, PHONG, CHI_NHANH, NGUOI_DUNG -> HoaDon, HopDong, Phong, ChiNhanh, NguoiDung
    // Đã sửa tên cột để khớp với schema thực tế: ngayTao (HopDong), tenNguoiDung (NguoiDung), soDienThoai (NguoiDung)
    String selectWithDetailsSql = "SELECT "
            + "hd.ID_HoaDon, hd.soDienMoi, hd.soNuocMoi, hd.soDienCu, hd.soNuocCu, "
            + "hd.tienDien, hd.tienNuoc, hd.tienPhong, hd.tongTien, "
            + "hd.trangThai, hd.ngayTao, " // Đã sửa tên cột
            + "hop.ngayTao AS ngayBatDauHopDong, hop.thoiHan, " // Lấy ngayTao và thoiHan từ HopDong
            + "p.soPhong, p.giaPhong, "
            + "cn.tenChiNhanh, "
            + "nd.tenNguoiDung, nd.soDienThoai " // Đã sửa tên cột
            + "FROM HoaDon hd "
            + "JOIN HopDong hop ON hd.ID_HopDong = hop.ID_HopDong "
            + "JOIN Phong p ON hd.ID_Phong = p.ID_Phong "
            + "JOIN ChiNhanh cn ON hd.ID_ChiNhanh = cn.ID_ChiNhanh " // Đã sửa JOIN từ hop.ID_ChiNhanh -> hd.ID_ChiNhanh
            + "JOIN NguoiDung nd ON hd.ID_NguoiDung = nd.ID_NguoiDung"; // Đã sửa JOIN từ hop.ID_NguoiDung -> hd.ID_NguoiDung
    
    // Tương tự cho selectDetailsByIdSql
    String selectDetailsByIdSql = "SELECT "
            + "hd.ID_HoaDon, hd.soDienMoi, hd.soNuocMoi, hd.soDienCu, hd.soNuocCu, "
            + "hd.tienDien, hd.tienNuoc, hd.tienPhong, hd.tongTien, "
            + "hd.trangThai, hd.ngayTao, " // Đã sửa tên cột
            + "hop.ngayTao AS ngayBatDauHopDong, hop.thoiHan, " // Lấy ngayTao và thoiHan từ HopDong
            + "p.soPhong, p.giaPhong, "
            + "cn.tenChiNhanh, "
            + "nd.tenNguoiDung, nd.soDienThoai " // Đã sửa tên cột
            + "FROM HoaDon hd "
            + "JOIN HopDong hop ON hd.ID_HopDong = hop.ID_HopDong "
            + "JOIN Phong p ON hd.ID_Phong = p.ID_Phong "
            + "JOIN ChiNhanh cn ON hd.ID_ChiNhanh = cn.ID_ChiNhanh "
            + "JOIN NguoiDung nd ON hd.ID_NguoiDung = nd.ID_NguoiDung "
            + "WHERE hd.ID_HoaDon = ?";
    
    // Tương tự cho getHoTenSql
    String getHoTenSql = "SELECT nd.tenNguoiDung FROM HoaDon hd " + // Đã sửa tên bảng và cột
                        "JOIN NguoiDung nd ON hd.ID_NguoiDung = nd.ID_NguoiDung " +
                        "WHERE hd.ID_HoaDon = ?";
    
    // Tương tự cho getTenPhongSql
    String getTenPhongSql = "SELECT p.soPhong FROM HoaDon hd " + // Đã sửa tên bảng
                           "JOIN Phong p ON hd.ID_Phong = p.ID_Phong " +
                           "WHERE hd.ID_HoaDon = ?";
    
    // Tương tự cho getTenChiNhanhSql
    String getTenChiNhanhSql = "SELECT cn.tenChiNhanh FROM HoaDon hd " + // Đã sửa tên bảng
                              "JOIN ChiNhanh cn ON hd.ID_ChiNhanh = cn.ID_ChiNhanh " +
                              "WHERE hd.ID_HoaDon = ?";
    
    // Tương tự cho getChiNhanhIdSql
    String getChiNhanhIdSql = "SELECT cn.ID_ChiNhanh FROM HoaDon hd " + // Đã sửa tên bảng
                              "JOIN ChiNhanh cn ON hd.ID_ChiNhanh = cn.ID_ChiNhanh " +
                              "WHERE hd.ID_HoaDon = ?";
    
    // Tương tự cho getChiNhanhIdByHopDongSql
    String getChiNhanhIdByHopDongSql = "SELECT ID_ChiNhanh FROM HopDong WHERE ID_HopDong = ?"; // Đã sửa tên bảng

    @Override
    public List<HoaDon> findAll() {
        return XQuery.getBeanList(HoaDon.class, findAllSql);
    }


    @Override
    public HoaDon findById(int id) {
        return XQuery.getSingleBean(HoaDon.class, findById, id);
    }


    @Override
    public void update(HoaDon entity) {
        XJdbc.executeUpdate(updateSql, 
            entity.getSoDienMoi(),
            entity.getSoNuocMoi(),
            entity.getSoDienCu(),
            entity.getSoNuocCu(),
            entity.getTienDien(),
            entity.getTienNuoc(),
            entity.getTienPhong(),
            entity.getTongTien(),
            entity.isTrangThaiThanhToan(), // Giữ nguyên tên getter của entity
            entity.getNgayThanhToan(),    // Giữ nguyên tên getter của entity
            entity.getID_HopDong(),
            entity.getID_HoaDon());
    }


    @Override
    public List<HoaDon> findByTrangThai(boolean daThanhToan) {
        return XQuery.getBeanList(HoaDon.class, findByTrangThai, daThanhToan);
    }

    @Override
    public List<Object[]> selectWithDetals() {
        List<Object[]> detailsList = new ArrayList<>();
        try (ResultSet rs = XJdbc.executeQuery(selectWithDetailsSql)) {
            while (rs.next()) {
                Object[] details = new Object[18]; // Kích thước mảng cần được điều chỉnh nếu số cột thay đổi
                details[0] = rs.getInt("ID_HoaDon");
                details[1] = rs.getInt("soDienMoi");
                details[2] = rs.getInt("soNuocMoi");
                details[3] = rs.getInt("soDienCu");
                details[4] = rs.getInt("soNuocCu");
                details[5] = rs.getDouble("tienDien");
                details[6] = rs.getDouble("tienNuoc");
                details[7] = rs.getDouble("tienPhong");
                details[8] = rs.getDouble("tongTien");
                details[9] = rs.getBoolean("trangThai"); // Đã sửa tên cột
                details[10] = rs.getDate("ngayTao"); // Đã sửa tên cột
                details[11] = rs.getDate("ngayBatDauHopDong"); // Lấy từ alias
                // Cần tính toán ngayKetThuc từ ngayBatDauHopDong và thoiHan
                Date ngayBatDauHopDong = rs.getDate("ngayBatDauHopDong");
                int thoiHan = rs.getInt("thoiHan");
                if (ngayBatDauHopDong != null) {
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.setTime(ngayBatDauHopDong);
                    cal.add(java.util.Calendar.MONTH, thoiHan);
                    details[12] = cal.getTime(); // ngayKetThuc
                } else {
                    details[12] = null;
                }

                details[13] = rs.getString("soPhong");
                details[14] = rs.getDouble("giaPhong");
                details[15] = rs.getString("tenChiNhanh");
                details[16] = rs.getString("tenNguoiDung"); // Đã sửa tên cột
                details[17] = rs.getString("soDienThoai"); // Đã sửa tên cột
                detailsList.add(details);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi truy vấn dữ liệu chi tiết: " + e.getMessage(), e);
        }
        return detailsList;
    }
    
    @Override
    public HoaDon create(HoaDon entity) {
        XJdbc.executeUpdate(createSql,
                entity.getSoDienMoi(),
                entity.getSoNuocMoi(),
                entity.getSoDienCu(),
                entity.getSoNuocCu(),
                entity.getTienDien(),
                entity.getTienNuoc(),
                entity.getTienPhong(),
                entity.getTongTien(),
                entity.isTrangThaiThanhToan(), // Giữ nguyên tên getter của entity
                entity.getNgayThanhToan(),    // Giữ nguyên tên getter của entity
                entity.getID_HopDong());
        return entity;
    }

    @Override
    public void deleteById(Integer id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public HoaDon findById(Integer id) {
        return findById(id.intValue()); 
    }
    
    @Override
    public Object[] getDetailsByHoaDonId(int hoaDonId) {
        try (ResultSet rs = XJdbc.executeQuery(selectDetailsByIdSql, hoaDonId)) {
            if (rs.next()) {
                Object[] details = new Object[18]; // Kích thước mảng cần được điều chỉnh nếu số cột thay đổi
                details[0] = rs.getInt("ID_HoaDon");
                details[1] = rs.getInt("soDienMoi");
                details[2] = rs.getInt("soNuocMoi");
                details[3] = rs.getInt("soDienCu");
                details[4] = rs.getInt("soNuocCu");
                details[5] = rs.getDouble("tienDien");
                details[6] = rs.getDouble("tienNuoc");
                details[7] = rs.getDouble("tienPhong");
                details[8] = rs.getDouble("tongTien");
                details[9] = rs.getBoolean("trangThai"); // Đã sửa tên cột
                details[10] = rs.getDate("ngayTao"); // Đã sửa tên cột
                details[11] = rs.getDate("ngayBatDauHopDong"); // Lấy từ alias
                // Cần tính toán ngayKetThuc từ ngayBatDauHopDong và thoiHan
                Date ngayBatDauHopDong = rs.getDate("ngayBatDauHopDong");
                int thoiHan = rs.getInt("thoiHan");
                if (ngayBatDauHopDong != null) {
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.setTime(ngayBatDauHopDong);
                    cal.add(java.util.Calendar.MONTH, thoiHan);
                    details[12] = cal.getTime(); // ngayKetThuc
                } else {
                    details[12] = null;
                }
                details[13] = rs.getString("soPhong");
                details[14] = rs.getDouble("giaPhong");
                details[15] = rs.getString("tenChiNhanh");
                details[16] = rs.getString("tenNguoiDung"); // Đã sửa tên cột
                details[17] = rs.getString("soDienThoai"); // Đã sửa tên cột
                return details;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi truy vấn thông tin chi tiết hóa đơn: " + e.getMessage(), e);
        }
        return null;
    }
    
    @Override
    public String getHoTenByHoaDonId(int hoaDonId) {
        try (ResultSet rs = XJdbc.executeQuery(getHoTenSql, hoaDonId)) {
            if (rs.next()) {
                return rs.getString("tenNguoiDung"); // Đã sửa tên cột
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy họ tên khách hàng: " + e.getMessage(), e);
        }
        return null;
    }
    
    @Override
    public String getTenPhongByHoaDonId(int hoaDonId) {
        try (ResultSet rs = XJdbc.executeQuery(getTenPhongSql, hoaDonId)) {
            if (rs.next()) {
                return rs.getString("soPhong");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy tên phòng: " + e.getMessage(), e);
        }
        return null;
    }
    
@Override
    public String getTenChiNhanhByHoaDonId(int hoaDonId) {
        try (ResultSet rs = XJdbc.executeQuery(getTenChiNhanhSql, hoaDonId)) {
            if (rs.next()) {
                return rs.getString("tenChiNhanh");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy tên chi nhánh: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Integer getChiNhanhIdByHoaDonId(int hoaDonId) {
        try (ResultSet rs = XJdbc.executeQuery(getChiNhanhIdSql, hoaDonId)) {
            if (rs.next()) {
                return rs.getInt("ID_ChiNhanh");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy ID chi nhánh: " + e.getMessage(), e);
        }
        return null;
    }
    
    @Override
    public Integer getChiNhanhIdByHopDongId(int hopDongId) {
        try (ResultSet rs = XJdbc.executeQuery(getChiNhanhIdByHopDongSql, hopDongId)) {
            if (rs.next()) {
                return rs.getInt("ID_ChiNhanh");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy ID chi nhánh theo hợp đồng: " + e.getMessage(), e);
        }
        return null;
    }
    
    @Override
    public double getGiaPhongByHopDongId(int hopDongId) {
        // Đã sửa tên bảng từ HOP_DONG, PHONG -> HopDong, Phong
        String sql = "SELECT p.giaPhong FROM HopDong hd " +
                     "JOIN Phong p ON hd.ID_Phong = p.ID_Phong " +
                     "WHERE hd.ID_HopDong = ?";
        try (ResultSet rs = XJdbc.executeQuery(sql, hopDongId)) {
            if (rs.next()) {
                return rs.getDouble("giaPhong");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy giá phòng theo hợp đồng: " + e.getMessage(), e);
        }
        return 0;
    }
    
}
