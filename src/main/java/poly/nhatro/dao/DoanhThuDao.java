package poly.nhatro.dao;

import java.util.Date;
import java.util.List;
import poly.nhatro.entity.DoanhThu;

/**
 *
 * @author Admin
 */
public interface DoanhThuDao extends CrudDao<DoanhThu, Integer> {
    
    List<DoanhThu> getAll();
    
    List<DoanhThu> getAllSimple();
    
    List<DoanhThu> getByDateRange(Date tuNgay, Date denNgay);
    
    // Lấy doanh thu theo trạng thái thanh toán
    List<DoanhThu> getByStatus(String trangThai);
    
    // Lấy doanh thu theo ID hóa đơn
    DoanhThu getById(int idHoaDon);
    
    // Cập nhật trạng thái thanh toán
    boolean updateStatus(int idHoaDon, String trangThai);
}
