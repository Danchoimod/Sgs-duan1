/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.cafe.entity;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HopDong {
    private UUID maHopDong;
    private UUID maNguoiThue;
    private UUID maPhong;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private BigDecimal tienCoc;
    private String trangThai;
}

