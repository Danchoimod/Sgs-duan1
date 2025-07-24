package poly.nhatro.dao;

import poly.nhatro.entity.ChiNhanh;
import java.math.BigDecimal;
import java.util.List;

public interface ChiNhanhDAO {
    /**
     * Lấy thông tin giá điện và giá nước theo ID chi nhánh
     * @param id ID của chi nhánh
     * @return ChiNhanh object chứa thông tin giá điện và giá nước
     */
    ChiNhanh getById(int id);
    
    /**
     * Lấy giá điện theo ID chi nhánh
     * @param id ID của chi nhánh
     * @return Giá điện (VND/kWh)
     */
    BigDecimal getGiaDienById(int id);
    
    /**
     * Lấy giá nước theo ID chi nhánh
     * @param id ID của chi nhánh
     */
    BigDecimal getGiaNuocById(int id);
    
    /**
     * Lấy danh sách tên tất cả chi nhánh
     * @return List<String> chứa tên các chi nhánh
     */
    List<String> getAllBranchNames();
}
