package poly.cafe.dao;

import poly.cafe.entity.Card;

/**
 *
 * @author phupham
 */
public interface CardDAO extends CrudDAO<Card, Integer> {
   
     public void deleteById(String id);
     
}
