package poly.cafe.dao;

import poly.cafe.entity.Category;

/**
 *
 * @author phupham
 */
public interface CategoryDAO extends CrudDAO<Category, String> { //Category là object lớp truyền vào, String là kiểu dữ liệu của ID

    public void deleteById(String id);
    
}
