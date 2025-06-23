package poly.cafe.dao;

import poly.cafe.entity.User;

/**
 *
 * @author phupham
 */
public interface UserDAO extends CrudDAO<User, String> {
    public void DeleteById(String id);
}
