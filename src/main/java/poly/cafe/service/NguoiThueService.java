package poly.cafe.service;

import poly.cafe.dao.NguoiThueDAO;
import poly.cafe.dao.impl.NguoiThueDAOImpl;
import poly.cafe.entity.NguoiThue;
import java.util.List;

public class NguoiThueService {
    private final NguoiThueDAO dao = new NguoiThueDAOImpl();
    public List<NguoiThue> findAll() {
        return dao.findAll();
    }
} 