package poly.nhatro.service;

import java.util.List;
import poly.nhatro.dao.ChiNhanhDAO;
import poly.nhatro.dao.impl.ChiNhanhImpl;
import poly.nhatro.entity.ChiNhanh;

public class ChiNhanhService {
    private ChiNhanhDAO chiNhanhDAO;
    
    public ChiNhanhService() {
        this.chiNhanhDAO = new ChiNhanhImpl();
    }
    
    public void insert(ChiNhanh chiNhanh) {
        chiNhanhDAO.insert(chiNhanh);
    }
    
    public void update(ChiNhanh chiNhanh) {
        chiNhanhDAO.update(chiNhanh);
    }
    
    public void delete(Integer id) {
        chiNhanhDAO.delete(id);
    }
    
    public ChiNhanh findById(Integer id) {
        return chiNhanhDAO.findById(id);
    }
    
    public List<ChiNhanh> findAll() {
        return chiNhanhDAO.findAll();
    }
    
    public List<ChiNhanh> findByKeyword(String keyword) {
        return chiNhanhDAO.findByKeyword(keyword);
    }
}