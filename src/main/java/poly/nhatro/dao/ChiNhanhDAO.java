package poly.nhatro.dao;

import poly.nhatro.entity.ChiNhanh;
import java.math.BigDecimal;
import java.util.List;

public interface ChiNhanhDAO {
    List<ChiNhanh> getAll();
    boolean add(ChiNhanh chiNhanh);
    boolean update(ChiNhanh chiNhanh);
    boolean delete(int id);
    List<ChiNhanh> search(String keyword);
    
    ChiNhanh getById(int id);
    BigDecimal getGiaDienById(int id);
    BigDecimal getGiaNuocById(int id);
    List<String> getAllBranchNames();
    int layIDTheoTen(String tenChiNhanh);
}