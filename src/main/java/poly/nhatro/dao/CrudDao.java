package poly.nhatro.dao;

import java.util.List;

/**
 *
 * @author admin
 */
public interface CrudDao<T, ID>{
    T create(T entity);

    void update(T entity);

    void deleteById(ID id);
    
    List<T> findAll();

    T findById(ID id);
}
