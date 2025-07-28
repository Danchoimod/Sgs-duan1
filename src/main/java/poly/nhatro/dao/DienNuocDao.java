
package poly.nhatro.dao;
import poly.nhatro.entity.DienNuoc;
import java.util.List;

/**
 *
 * @author THACH VAN BACH
 */
public interface DienNuocDao extends CrudDao<DienNuoc, Object>{
    public void insert(DienNuoc sdn);

    public void delete(int maSo);

    public DienNuoc selectById(int maSo);

    public List<DienNuoc> selectAll();
    
    List<DienNuoc> getAll();
    
    DienNuoc getById(int maso);
    
    List<DienNuoc> findByChiNhanhVaThangNam(int idChiNhanh, int thang, int nam);
    List<DienNuoc> timTheoChiNhanh(int idChiNhanh);
    public boolean kiemTraTrungThangNam(int idPhong, int thang, int nam);


}
