package poly.nhatro.service;

import poly.nhatro.dao.NguoiThueDAO;
import poly.nhatro.dao.impl.NguoiThueDAOImpl;
import poly.nhatro.entity.NguoiThue;
import java.util.List;

public class NguoiThueService {
    private final NguoiThueDAO dao = new NguoiThueDAOImpl();
    public List<NguoiThue> findAll() {
        return dao.findAll();
    }
} 