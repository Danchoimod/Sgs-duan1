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
    String createSql = "INSERT INTO HoaDon(trangThai, ngayTao, ID_NguoiDung, ID_Phong, ID_HopDong, ID_ChiNhanh) VALUES(?, ?, ?, ?, ?, ?)";
    String updateSql = "UPDATE HoaDon SET trangThai = ?, ngayTao = ?, ID_NguoiDung = ?, ID_Phong = ?, ID_HopDong = ?, ID_ChiNhanh = ? WHERE ID_HoaDon = ?";
    String deleteSql = "DELETE FROM HoaDon WHERE ID_HoaDon = ?";
    String findAllSql = "SELECT " +
            "hdon.ID_HoaDon, " +
            "hdon.trangThai, " +
            "hdon.ngayTao, " +
            "hdon.ID_NguoiDung, " +
            "hdon.ID_Phong, " +
            "hdon.ID_HopDong, " +
            "hdon.ID_ChiNhanh, " +
            "nd.tenNguoiDung, " +
            "p.soPhong, " +
            "dn.soDienCu, " +
            "dn.soDienMoi, " +
            "dn.soNuocCu, " +
            "dn.soNuocMoi, " +
            "(dn.soDienMoi - dn.soDienCu) AS soDien, " +
            "(dn.soNuocMoi - dn.soNuocCu) AS soNuoc, " +
            "(dn.soDienMoi - dn.soDienCu) * cn.giaDien AS tienDien, " +
            "(dn.soNuocMoi - dn.soNuocCu) * cn.giaNuoc AS tienNuoc, " +
            "p.giaPhong AS tienPhong, " +
            "((dn.soDienMoi - dn.soDienCu) * cn.giaDien + " +
            " (dn.soNuocMoi - dn.soNuocCu) * cn.giaNuoc + " +
            " p.giaPhong) AS tongTien, " +
            "hdon.trangThai AS trangThaiThanhToan " +
            "FROM HoaDon hdon " +
            "JOIN HopDong hd ON hdon.ID_HopDong = hd.ID_HopDong " +
            "JOIN NguoiDung nd ON hd.ID_NguoiDung = nd.ID_NguoiDung " +
            "JOIN Phong p ON hd.ID_Phong = p.ID_Phong " +
            "JOIN DienNuoc dn ON p.ID_Phong = dn.ID_Phong " +
            "JOIN ChiNhanh cn ON p.ID_ChiNhanh = cn.ID_ChiNhanh";
    String findByIdSql = "SELECT " +
            "hdon.ID_HoaDon, " +
            "hdon.trangThai, " +
            "hdon.ngayTao, " +
            "hdon.ID_NguoiDung, " +
            "hdon.ID_Phong, " +
            "hdon.ID_HopDong, " +
            "hdon.ID_ChiNhanh, " +
            "nd.tenNguoiDung, " +
            "p.soPhong, " +
            "dn.soDienCu, " +
            "dn.soDienMoi, " +
            "dn.soNuocCu, " +
            "dn.soNuocMoi, " +
            "(dn.soDienMoi - dn.soDienCu) AS soDien, " +
            "(dn.soNuocMoi - dn.soNuocCu) AS soNuoc, " +
            "(dn.soDienMoi - dn.soDienCu) * cn.giaDien AS tienDien, " +
            "(dn.soNuocMoi - dn.soNuocCu) * cn.giaNuoc AS tienNuoc, " +
            "p.giaPhong AS tienPhong, " +
            "((dn.soDienMoi - dn.soDienCu) * cn.giaDien + " +
            " (dn.soNuocMoi - dn.soNuocCu) * cn.giaNuoc + " +
            " p.giaPhong) AS tongTien, " +
            "hdon.trangThai AS trangThaiThanhToan " +
            "FROM HoaDon hdon " +
            "JOIN HopDong hd ON hdon.ID_HopDong = hd.ID_HopDong " +
            "JOIN NguoiDung nd ON hd.ID_NguoiDung = nd.ID_NguoiDung " +
            "JOIN Phong p ON hd.ID_Phong = p.ID_Phong " +
            "JOIN DienNuoc dn ON p.ID_Phong = dn.ID_Phong " +
            "JOIN ChiNhanh cn ON p.ID_ChiNhanh = cn.ID_ChiNhanh " +
            "WHERE hdon.ID_HoaDon = ?";
    String getChiNhanhIdByHopDongSql = "SELECT p.ID_ChiNhanh FROM HopDong hd JOIN Phong p ON hd.ID_Phong = p.ID_Phong WHERE hd.ID_HopDong = ?";

    @Override
    public List<HoaDon> findAll() {
        return XQuery.getBeanList(HoaDon.class, findAllSql);
    }


    @Override
    public HoaDon findById(int id) {
        return XQuery.getSingleBean(HoaDon.class, findByIdSql, id);
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
    public List<Object[]> getDetailedBillingData() {
        String sql = "SELECT " +
                "hdon.ID_HoaDon, " +
                "cn.tenChiNhanh, " +
                "p.soPhong, " +
                "nd.tenNguoiDung, " +
                "dn.soDienCu, " +
                "dn.soDienMoi, " +
                "dn.soNuocCu, " +
                "dn.soNuocMoi, " +
                "(dn.soDienMoi - dn.soDienCu) AS soDien, " +
                "(dn.soNuocMoi - dn.soNuocCu) AS soNuoc, " +
                "(dn.soDienMoi - dn.soDienCu) * cn.giaDien AS tienDien, " +
                "(dn.soNuocMoi - dn.soNuocCu) * cn.giaNuoc AS tienNuoc, " +
                "p.giaPhong AS tienPhong, " +
                "((dn.soDienMoi - dn.soDienCu) * cn.giaDien + " +
                " (dn.soNuocMoi - dn.soNuocCu) * cn.giaNuoc + " +
                " p.giaPhong) AS tongTien, " +
                "hdon.trangThai AS trangThaiThanhToan " +
                "FROM HopDong hd " +
                "JOIN NguoiDung nd ON hd.ID_NguoiDung = nd.ID_NguoiDung " +
                "JOIN Phong p ON hd.ID_Phong = p.ID_Phong " +
                "JOIN DienNuoc dn ON p.ID_Phong = dn.ID_Phong " +
                "JOIN ChiNhanh cn ON p.ID_ChiNhanh = cn.ID_ChiNhanh " +
                "JOIN HoaDon hdon ON hd.ID_HopDong = hdon.ID_HopDong";
        
        List<Object[]> resultList = new ArrayList<>();
        try (ResultSet rs = XJdbc.executeQuery(sql)) {
            while (rs.next()) {
                Object[] row = new Object[15];
                row[0] = rs.getInt("ID_HoaDon");
                row[1] = rs.getString("tenChiNhanh");
                row[2] = rs.getString("soPhong");
                row[3] = rs.getString("tenNguoiDung");
                row[4] = rs.getInt("soDienCu");
                row[5] = rs.getInt("soDienMoi");
                row[6] = rs.getInt("soNuocCu");
                row[7] = rs.getInt("soNuocMoi");
                row[8] = rs.getInt("soDien");
                row[9] = rs.getInt("soNuoc");
                row[10] = rs.getDouble("tienDien");
                row[11] = rs.getDouble("tienNuoc");
                row[12] = rs.getDouble("tienPhong");
                row[13] = rs.getDouble("tongTien");
                row[14] = rs.getString("trangThaiThanhToan");
                resultList.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tải dữ liệu hóa đơn chi tiết: " + e.getMessage(), e);
        }
        return resultList;
    }
    
    @Override
    public List<Object[]> getRoomsByChiNhanh(int chiNhanhId) {
        // Get all rooms that have existing hop dong (both active and inactive)
        String sql = "SELECT DISTINCT p.ID_Phong, p.soPhong " +
                     "FROM Phong p " +
                     "JOIN HopDong hd ON p.ID_Phong = hd.ID_Phong " +
                     "WHERE p.ID_ChiNhanh = ? " +
                     "ORDER BY p.soPhong";
        List<Object[]> rooms = new ArrayList<>();
        
        System.out.println("Executing SQL: " + sql + " with chiNhanhId: " + chiNhanhId);
        
        try (ResultSet rs = XJdbc.executeQuery(sql, chiNhanhId)) {
            while (rs.next()) {
                Object[] room = new Object[2];
                room[0] = rs.getInt("ID_Phong");
                room[1] = rs.getString("soPhong");
                rooms.add(room);
                System.out.println("Found room: ID=" + room[0] + ", Name=" + room[1]);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getRoomsByChiNhanh: " + e.getMessage());
            throw new RuntimeException("Lỗi khi lấy danh sách phòng theo chi nhánh: " + e.getMessage(), e);
        }
        
        System.out.println("Total rooms found: " + rooms.size());
        return rooms;
    }
    
    @Override
    public Object[] getRoomDetailData(int phongId) {
        String sql = "SELECT " +
                "nd.tenNguoiDung, " +
                "dn.soDienCu, dn.soDienMoi, " +
                "dn.soNuocCu, dn.soNuocMoi, " +
                "(dn.soDienMoi - dn.soDienCu) AS soDien, " +
                "(dn.soNuocMoi - dn.soNuocCu) AS soNuoc, " +
                "(dn.soDienMoi - dn.soDienCu) * cn.giaDien AS tienDien, " +
                "(dn.soNuocMoi - dn.soNuocCu) * cn.giaNuoc AS tienNuoc, " +
                "p.giaPhong AS tienPhong, " +
                "((dn.soDienMoi - dn.soDienCu) * cn.giaDien + " +
                " (dn.soNuocMoi - dn.soNuocCu) * cn.giaNuoc + " +
                " p.giaPhong) AS tongTien " +
                "FROM Phong p " +
                "JOIN HopDong hd ON p.ID_Phong = hd.ID_Phong " +
                "JOIN NguoiDung nd ON hd.ID_NguoiDung = nd.ID_NguoiDung " +
                "JOIN DienNuoc dn ON p.ID_Phong = dn.ID_Phong " +
                "JOIN ChiNhanh cn ON p.ID_ChiNhanh = cn.ID_ChiNhanh " +
                "WHERE p.ID_Phong = ?";
        
        try (ResultSet rs = XJdbc.executeQuery(sql, phongId)) {
            if (rs.next()) {
                Object[] data = new Object[11];
                data[0] = rs.getString("tenNguoiDung");
                data[1] = rs.getInt("soDienCu");
                data[2] = rs.getInt("soDienMoi");
                data[3] = rs.getInt("soNuocCu");
                data[4] = rs.getInt("soNuocMoi");
                data[5] = rs.getInt("soDien");
                data[6] = rs.getInt("soNuoc");
                data[7] = rs.getDouble("tienDien");
                data[8] = rs.getDouble("tienNuoc");
                data[9] = rs.getDouble("tienPhong");
                data[10] = rs.getDouble("tongTien");
                return data;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy thông tin chi tiết phòng: " + e.getMessage(), e);
        }
        return null;
    }
    
    @Override
    public List<Object[]> getDetailedBillingDataByStatus(String trangThai) {
        String sql = "SELECT " +
                "hdon.ID_HoaDon, " +
                "cn.tenChiNhanh, " +
                "p.soPhong, " +
                "nd.tenNguoiDung, " +
                "dn.soDienCu, " +
                "dn.soDienMoi, " +
                "dn.soNuocCu, " +
                "dn.soNuocMoi, " +
                "(dn.soDienMoi - dn.soDienCu) AS soDien, " +
                "(dn.soNuocMoi - dn.soNuocCu) AS soNuoc, " +
                "(dn.soDienMoi - dn.soDienCu) * cn.giaDien AS tienDien, " +
                "(dn.soNuocMoi - dn.soNuocCu) * cn.giaNuoc AS tienNuoc, " +
                "p.giaPhong AS tienPhong, " +
                "((dn.soDienMoi - dn.soDienCu) * cn.giaDien + " +
                " (dn.soNuocMoi - dn.soNuocCu) * cn.giaNuoc + " +
                " p.giaPhong) AS tongTien, " +
                "hdon.trangThai AS trangThaiThanhToan " +
                "FROM HopDong hd " +
                "JOIN NguoiDung nd ON hd.ID_NguoiDung = nd.ID_NguoiDung " +
                "JOIN Phong p ON hd.ID_Phong = p.ID_Phong " +
                "JOIN DienNuoc dn ON p.ID_Phong = dn.ID_Phong " +
                "JOIN ChiNhanh cn ON p.ID_ChiNhanh = cn.ID_ChiNhanh " +
                "JOIN HoaDon hdon ON hd.ID_HopDong = hdon.ID_HopDong " +
                "WHERE hdon.trangThai = ?";
        
        List<Object[]> resultList = new ArrayList<>();
        try (ResultSet rs = XJdbc.executeQuery(sql, trangThai)) {
            while (rs.next()) {
                Object[] row = new Object[15];
                row[0] = rs.getInt("ID_HoaDon");
                row[1] = rs.getString("tenChiNhanh");
                row[2] = rs.getString("soPhong");
                row[3] = rs.getString("tenNguoiDung");
                row[4] = rs.getInt("soDienCu");
                row[5] = rs.getInt("soDienMoi");
                row[6] = rs.getInt("soNuocCu");
                row[7] = rs.getInt("soNuocMoi");
                row[8] = rs.getInt("soDien");
                row[9] = rs.getInt("soNuoc");
                row[10] = rs.getDouble("tienDien");
                row[11] = rs.getDouble("tienNuoc");
                row[12] = rs.getDouble("tienPhong");
                row[13] = rs.getDouble("tongTien");
                row[14] = rs.getString("trangThaiThanhToan");
                resultList.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tải dữ liệu hóa đơn theo trạng thái: " + e.getMessage(), e);
        }
        return resultList;
    }
    
}
