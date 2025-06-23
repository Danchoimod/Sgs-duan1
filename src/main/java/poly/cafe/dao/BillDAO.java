package poly.cafe.dao;

import java.util.Date;
import java.util.List;
import poly.cafe.entity.Bill;

/**
 *
 * @author phupham
 */
public interface BillDAO extends CrudDAO<Bill, Long>{
    List<Bill> findByUsername (String username);
    List<Bill> findByCardId (Integer cardId);

    public List<Bill> findByTimeRange(Date begin, Date end);
    public Bill findServicingByCardId(Integer cardId);
    List<Bill> findByUserAndTimeRange(String username, Date begin, Date end);
    
}
