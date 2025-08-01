package poly.nhatro.dao.impl;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import poly.nhatro.dao.CrudDao;
import poly.nhatro.dao.DienNuocDAO;
import poly.nhatro.entity.DienNuoc;
import poly.nhatro.util.XJdbc;
import poly.nhatro.util.XQuery;
import java.util.Date;
import java.util.Calendar;

public class DienNuocDAOImpl implements DienNuocDAO, CrudDao<DienNuoc, Integer>{

    // Định nghĩa các câu lệnh SQL dưới dạng hằng số chuỗi
    String createSql = "INSERT INTO DienNuoc (soDienCu, soDienMoi, soNuocCu, soNuocMoi, thangNam, ID_Phong) VALUES (?, ?, ?, ?, ?, ?)";
    String updateSql = "UPDATE DienNuoc SET soDienCu = ?, soDienMoi = ?, soNuocCu = ?, soNuocMoi = ?, thangNam = ?, ID_Phong = ? WHERE ID_DienNuoc = ?";
    String deleteSql = "DELETE FROM DienNuoc WHERE ID_DienNuoc = ?";
    
    // Cập nhật câu lệnh SELECT để JOIN với bảng Phong và ChiNhanh để lấy soPhong, tenChiNhanh, giaDien, giaNuoc, giaPhong
    // QUAN TRỌNG: Thêm AS idDienNuoc và AS idPhong để XQuery có thể ánh xạ đúng vào thuộc tính
    String findAllSql = "SELECT dn.ID_DienNuoc AS idDienNuoc, dn.soDienCu, dn.soDienMoi, dn.soNuocCu, dn.soNuocMoi, dn.thangNam, dn.ID_Phong AS idPhong, " +
                        "p.soPhong, p.giaPhong AS giaPhong, cn.tenChiNhanh, cn.giaDien AS giaDienChiNhanh, cn.giaNuoc AS giaNuocChiNhanh " +
                        "FROM DienNuoc dn " +
                        "JOIN Phong p ON dn.ID_Phong = p.ID_Phong " +
                        "JOIN ChiNhanh cn ON p.ID_ChiNhanh = cn.ID_ChiNhanh";
    
    String findByIdSql = "SELECT dn.ID_DienNuoc AS idDienNuoc, dn.soDienCu, dn.soDienMoi, dn.soNuocCu, dn.soNuocMoi, dn.thangNam, dn.ID_Phong AS idPhong, " +
                         "p.soPhong, p.giaPhong AS giaPhong, cn.tenChiNhanh, cn.giaDien AS giaDienChiNhanh, cn.giaNuoc AS giaNuocChiNhanh " +
                         "FROM DienNuoc dn " +
                         "JOIN Phong p ON dn.ID_Phong = p.ID_Phong " +
                         "JOIN ChiNhanh cn ON p.ID_ChiNhanh = cn.ID_ChiNhanh " +
                         "WHERE dn.ID_DienNuoc = ?";

    @Override
    public DienNuoc create(DienNuoc entity) {
        try {
            String sql = "INSERT INTO DienNuoc (soDienCu, soDienMoi, soNuocCu, soNuocMoi, thangNam, ID_Phong) VALUES (?, ?, ?, ?, ?, ?)";
            java.sql.PreparedStatement stmt = XJdbc.openConnection().prepareStatement(sql, java.sql.PreparedStatement.RETURN_GENERATED_KEYS);
            
            stmt.setInt(1, entity.getSoDienCu());
            stmt.setInt(2, entity.getSoDienMoi());
            stmt.setInt(3, entity.getSoNuocCu());
            stmt.setInt(4, entity.getSoNuocMoi());
            stmt.setDate(5, new java.sql.Date(entity.getThangNam().getTime()));
            stmt.setInt(6, entity.getIdPhong());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (java.sql.ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        entity.setIdDienNuoc(rs.getInt(1));
                        System.out.println("Thêm DienNuoc thành công với ID: " + entity.getIdDienNuoc());
                    }
                }
            }
            
            return entity;
        } catch (java.sql.SQLException ex) {
            System.err.println("Lỗi khi thêm DienNuoc: " + ex.getMessage());
            throw new RuntimeException("Error when creating DienNuoc: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void update(DienNuoc entity) {
        try {
            int rowsAffected = XJdbc.executeUpdate(updateSql,
                    entity.getSoDienCu(),
                    entity.getSoDienMoi(),
                    entity.getSoNuocCu(),
                    entity.getSoNuocMoi(),
                    entity.getThangNam(),
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
        String sql = "SELECT dn.ID_DienNuoc AS idDienNuoc, dn.soDienCu, dn.soDienMoi, dn.soNuocCu, dn.soNuocMoi, dn.thangNam, dn.ID_Phong AS idPhong, " +
                     "p.soPhong, p.giaPhong AS giaPhong, cn.tenChiNhanh, cn.giaDien AS giaDienChiNhanh, cn.giaNuoc AS giaNuocChiNhanh " +
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

    @Override
    public DienNuoc findByPhongThangNam(Integer idPhong, int thang, int nam) {
        String sql = "SELECT dn.ID_DienNuoc AS idDienNuoc, dn.soDienCu, dn.soDienMoi, dn.soNuocCu, dn.soNuocMoi, dn.thangNam, dn.ID_Phong AS idPhong, " +
                     "p.soPhong, p.giaPhong AS giaPhong, cn.tenChiNhanh, cn.giaDien AS giaDienChiNhanh, cn.giaNuoc AS giaNuocChiNhanh " +
                     "FROM DienNuoc dn " +
                     "JOIN Phong p ON dn.ID_Phong = p.ID_Phong " +
                     "JOIN ChiNhanh cn ON p.ID_ChiNhanh = cn.ID_ChiNhanh " +
                     "WHERE dn.ID_Phong = ? AND MONTH(dn.thangNam) = ? AND YEAR(dn.thangNam) = ?";
        try {
            return XQuery.getSingleBean(DienNuoc.class, sql, idPhong, thang, nam);
        } catch (RuntimeException ex) {
            System.err.println("Lỗi khi tìm DienNuoc theo Phòng, Tháng, Năm: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public DienNuoc findLatestByPhong(Integer idPhong) {
        String sql = "SELECT TOP 1 dn.ID_DienNuoc AS idDienNuoc, dn.soDienCu, dn.soDienMoi, dn.soNuocCu, dn.soNuocMoi, dn.thangNam, dn.ID_Phong, " +
                     "p.soPhong, p.giaPhong AS giaPhong, cn.tenChiNhanh, cn.giaDien AS giaDienChiNhanh, cn.giaNuoc AS giaNuocChiNhanh " +
                     "FROM DienNuoc dn " +
                     "JOIN Phong p ON dn.ID_Phong = p.ID_Phong " +
                     "JOIN ChiNhanh cn ON p.ID_ChiNhanh = cn.ID_ChiNhanh " +
                     "WHERE dn.ID_Phong = ? " +
                     "ORDER BY dn.thangNam DESC";
        try {
            return XQuery.getSingleBean(DienNuoc.class, sql, idPhong);
        } catch (RuntimeException ex) {
            System.err.println("Lỗi khi tìm DienNuoc mới nhất theo Phòng: " + ex.getMessage());
            // Return null instead of throwing exception if no record found
            return null;
        }
    }
}
