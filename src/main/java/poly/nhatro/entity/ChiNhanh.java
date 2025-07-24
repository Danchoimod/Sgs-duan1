package poly.nhatro.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ChiNhanh {
    private int ID_ChiNhanh;
    private String tenChiNhanh;
    private String diaChi;
    private BigDecimal giaDien;
    private BigDecimal giaNuoc;
}