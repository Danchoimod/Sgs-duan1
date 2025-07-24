package poly.nhatro.service;

import poly.nhatro.entity.ChiNhanh;
import java.util.List;

public interface ChiNhanhService {
    List<ChiNhanh> getAll();
    boolean add(ChiNhanh chiNhanh);
    boolean update(ChiNhanh chiNhanh);
    boolean delete(int id);
    List<ChiNhanh> search(String keyword);
}