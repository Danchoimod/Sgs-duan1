package poly.nhatro.dao;

import java.util.List;
import poly.nhatro.entity.HoaDon;

/**
 *
 * @author tranthithuyngan
 */
public interface HoaDonDAO {
/**
     * Lấy tất cả hóa đơn từ database
     * @return List<HoaDon> danh sách tất cả hóa đơn
     */
    List<HoaDon> findAll();
    
    List<HoaDon> selectAll();
    
    HoaDon findById(int id);
    void insert(HoaDon hoaDon);
    void update(HoaDon hoaDon);
    void delete(int id);
    List<HoaDon> findByTrangThai(boolean daThanhToan);
    List<Object[]> findWithDetals();
            
    
    
};
