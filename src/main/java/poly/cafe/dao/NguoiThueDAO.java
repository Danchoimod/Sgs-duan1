package poly.cafe.dao;

import poly.cafe.entity.NguoiThue;
import java.util.List;
import java.util.UUID;

public interface NguoiThueDAO {
    List<NguoiThue> findAll();
    NguoiThue findById(UUID id);
    void insert(NguoiThue nguoiThue);
    void update(NguoiThue nguoiThue);
    void delete(UUID id);
} 