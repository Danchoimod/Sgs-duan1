
package poly.nhatro.entity;
import lombok.*;
/**
 *
 * @author THACH VAN BACH
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DienNuoc {
    private int maSo;
    private int thang;
    private int nam;
    private int soDien;
    private int soNuoc;
    private int idPhong;
        private int idChiNhanh;
}
