package poly.nhatro.dao;

import poly.nhatro.entity.GopY;
import java.util.List;

public interface GopYDAO {
    // Phương thức tạo mới một góp ý
    void create(GopY entity); 

    // Phương thức cập nhật thông tin góp ý
    void update(GopY entity);

    // Phương thức xóa góp ý bằng ID
    void deleteById(Integer id);

    // Phương thức tìm góp ý bằng ID
    GopY findById(Integer id);

    // Phương thức lấy tất cả góp ý
    List<GopY> findAll();

    // Phương thức tìm góp ý theo ID chi nhánh (tên phương thức này phải khớp hoàn toàn)
    List<GopY> findByChiNhanh(Integer idChiNhanh);
}