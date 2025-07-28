/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.nhatro.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import poly.nhatro.dao.DoanhThuDao;
import poly.nhatro.entity.DoanhThu;
import poly.nhatro.entity.HoaDon; // This import seems unused in the provided snippet
import poly.nhatro.util.XJdbc;

/**
 *
 * @author Admin
 */
public class DoanhThuImpl implements DoanhThuDao {
 private DoanhThu mapResult(ResultSet rs) throws Exception {
        DoanhThu dt = new DoanhThu();
        dt.setIdHoaDon(rs.getInt("ID_HoaDon"));
        dt.setSoDienMoi(rs.getInt("soDienMoi"));   // Thêm các trường này
        dt.setSoNuocMoi(rs.getInt("soNuocMoi"));   // Thêm các trường này
        dt.setSoDienCu(rs.getInt("soDienCu"));     // Thêm các trường này
        dt.setSoNuocCu(rs.getInt("soNuocCu"));     // Thêm các trường này
        dt.setTienDien(rs.getBigDecimal("tienDien"));
        dt.setTienNuoc(rs.getBigDecimal("tienNuoc"));
        dt.setTienPhong(rs.getBigDecimal("tienPhong"));
        dt.setTongTien(rs.getBigDecimal("tongTien"));
        dt.setNgayThanhToan(rs.getDate("ngayTao")); // Đã sửa từ ngayThanhToan -> ngayTao
        dt.setTrangThai(rs.getBoolean("trangThai")); // Đã sửa từ trangThaiThanhToan -> trangThai
        dt.setIdHopDong(rs.getInt("ID_HopDong")); // Thêm trường này
        return dt;
    }

    @Override
    public List<DoanhThu> getAll() {
        List<DoanhThu> list = new ArrayList<>();
        try {
            // Đã sửa tên bảng từ HOA_DON -> HoaDon
            // Đã sửa tên cột từ NgayThanhToan -> ngayTao
            String sql = "SELECT * FROM HoaDon ORDER BY ngayTao DESC";
            System.out.println("[DEBUG - DoanhThuImpl] Executing getAll SQL: " + sql);
            ResultSet rs = XJdbc.executeQuery(sql);
            int rowCount = 0;
            while (rs.next()) {
                list.add(mapResult(rs));
                rowCount++;
            }
            System.out.println("[DEBUG - DoanhThuImpl] Number of rows fetched by getAll: " + rowCount);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lấy tất cả doanh thu: " + e.getMessage(), e);
        }
        System.out.println("[DEBUG - DoanhThuImpl] Returning list with size: " + list.size());
        return list;
    }

    @Override
    public List<DoanhThu> getByDateRange(Date tuNgay, Date denNgay) {
        List<DoanhThu> list = new ArrayList<>();
        try {
            // Đã sửa tên bảng từ HOA_DON -> HoaDon
            // Đã sửa tên cột từ ngayThanhToan -> ngayTao
            String sql = "SELECT * FROM HoaDon WHERE ngayTao BETWEEN ? AND ? ORDER BY ngayTao DESC";

            java.sql.Timestamp start = new java.sql.Timestamp(tuNgay.getTime());
            java.sql.Timestamp end = new java.sql.Timestamp(denNgay.getTime());

            System.out.println("[DEBUG - DoanhThuImpl] Executing getByDateRange SQL: " + sql);
            System.out.println("[DEBUG - DoanhThuImpl] Params: " + start + " - " + end);

            ResultSet rs = XJdbc.executeQuery(sql, start, end);
            int rowCount = 0;
            while (rs.next()) {
                list.add(mapResult(rs));
                rowCount++;
            }
            System.out.println("[DEBUG - DoanhThuImpl] Number of rows fetched by getByDateRange: " + rowCount);

            System.out.println("[DEBUG - DoanhThuImpl] Found " + list.size() + " records");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi truy vấn theo ngày: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public DoanhThu getById(int idHoaDon) {
        // Đã sửa tên bảng từ HOA_DON -> HoaDon
        String sql = "SELECT * FROM HoaDon WHERE ID_HoaDon = ?";
        try {
            System.out.println("[DEBUG - DoanhThuImpl] Executing getById SQL: " + sql + " with ID: " + idHoaDon);
            ResultSet rs = XJdbc.executeQuery(sql, idHoaDon);
            if (rs.next()) {
                System.out.println("[DEBUG - DoanhThuImpl] Found record for ID: " + idHoaDon);
                return mapResult(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lấy doanh thu theo ID: " + e.getMessage(), e);
        }
        System.out.println("[DEBUG - DoanhThuImpl] No record found for ID: " + idHoaDon);
        return null; // Trả về null nếu không tìm thấy
    }

    public boolean update(DoanhThu dt) {
        // Đã sửa tên bảng từ HOA_DON -> HoaDon
        // Đã sửa tên cột từ NgayThanhToan -> ngayTao, trangThaiThanhToan -> trangThai
        String sql = """
        UPDATE HoaDon
        SET soDienMoi = ?, soNuocMoi = ?, soDienCu = ?, soNuocCu = ?,
            TienDien = ?, TienNuoc = ?, TienPhong = ?, TongTien = ?, ngayTao = ?, trangThai = ?, ID_HopDong = ?
        WHERE ID_HoaDon = ?
        """;
        System.out.println("[DEBUG - DoanhThuImpl] Executing update SQL for ID: " + dt.getIdHoaDon());

        int rows = XJdbc.executeUpdate(sql,
                dt.getSoDienMoi(),
                dt.getSoNuocMoi(),
                dt.getSoDienCu(),
                dt.getSoNuocCu(),
                dt.getTienDien(),
                dt.getTienNuoc(),
                dt.getTienPhong(),
                dt.getTongTien(),
                new java.sql.Date(dt.getNgayThanhToan().getTime()), // Chuyển đổi từ java.util.Date sang java.sql.Date
                dt.isTrangThai(),
                dt.getIdHopDong(),
                dt.getIdHoaDon()
        );
        System.out.println("[DEBUG - DoanhThuImpl] Rows affected by update: " + rows);
        return rows > 0;
    }
}
