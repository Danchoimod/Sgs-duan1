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
import poly.nhatro.entity.HoaDon;
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
        dt.setNgayThanhToan(rs.getDate("ngayTao"));
        dt.setTrangThai(rs.getBoolean("trangThaiThanhToan"));
        dt.setIdHopDong(rs.getInt("ID_HopDong")); // Thêm trường này
        return dt;
    }

    @Override
    public List<DoanhThu> getAll() {
        List<DoanhThu> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery("SELECT * FROM HoaDon ORDER BY ngayTao DESC");
            while (rs.next()) {
                list.add(mapResult(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lấy tất cả doanh thu: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<DoanhThu> getByDateRange(Date tuNgay, Date denNgay) {
        List<DoanhThu> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM HoaDon WHERE ngayTao BETWEEN ? AND ? ORDER BY ngayTao DESC";

            java.sql.Timestamp start = new java.sql.Timestamp(tuNgay.getTime());
            java.sql.Timestamp end = new java.sql.Timestamp(denNgay.getTime());

            System.out.println("Executing SQL: " + sql);
            System.out.println("Params: " + start + " - " + end);

            ResultSet rs = XJdbc.executeQuery(sql, start, end);

            while (rs.next()) {
                list.add(mapResult(rs));
            }

            System.out.println("Found " + list.size() + " records");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi truy vấn theo ngày: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public DoanhThu getById(int idHoaDon) {
        String sql = "SELECT * FROM HoaDon WHERE ID_HoaDon = ?";
        try {
            ResultSet rs = XJdbc.executeQuery(sql, idHoaDon);
            if (rs.next()) {
                return mapResult(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lấy doanh thu theo ID: " + e.getMessage(), e);
        }
        return null; // Trả về null nếu không tìm thấy
    }

    public boolean update(DoanhThu dt) {
        String sql = """
        UPDATE HoaDon
        SET soDienMoi = ?, soNuocMoi = ?, soDienCu = ?, soNuocCu = ?,
            TienDien = ?, TienNuoc = ?, TienPhong = ?, TongTien = ?, ngayTao = ?, trangThaiThanhToan = ?, ID_HopDong = ?
        WHERE ID_HoaDon = ?
        """;

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

        return rows > 0;
    }
}
