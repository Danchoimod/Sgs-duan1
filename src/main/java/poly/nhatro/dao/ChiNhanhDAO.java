package poly.nhatro.dao;

import java.util.List;
import poly.nhatro.entity.ChiNhanh;

public interface ChiNhanhDAO {
    void insert(ChiNhanh chiNhanh);
    void update(ChiNhanh chiNhanh);
    void delete(Integer id);
    ChiNhanh findById(Integer id);
    List<ChiNhanh> findAll();
    List<ChiNhanh> findByKeyword(String keyword);
}