package poly.nhatro.dao.impl;

import java.util.List;
import poly.nhatro.dao.HoaDonDAO;
import poly.nhatro.entity.HoaDon;

/**
 *
 * @author tranthithuyngan
 */
public class hoaDonImpl implements HoaDonDAO {
    String createSQL = "INSERT INTO HOA_DON(soDienMoi, soNuocMoi, soDienCu, soNuocCu, tienDien, tienNuoc, tienPhong, tongTien, trangThaiThanhToan, ngayThanhToan, ID_HopDong VALUES(?,?,?,?,?,?,?,?,?,?,?)";
    String updateSQL = "UPDATE HOA_DON SET soDienMoi = ?, soNuocMoi = ?, soNuocCu = ? , tienDien = ?, tienNuoc = ? , tienPhong = ?, tongTien = ? , trangThaiThanhToan = ? , ngayThanhToan = ?, ID_HopDong = ? WHERE ID_HoaDon = ?";
    String deleteSQL = "DELETE FROM HOA_DON WHERE ID_HoaDon = ?";
    String findAllSQL = "SELECT * FROM HOA_DON";
    String findByIdSQL = "SELECT * FROM HOA_DON WHERE ID_HoaDon = ?";
    
    @Override
    public List<HoaDon> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<HoaDon> selectAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public HoaDon findById(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void insert(HoaDon hoaDon) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(HoaDon hoaDon) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<HoaDon> findByTrangThai(boolean daThanhToan) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Object[]> findWithDetals() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
