package poly.nhatro.dao.impl;

import java.sql.*;
import java.util.*;
import poly.nhatro.entity.DienNuoc;
import poly.nhatro.dao.DienNuocDao;
import poly.nhatro.util.XJdbc;

/**
 *
 * @author THACH VAN BACH
 */
public class DienNuocDaoImpl implements DienNuocDao {

    @Override
    public void insert(DienNuoc sdn) {
        String sql = "INSERT INTO SODIENNUOC (thang, nam, soDien, soNuoc, ID_Phong, ID_ChiNhanh) VALUES (?, ?, ?, ?, ?, ?)";
        XJdbc.executeUpdate(sql,
                sdn.getThang(), sdn.getNam(), sdn.getSoDien(), sdn.getSoNuoc(),
                sdn.getIdPhong(), sdn.getIdChiNhanh()
        );
    }

    @Override
    public void update(DienNuoc sdn) {
        String sql = "UPDATE SODIENNUOC SET thang = ?, nam = ?, soDien = ?, soNuoc = ?, ID_Phong = ?, ID_ChiNhanh = ? WHERE ID_DienNuoc = ?";
        XJdbc.executeUpdate(sql,
                sdn.getThang(), sdn.getNam(), sdn.getSoDien(), sdn.getSoNuoc(),
                sdn.getIdPhong(), sdn.getIdChiNhanh(), sdn.getMaSo()
        );
    }

    @Override
    public void delete(int maso) {
        String sql = "DELETE FROM SODIENNUOC WHERE ID_DienNuoc = ?";
        XJdbc.executeUpdate(sql, maso);
    }

    @Override
    public List<DienNuoc> getAll() {
        return selectAll();
    }

    @Override
    public DienNuoc getById(int maso) {
        return selectById(maso);
    }

    @Override
    public List<DienNuoc> findByChiNhanhVaThangNam(int idChiNhanh, int thang, int nam) {
        String sql = "SELECT * FROM SODIENNUOC WHERE ID_ChiNhanh = ? AND thang = ? AND nam = ?";
        return select(sql, idChiNhanh, thang, nam);
    }

    @Override
    public DienNuoc create(DienNuoc sdn) {
        String sql = "INSERT INTO SODIENNUOC (thang, nam, soDien, soNuoc, ID_Phong, ID_ChiNhanh) VALUES (?, ?, ?, ?, ?, ?)";
        try (var conn = XJdbc.openConnection(); var ps = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, sdn.getThang());
            ps.setInt(2, sdn.getNam());
            ps.setInt(3, sdn.getSoDien());
            ps.setInt(4, sdn.getSoNuoc());
            ps.setInt(5, sdn.getIdPhong());
            ps.setInt(6, sdn.getIdChiNhanh());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        sdn.setMaSo(rs.getInt(1));
                        return sdn;
                    }
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Lỗi khi thêm mới số điện nước", ex);
        }
        return null;
    }

    @Override
    public void deleteById(Object id) {
        if (id instanceof Integer maso) {
            delete(maso);
        }
    }

    @Override
    public List<DienNuoc> findAll() {
        return selectAll();
    }

    @Override
    public DienNuoc findById(Object id) {
        if (id instanceof Integer maso) {
            return selectById(maso);
        }
        return null;
    }

    @Override
    public DienNuoc selectById(int maSo) {
        String sql = "SELECT * FROM SODIENNUOC WHERE ID_DienNuoc = ?";
        List<DienNuoc> list = select(sql, maSo);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<DienNuoc> selectAll() {
        String sql = "SELECT * FROM SODIENNUOC";
        return select(sql);
    }

    private List<DienNuoc> select(String sql, Object... args) {
        List<DienNuoc> list = new ArrayList<>();
        try (ResultSet rs = XJdbc.executeQuery(sql, args)) {
            while (rs.next()) {
                DienNuoc sdn = new DienNuoc();
                sdn.setMaSo(rs.getInt("ID_DienNuoc"));
                sdn.setThang(rs.getInt("thang"));
                sdn.setNam(rs.getInt("nam"));
                sdn.setSoDien(rs.getInt("soDien"));
                sdn.setSoNuoc(rs.getInt("soNuoc"));
                sdn.setIdPhong(rs.getInt("ID_Phong"));
                sdn.setIdChiNhanh(rs.getInt("ID_ChiNhanh"));
                list.add(sdn);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi truy vấn dữ liệu SODIENNUOC", e);
        }
        return list;
    }

    @Override
    public List<DienNuoc> timTheoChiNhanh(int idChiNhanh) {
        String sql = "SELECT * FROM SODIENNUOC WHERE ID_ChiNhanh = ?";
        List<DienNuoc> list = new ArrayList<>();
        try (ResultSet rs = XJdbc.executeQuery(sql, idChiNhanh)) {
            while (rs.next()) {
                DienNuoc dn = new DienNuoc();
                dn.setMaSo(rs.getInt("ID_DienNuoc"));
                dn.setIdPhong(rs.getInt("ID_Phong"));
                dn.setThang(rs.getInt("Thang"));
                dn.setNam(rs.getInt("Nam"));
                dn.setSoDien(rs.getInt("SoDien"));
                dn.setSoNuoc(rs.getInt("SoNuoc"));
                list.add(dn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public boolean kiemTraTrungThangNam(int idPhong, int thang, int nam) {
        String sql = "SELECT COUNT(*) FROM SODIENNUOC WHERE ID_PHONG = ? AND THANG = ? AND NAM = ?";
        Integer count = XJdbc.getValue(sql, idPhong, thang, nam); // Dùng hàm tiện ích sẵn có
        return count != null && count > 0;
    }

}
