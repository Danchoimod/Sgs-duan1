package poly.nhatro.dao;

import java.util.List;
import poly.nhatro.entity.DienNuoc;

public interface DienNuocDAO extends CrudDao<DienNuoc, Integer>{
    /**
     * Finds a list of DienNuoc records by branch ID.
     * @param idChiNhanh The ID of the branch to filter by.
     * @return A list of DienNuoc objects belonging to that branch.
     */
    List<DienNuoc> findByChiNhanhId(Integer idChiNhanh);

    /**
     * Finds the Phong ID based on the room number.
     * @param soPhong The room number to search for.
     * @return The Phong ID if found, otherwise null.
     */
    Integer findPhongIdBySoPhong(String soPhong);
}