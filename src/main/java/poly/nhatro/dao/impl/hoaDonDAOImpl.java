package poly.nhatro.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    String createSql = "INSERT INTO HOA_DON(soDienMoi, soNuocMoi, soDienCu, soNuocCu, tienDien, tienNuoc, tienPhong, tongTien, trangThaiThanhToan, ngayThanhToan, ID_HopDong) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String updateSql = "UPDATE HOA_DON SET soDienMoi = ?, soNuocMoi = ?, soDienCu = ?, soNuocCu = ?, tienDien = ?, tienNuoc = ?, tienPhong = ?, tongTien = ?, trangThaiThanhToan = ?, ngayThanhToan = ?, ID_HopDong = ? WHERE ID_HoaDon = ?";
    String deleteSql = "DELETE FROM HOA_DON WHERE ID_HoaDon = ?";
    String findAllSql = "SELECT * FROM HOA_DON";
    String findById = "SELECT * FROM HOA_DON WHERE ID_HoaDon = ?";
    String findByTrangThai = "SELECT * FROM HOA_DON WHERE trangThaiThanhToan = ?";
    String selectWithDetailsSql = "SELECT "
            + "hd.ID_HoaDon, hd.soDienMoi, hd.soNuocMoi, hd.soDienCu, hd.soNuocCu, "
            + "hd.tienDien, hd.tienNuoc, hd.tienPhong, hd.tongTien, "
            + "hd.trangThaiThanhToan, hd.ngayThanhToan, "
            + "hop.ngayBatDau, hop.ngayKetThuc, "
            + "p.soPhong, p.giaPhong, "
            + "cn.tenChiNhanh, "
            + "nd.hoVaTen, nd.sdt "
            + "FROM HOA_DON hd "
            + "JOIN HOP_DONG hop ON hd.ID_HopDong = hop.ID_HopDong "
            + "JOIN PHONG p ON hop.ID_Phong = p.ID_Phong "
            + "JOIN CHI_NHANH cn ON hop.ID_ChiNhanh = cn.ID_ChiNhanh "
            + "JOIN NGUOI_DUNG nd ON hop.ID_NguoiDung = nd.ID_NguoiDung";
    
    String selectDetailsByIdSql = "SELECT "
            + "hd.ID_HoaDon, hd.soDienMoi, hd.soNuocMoi, hd.soDienCu, hd.soNuocCu, "
            + "hd.tienDien, hd.tienNuoc, hd.tienPhong, hd.tongTien, "
            + "hd.trangThaiThanhToan, hd.ngayThanhToan, "
            + "hop.ngayBatDau, hop.ngayKetThuc, "
            + "p.soPhong, p.giaPhong, "
            + "cn.tenChiNhanh, "
            + "nd.hoVaTen, nd.sdt "
            + "FROM HOA_DON hd "
            + "JOIN HOP_DONG hop ON hd.ID_HopDong = hop.ID_HopDong "
            + "JOIN PHONG p ON hop.ID_Phong = p.ID_Phong "
            + "JOIN CHI_NHANH cn ON hop.ID_ChiNhanh = cn.ID_ChiNhanh "
            + "JOIN NGUOI_DUNG nd ON hop.ID_NguoiDung = nd.ID_NguoiDung "
            + "WHERE hd.ID_HoaDon = ?";
    
    String getHoTenSql = "SELECT nd.hoVaTen FROM HOA_DON hd " +
                        "JOIN HOP_DONG hop ON hd.ID_HopDong = hop.ID_HopDong " +
                        "JOIN NGUOI_DUNG nd ON hop.ID_NguoiDung = nd.ID_NguoiDung " +
                        "WHERE hd.ID_HoaDon = ?";
    
    String getTenPhongSql = "SELECT p.soPhong FROM HOA_DON hd " +
                           "JOIN HOP_DONG hop ON hd.ID_HopDong = hop.ID_HopDong " +
                           "JOIN PHONG p ON hop.ID_Phong = p.ID_Phong " +
                           "WHERE hd.ID_HoaDon = ?";
    
    String getTenChiNhanhSql = "SELECT cn.tenChiNhanh FROM HOA_DON hd " +
                              "JOIN HOP_DONG hop ON hd.ID_HopDong = hop.ID_HopDong " +
                              "JOIN CHI_NHANH cn ON hop.ID_ChiNhanh = cn.ID_ChiNhanh " +
                              "WHERE hd.ID_HoaDon = ?";
    
    String getChiNhanhIdSql = "SELECT cn.ID_ChiNhanh FROM HOA_DON hd " +
                              "JOIN HOP_DONG hop ON hd.ID_HopDong = hop.ID_HopDong " +
                              "JOIN CHI_NHANH cn ON hop.ID_ChiNhanh = cn.ID_ChiNhanh " +
                              "WHERE hd.ID_HoaDon = ?";
    
    String getChiNhanhIdByHopDongSql = "SELECT ID_ChiNhanh FROM HOP_DONG WHERE ID_HopDong = ?";

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
            entity.isTrangThaiThanhToan(),
            entity.getNgayThanhToan(),
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
                Object[] details = new Object[18];
                details[0] = rs.getInt("ID_HoaDon");
                details[1] = rs.getInt("soDienMoi");
                details[2] = rs.getInt("soNuocMoi");
                details[3] = rs.getInt("soDienCu");
                details[4] = rs.getInt("soNuocCu");
                details[5] = rs.getDouble("tienDien");
                details[6] = rs.getDouble("tienNuoc");
                details[7] = rs.getDouble("tienPhong");
                details[8] = rs.getDouble("tongTien");
                details[9] = rs.getBoolean("trangThaiThanhToan");
                details[10] = rs.getDate("ngayThanhToan");
                details[11] = rs.getDate("ngayBatDau");
                details[12] = rs.getDate("ngayKetThuc");
                details[13] = rs.getString("soPhong");
                details[14] = rs.getDouble("giaPhong");
                details[15] = rs.getString("tenChiNhanh");
                details[16] = rs.getString("hoVaTen");
                details[17] = rs.getString("sdt");
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
                entity.isTrangThaiThanhToan(),
                entity.getNgayThanhToan(),
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
                Object[] details = new Object[18];
                details[0] = rs.getInt("ID_HoaDon");
                details[1] = rs.getInt("soDienMoi");
                details[2] = rs.getInt("soNuocMoi");
                details[3] = rs.getInt("soDienCu");
                details[4] = rs.getInt("soNuocCu");
                details[5] = rs.getDouble("tienDien");
                details[6] = rs.getDouble("tienNuoc");
                details[7] = rs.getDouble("tienPhong");
                details[8] = rs.getDouble("tongTien");
                details[9] = rs.getBoolean("trangThaiThanhToan");
                details[10] = rs.getDate("ngayThanhToan");
                details[11] = rs.getDate("ngayBatDau");
                details[12] = rs.getDate("ngayKetThuc");
                details[13] = rs.getString("soPhong");
                details[14] = rs.getDouble("giaPhong");
                details[15] = rs.getString("tenChiNhanh");
                details[16] = rs.getString("hoVaTen");
                details[17] = rs.getString("sdt");
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
                return rs.getString("hoVaTen");
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
        String sql = "SELECT p.giaPhong FROM HOP_DONG hd " +
                     "JOIN PHONG p ON hd.ID_Phong = p.ID_Phong " +
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
