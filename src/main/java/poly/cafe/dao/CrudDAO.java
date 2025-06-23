package poly.cafe.dao;

import java.util.List;

/**
 *
 * @author phupham
 */
public interface CrudDAO<T, ID> { //T là truyền entity, ID là truy vấn
    T create(T entity);
    void update(T entity);
    void DeleteById(ID id);
    List<T>findAll();
    T findById(ID id);
}
