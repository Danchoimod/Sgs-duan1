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

    String createSql = "INSERT INTO HoaDon(trangThai, ngayTao, ID_NguoiDung, ID_Phong, ID_HopDong, ID_ChiNhanh) VALUES(?, ?, ?, ?, ?, ?)";
    String updateSql = "UPDATE HoaDon SET trangThai = ?, ngayTao = ?, ID_NguoiDung = ?, ID_Phong = ?, ID_HopDong = ?, ID_ChiNhanh = ? WHERE ID_HoaDon = ?";
    String deleteSql = "DELETE FROM HoaDon WHERE ID_HoaDon = ?";
    String findAllSql = "SELECT * FROM HoaDon";
    String findById = "SELECT * FROM HoaDon WHERE ID_HoaDon = ?";
    String findByTrangThai = "SELECT * FROM HoaDon WHERE trangThai = ?";
    String selectWithDetailsSql = "SELECT "
            + "hd.ID_HoaDon, hd.trangThai, hd.ngayTao, hd.ID_NguoiDung, hd.ID_Phong, "
            + "hd.ID_HopDong, hd.ID_ChiNhanh, "
            + "hop.ngayBatDau, hop.ngayKetThuc, "
            + "p.soPhong, p.giaPhong, "
            + "cn.tenChiNhanh, "
            + "nd.tenNguoiDung, nd.soDienThoai "
            + "FROM HoaDon hd "
            + "JOIN HopDong hop ON hd.ID_HopDong = hop.ID_HopDong "
            + "JOIN Phong p ON hop.ID_Phong = p.ID_Phong "
            + "JOIN ChiNhanh cn ON hop.ID_ChiNhanh = cn.ID_ChiNhanh "
            + "JOIN NguoiDung nd ON hop.ID_NguoiDung = nd.ID_NguoiDung";

    
    // Tương tự cho selectDetailsByIdSql
    String selectDetailsByIdSql = "SELECT "
            + "hd.ID_HoaDon, hd.soDienMoi, hd.soNuocMoi, hd.soDienCu, hd.soNuocCu, "
            + "hd.tienDien, hd.tienNuoc, hd.tienPhong, hd.tongTien, "
            + "hd.trangThai, hd.ngayTao, " // Đã sửa tên cột
            + "hop.ngayTao AS ngayBatDauHopDong, hop.thoiHan, " // Lấy ngayTao và thoiHan từ HopDong
            + "p.soPhong, p.giaPhong, "
            + "cn.tenChiNhanh, "

            + "nd.tenNguoiDung, nd.soDienThoai "
            + "FROM HoaDon hd "
            + "JOIN HopDong hop ON hd.ID_HopDong = hop.ID_HopDong "
            + "JOIN Phong p ON hop.ID_Phong = p.ID_Phong "
            + "JOIN ChiNhanh cn ON hop.ID_ChiNhanh = cn.ID_ChiNhanh "
            + "JOIN NguoiDung nd ON hop.ID_NguoiDung = nd.ID_NguoiDung "
            + "WHERE hd.ID_HoaDon = ?";
    
    String getHoTenSql = "SELECT nd.tenNguoiDung FROM HoaDon hd " +
                        "JOIN NguoiDung nd ON hd.ID_NguoiDung = nd.ID_NguoiDung " +
                        "WHERE hd.ID_HoaDon = ?";
    
    String getTenPhongSql = "SELECT p.soPhong FROM HoaDon hd " +
                           "JOIN Phong p ON hd.ID_Phong = p.ID_Phong " +
                           "WHERE hd.ID_HoaDon = ?";
    
    String getTenChiNhanhSql = "SELECT cn.tenChiNhanh FROM HoaDon hd " +
                              "JOIN ChiNhanh cn ON hd.ID_ChiNhanh = cn.ID_ChiNhanh " +
                              "WHERE hd.ID_HoaDon = ?";
    
    String getChiNhanhIdSql = "SELECT cn.ID_ChiNhanh FROM HoaDon hd " +
                              "JOIN ChiNhanh cn ON hd.ID_ChiNhanh = cn.ID_ChiNhanh " +
                              "WHERE hd.ID_HoaDon = ?";
    
    String getChiNhanhIdByHopDongSql = "SELECT p.ID_ChiNhanh FROM HopDong hd JOIN Phong p ON hd.ID_Phong = p.ID_Phong WHERE hd.ID_HopDong = ?";


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

            entity.getTrangThai(),
            entity.getNgayTao(),
            entity.getID_NguoiDung(),
            entity.getID_Phong(),

            entity.getID_HopDong(),
            entity.getID_ChiNhanh(),
            entity.getID_HoaDon());
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
                details[16] = rs.getString("tenNguoiDung");
                details[17] = rs.getString("soDienThoai");

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

                entity.getTrangThai(),
                entity.getNgayTao(),
                entity.getID_NguoiDung(),
                entity.getID_Phong(),
                entity.getID_HopDong(),
                entity.getID_ChiNhanh());

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

                details[16] = rs.getString("tenNguoiDung");
                details[17] = rs.getString("soDienThoai");

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

                return rs.getString("tenNguoiDung");

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

    @Override
    public List<HoaDon> findByTrangThai(String trangThai) {
        return XQuery.getBeanList(HoaDon.class, findByTrangThai);
    }
    
}
