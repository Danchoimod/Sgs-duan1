package poly.nhatro.dao;

import java.util.List;
import poly.nhatro.entity.HoaDon;

/**
 *
 * @author tranthithuyngan
 */
public interface HoaDonDAO extends CrudDao<HoaDon, Integer> {
/**
     * Lấy tất cả hóa đơn từ database
     * @return List<HoaDon> danh sách tất cả hóa đơn
     */
    List<HoaDon> findAll();
        
    HoaDon findById(int id);
    
    List<HoaDon> findByTrangThai(boolean daThanhToan);
    List<Object[]> selectWithDetals();
    
    /**
     * Lấy thông tin chi tiết hóa đơn theo ID
     * @param hoaDonId ID của hóa đơn
     * @return Object[] chứa thông tin chi tiết
     */
    Object[] getDetailsByHoaDonId(int hoaDonId);
    
    /**
     * Lấy họ tên khách hàng theo ID hóa đơn
     * @param hoaDonId ID của hóa đơn
     * @return Họ tên khách hàng
     */
    String getHoTenByHoaDonId(int hoaDonId);
    
    /**
     * Lấy tên phòng theo ID hóa đơn
     * @param hoaDonId ID của hóa đơn
     * @return Tên phòng
     */
    String getTenPhongByHoaDonId(int hoaDonId);
    
    /**
     * Lấy tên chi nhánh theo ID hóa đơn
     * @param hoaDonId ID của hóa đơn
     * @return Tên chi nhánh
     */
    String getTenChiNhanhByHoaDonId(int hoaDonId);
    
    /**
     * Lấy ID chi nhánh theo ID hóa đơn
     * @param hoaDonId ID của hóa đơn
     * @return ID chi nhánh
     */
    Integer getChiNhanhIdByHoaDonId(int hoaDonId);
    
    /**
     * Lấy ID chi nhánh theo ID hợp đồng
     * @param hopDongId ID của hợp đồng
     * @return ID chi nhánh
     */
    Integer getChiNhanhIdByHopDongId(int hopDongId);
            
    double getGiaPhongByHopDongId(int hopDongId);
    
    
};
