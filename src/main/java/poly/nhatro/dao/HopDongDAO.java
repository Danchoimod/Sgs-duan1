package poly.nhatro.dao;

import poly.nhatro.entity.HopDong;
import java.util.List;

public interface HopDongDAO {
    void add(HopDong entity);
    void update(HopDong entity);
    void delete(String id);
    HopDong getById(String id);
    List<HopDong> getAll();
}