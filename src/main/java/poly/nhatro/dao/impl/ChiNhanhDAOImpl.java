package poly.nhatro.dao.impl;

import poly.nhatro.dao.ChiNhanhDAO;
import poly.nhatro.entity.ChiNhanh;
import poly.nhatro.util.XJdbc;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class ChiNhanhDAOImpl implements ChiNhanhDAO {
    
    @Override
    public ChiNhanh getById(int id) {
        String sql = "SELECT ID_ChiNhanh, tenChiNhanh, diaChi, giaDien, giaNuoc FROM CHI_NHANH WHERE ID_ChiNhanh = ?";
        
        try (ResultSet rs = XJdbc.executeQuery(sql, id)) {
            if (rs.next()) {
                ChiNhanh chiNhanh = new ChiNhanh();
                chiNhanh.setID_ChiNhanh(rs.getInt("ID_ChiNhanh"));
                chiNhanh.setTenChiNhanh(rs.getString("tenChiNhanh"));
                chiNhanh.setGiaDien(rs.getBigDecimal("giaDien"));
                chiNhanh.setGiaNuoc(rs.getBigDecimal("giaNuoc"));
                return chiNhanh;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public BigDecimal getGiaDienById(int id) {
        String sql = "SELECT giaDien FROM CHI_NHANH WHERE ID_ChiNhanh = ?";
        
        try (ResultSet rs = XJdbc.executeQuery(sql, id)) {
            if (rs.next()) {
                return rs.getBigDecimal("giaDien");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getGiaNuocById(int id) {
        String sql = "SELECT giaNuoc FROM CHI_NHANH WHERE ID_ChiNhanh = ?";
        
        try (ResultSet rs = XJdbc.executeQuery(sql, id)) {
            if (rs.next()) {
                return rs.getBigDecimal("giaNuoc");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public List<String> getAllBranchNames() {
        List<String> branchNames = new ArrayList<>();
        String sql = "SELECT tenChiNhanh FROM CHI_NHANH ORDER BY tenChiNhanh";
        
        try (ResultSet rs = XJdbc.executeQuery(sql)) {
            while (rs.next()) {
                branchNames.add(rs.getString("tenChiNhanh"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return branchNames;
    }
}
