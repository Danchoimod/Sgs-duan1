/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.nhatro.dao.impl;

import java.math.BigDecimal;
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
        dt.setIdHoaDon(rs.getInt("ID_HoaDon")); // Kiểm tra tên cột
        dt.setTienDien(rs.getBigDecimal("tienDien"));
        dt.setTienNuoc(rs.getBigDecimal("tienNuoc"));
        dt.setTienPhong(rs.getBigDecimal("tienPhong"));
        dt.setTongTien(rs.getBigDecimal("tongTien"));
        dt.setNgayThanhToan(rs.getDate("ngayThanhToan")); // Đảm bảo viết thường
        dt.setTrangThai(rs.getBoolean("trangThaiThanhToan"));
        return dt;
    }

    @Override
    public List<DoanhThu> getAll() {
        List<DoanhThu> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery("SELECT * FROM HOA_DON ORDER BY NgayThanhToan DESC");
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
            String sql = "SELECT * FROM HOA_DON WHERE ngayThanhToan BETWEEN ? AND ? ORDER BY ngayThanhToan DESC";

            // Chuyển đổi Date sang Timestamp
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
}
