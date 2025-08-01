package poly.nhatro.dao;

import java.util.List;
import poly.nhatro.entity.HoaDon;

/**
 *
 * @author tranthithuyngan
 */
public interface HoaDonDAO extends CrudDao<HoaDon, Integer> {
    List<HoaDon> findAll();
    
    HoaDon findById(int id);
    
    Integer getChiNhanhIdByHopDongId(int hopDongId);
            
    double getGiaPhongByHopDongId(int hopDongId);
    
    /**
     * Lấy danh sách chi tiết hóa đơn với đầy đủ thông tin từ các bảng liên quan
     * @return List<Object[]> danh sách chi tiết hóa đơn
     */
    List<Object[]> getDetailedBillingData();
    
    /**
     * Lấy danh sách phòng theo chi nhánh
     * @param chiNhanhId ID chi nhánh
     * @return List<Object[]> danh sách phòng [ID_Phong, soPhong]
     */
    List<Object[]> getRoomsByChiNhanh(int chiNhanhId);
    
    /**
     * Lấy thông tin chi tiết phòng để fill form
     * @param phongId ID phòng
     * @return Object[] thông tin chi tiết phòng
     */
    Object[] getRoomDetailData(int phongId);
    
    /**
     * Lấy danh sách chi tiết hóa đơn theo trạng thái thanh toán
     * @param trangThai trạng thái thanh toán
     * @return List<Object[]> danh sách chi tiết hóa đơn
     */
    List<Object[]> getDetailedBillingDataByStatus(String trangThai);
    
    /**
     * Lấy ID hợp đồng dựa trên ID phòng và ID người dùng
     * @param phongId ID phòng
     * @param nguoiDungId ID người dùng
     * @return ID hợp đồng hoặc -1 nếu không tìm thấy
     */
    int getHopDongIdByPhongAndNguoiDung(int phongId, int nguoiDungId);
    
};
