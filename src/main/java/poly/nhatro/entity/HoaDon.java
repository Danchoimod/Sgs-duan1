/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.nhatro.entity;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HoaDon {
    private UUID maHoaDon;
    private UUID maHopDong;
    private UUID maSo;
    private int thang;
    private int nam;
    private BigDecimal tienDien;
    private BigDecimal tienNuoc;
    private BigDecimal tienPhong;
    private BigDecimal tongTien;
    private boolean daThanhToan;
}
