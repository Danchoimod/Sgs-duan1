package poly.nhatro.dao.impl;

import java.util.List;
import poly.nhatro.dao.NguoiThueDAO;
import poly.nhatro.entity.NguoiThue;
import poly.nhatro.util.XQuery;

/**
 *
 * @author Phu Pham
 */
public class NguoiThueDaoImpl implements NguoiThueDAO {

    private final String FIND_ALL = "SELECT * FROM NGUOI_THUE";
    private final String FIND_BY_ID = "SELECT * FROM NGUOI_THUE WHERE ID_NguoiThue = ?";
    
    @Override
    public List<NguoiThue> findAll() {
        return XQuery.getBeanList(NguoiThue.class, FIND_ALL);
    }
    
    @Override
    public NguoiThue findById(int id) {
        return XQuery.getSingleBean(NguoiThue.class, FIND_BY_ID, id);
    }

}
