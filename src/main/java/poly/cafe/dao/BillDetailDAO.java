package poly.cafe.dao;

import java.util.List;
import poly.cafe.entity.BillDetail;

/**
 *
 * @author phupham
 */
public interface BillDetailDAO  extends CrudDAO<BillDetail, Long> {
    List<BillDetail> findByBillId(Long billId);
    List<BillDetail> findByDrinkId(String drinkId);
}
