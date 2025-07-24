package poly.nhatro.service.impl;

import poly.nhatro.dao.ChiNhanhDAO;
import poly.nhatro.dao.impl.ChiNhanhDAOImpl;
import poly.nhatro.entity.ChiNhanh;
import poly.nhatro.service.ChiNhanhService;
import java.util.List;

public class ChiNhanhServiceImpl implements ChiNhanhService {
    private ChiNhanhDAO chiNhanhDAO;
    
    public ChiNhanhServiceImpl() {
        this.chiNhanhDAO = new ChiNhanhDAOImpl();
    }
    
    @Override
    public List<ChiNhanh> getAll() {
        return chiNhanhDAO.getAll();
    }
    
    @Override
    public boolean add(ChiNhanh chiNhanh) {
        return chiNhanhDAO.add(chiNhanh);
    }
    
    @Override
    public boolean update(ChiNhanh chiNhanh) {
        return chiNhanhDAO.update(chiNhanh);
    }
    
    @Override
    public boolean delete(int id) {
        return chiNhanhDAO.delete(id);
    }
    
    @Override
    public List<ChiNhanh> search(String keyword) {
        return chiNhanhDAO.search(keyword);
    }
}