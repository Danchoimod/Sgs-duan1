package poly.nhatro.dao;

import poly.nhatro.entity.NguoiDung;
import java.util.List;

public interface NguoiDungDAO {
    List<NguoiDung> findAll();
    NguoiDung findById(int id);
    void insert(NguoiDung nguoiDung);
    void update(NguoiDung nguoiDung);
    void delete(int id);
    List<NguoiDung> findByVaiTro(String vaiTro);
    List<NguoiDung> findByTrangThai(String trangThai);
    List<NguoiDung> findAvailableForContract(); // Tìm người dùng chưa có hợp đồng hoạt động
}
