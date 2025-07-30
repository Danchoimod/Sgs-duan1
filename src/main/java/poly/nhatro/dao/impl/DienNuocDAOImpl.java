package poly.nhatro.dao.impl;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

import poly.nhatro.dao.CrudDao;
import poly.nhatro.dao.DienNuocDAO;
import poly.nhatro.entity.DienNuoc;
import poly.nhatro.util.XJdbc;
import poly.nhatro.util.XQuery;

public class DienNuocDAOImpl implements DienNuocDAO, CrudDao<DienNuoc, Integer>{
    // Định nghĩa các câu lệnh SQL dưới dạng hằng số chuỗi
    String createSql = "INSERT INTO DienNuoc (GiaDien, GiaNuoc, ID_Phong) VALUES (?, ?, ?)";
    String updateSql = "UPDATE DienNuoc SET GiaDien = ?, GiaNuoc = ?, ID_Phong = ? WHERE ID_DienNuoc = ?";
    String deleteSql = "DELETE FROM DienNuoc WHERE ID_DienNuoc = ?";
    // Cập nhật câu lệnh SELECT để JOIN với bảng Phong và ChiNhanh để lấy soPhong và tenChiNhanh
    // QUAN TRỌNG: Thêm AS idDienNuoc để XQuery có thể ánh xạ đúng vào thuộc tính idDienNuoc
    String findAllSql = "SELECT dn.ID_DienNuoc AS idDienNuoc, dn.GiaDien, dn.GiaNuoc, dn.ID_Phong, p.soPhong, cn.tenChiNhanh " +
                        "FROM DienNuoc dn " +
                        "JOIN Phong p ON dn.ID_Phong = p.ID_Phong " +
                        "JOIN ChiNhanh cn ON p.ID_ChiNhanh = cn.ID_ChiNhanh";
    String findByIdSql = "SELECT dn.ID_DienNuoc AS idDienNuoc, dn.GiaDien, dn.GiaNuoc, dn.ID_Phong, p.soPhong, cn.tenChiNhanh " +
                         "FROM DienNuoc dn " +
                         "JOIN Phong p ON dn.ID_Phong = p.ID_Phong " +
                         "JOIN ChiNhanh cn ON p.ID_ChiNhanh = cn.ID_ChiNhanh " +
                         "WHERE dn.ID_DienNuoc = ?";

    @Override
    public DienNuoc create(DienNuoc entity) {
        try {
            // Thay vì cố gắng lấy ID tự động tạo, chỉ thực hiện lệnh UPDATE
            XJdbc.executeUpdate(createSql,
                    entity.getGiaDien(),
                    entity.getGiaNuoc(),
                    entity.getIdPhong()
            );
            System.out.println("Thêm DienNuoc thành công. ID sẽ được hiển thị sau khi làm mới.");
            // ID của entity này sẽ không được cập nhật ngay lập tức nếu không có cơ chế lấy generated keys từ XJdbc
            return entity;
        } catch (RuntimeException ex) {
            System.err.println("Lỗi khi thêm DienNuoc: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void update(DienNuoc entity) {
        try {
            int rowsAffected = XJdbc.executeUpdate(updateSql,
                    entity.getGiaDien(),
                    entity.getGiaNuoc(),
                    entity.getIdPhong(),
                    entity.getIdDienNuoc()
            );
            if (rowsAffected > 0) {
                System.out.println("Cập nhật DienNuoc thành công: " + entity);
            } else {
                System.out.println("Không tìm thấy DienNuoc với ID: " + entity.getIdDienNuoc() + " để cập nhật.");
            }
        } catch (RuntimeException ex) {
            System.err.println("Lỗi khi cập nhật DienNuoc: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void deleteById(Integer id) {
        try {
            int rowsAffected = XJdbc.executeUpdate(deleteSql, id);
            if (rowsAffected > 0) {
                System.out.println("Xóa DienNuoc với ID " + id + " thành công.");
            } else {
                System.out.println("Không tìm thấy DienNuoc với ID " + id + " để xóa.");
            }
        } catch (RuntimeException ex) {
            System.err.println("Lỗi khi xóa DienNuoc: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<DienNuoc> findAll() {
        try {
            List<DienNuoc> list = XQuery.getBeanList(DienNuoc.class, findAllSql);
            System.out.println("Tìm thấy " + list.size() + " bản ghi DienNuoc.");
            return list;
        } catch (RuntimeException ex) {
            System.err.println("Lỗi khi tìm tất cả DienNuoc: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public DienNuoc findById(Integer id) {
        try {
            DienNuoc dn = XQuery.getSingleBean(DienNuoc.class, findByIdSql, id);
            if (dn != null) {
                System.out.println("Tìm thấy DienNuoc với ID " + id + ": " + dn);
            } else {
                System.out.println("Không tìm thấy DienNuoc với ID: " + id);
            }
            return dn;
        } catch (RuntimeException ex) {
            System.err.println("Lỗi khi tìm DienNuoc theo ID: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<DienNuoc> findByChiNhanhId(Integer idChiNhanh) {
        String sql = "SELECT dn.ID_DienNuoc AS idDienNuoc, dn.GiaDien, dn.GiaNuoc, dn.ID_Phong, p.soPhong, cn.tenChiNhanh " +
                     "FROM DienNuoc dn " +
                     "JOIN Phong p ON dn.ID_Phong = p.ID_Phong " +
                     "JOIN ChiNhanh cn ON p.ID_ChiNhanh = cn.ID_ChiNhanh " +
                     "WHERE p.ID_ChiNhanh = ?";
        try {
            List<DienNuoc> list = XQuery.getBeanList(DienNuoc.class, sql, idChiNhanh);
            System.out.println("Tìm thấy " + list.size() + " bản ghi DienNuoc cho chi nhánh ID " + idChiNhanh);
            return list;
        } catch (RuntimeException ex) {
            System.err.println("Lỗi khi tìm DienNuoc theo Chi nhánh ID: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public Integer findPhongIdBySoPhong(String soPhong) {
        String sql = "SELECT ID_Phong FROM Phong WHERE soPhong = ?";
        try {
            return XJdbc.getValue(sql, soPhong);
        } catch (RuntimeException ex) {
            System.err.println("Lỗi khi tìm ID_Phong theo soPhong: " + ex.getMessage());
            throw ex;
        }
    }
}