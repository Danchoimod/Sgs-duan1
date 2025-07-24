package poly.nhatro.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import poly.nhatro.dao.ChiNhanhDAO;
import poly.nhatro.entity.ChiNhanh;
import poly.nhatro.util.XJdbc;

public class ChiNhanhImpl implements ChiNhanhDAO {

    @Override
    public void insert(ChiNhanh chiNhanh) {
        String sql = "INSERT INTO CHI_NHANH(tenChiNhanh, diaChi, giaDien, giaNuoc) VALUES(?,?,?,?)";
        XJdbc.executeUpdate(sql,
                chiNhanh.getTenChiNhanh(),
                chiNhanh.getDiaChi(),
                chiNhanh.getGiaDien(),
                chiNhanh.getGiaNuoc());
    }

    @Override
    public void update(ChiNhanh chiNhanh) {
        String sql = "UPDATE CHI_NHANH SET tenChiNhanh=?, diaChi=?, giaDien=?, giaNuoc=? WHERE ID_ChiNhanh=?";
        XJdbc.executeUpdate(sql,
                chiNhanh.getTenChiNhanh(),
                chiNhanh.getDiaChi(),
                chiNhanh.getGiaDien(),
                chiNhanh.getGiaNuoc(),
                chiNhanh.getId_ChiNhanh());
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM CHI_NHANH WHERE ID_ChiNhanh=?";
        XJdbc.executeUpdate(sql, id);
    }

    @Override
    public ChiNhanh findById(Integer id) {
        String sql = "SELECT * FROM CHI_NHANH WHERE ID_ChiNhanh=?";
        List<ChiNhanh> list = select(sql, id);
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public List<ChiNhanh> findAll() {
        String sql = "SELECT * FROM CHI_NHANH";
        return select(sql);
    }

    @Override
    public List<ChiNhanh> findByKeyword(String keyword) {
        String sql = "SELECT * FROM CHI_NHANH WHERE tenChiNhanh LIKE ?";
        return select(sql, "%" + keyword + "%");
    }

    private List<ChiNhanh> select(String sql, Object... args) {
        List<ChiNhanh> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = XJdbc.executeQuery(sql, args);
                while (rs.next()) {
                    ChiNhanh cn = new ChiNhanh();
                    cn.setId_ChiNhanh(rs.getInt("ID_ChiNhanh"));
                    cn.setTenChiNhanh(rs.getString("tenChiNhanh"));
                    cn.setDiaChi(rs.getString("diaChi"));
                    cn.setGiaDien(rs.getDouble("giaDien"));
                    cn.setGiaNuoc(rs.getDouble("giaNuoc"));
                    list.add(cn);
                }
            } finally {
                if (rs != null) {
                    rs.getStatement().getConnection().close();
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }
}