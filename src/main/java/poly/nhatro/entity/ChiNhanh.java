package poly.nhatro.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ChiNhanh {
    private int ID_ChiNhanh; // ID tự tăng trong DB, không cần set từ code
    private String tenChiNhanh;
    private String diaChi;
    private BigDecimal giaDien; // DECIMAL hoặc NUMERIC trong SQL
    private BigDecimal giaNuoc; // DECIMAL hoặc NUMERIC trong SQL
}