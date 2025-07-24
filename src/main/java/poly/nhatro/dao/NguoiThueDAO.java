
package poly.nhatro.dao;

import java.util.List;
import poly.nhatro.entity.NguoiThue;

/**
 *
 * @author tranthuyngan
 */
public interface NguoiThueDAO {
    List<NguoiThue> findAll();
    NguoiThue findById(int id);
}
