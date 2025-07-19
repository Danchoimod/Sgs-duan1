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
public class ChiNhanh {
    private UUID maChiNhanh;
    private String tenChiNhanh;
    private String hinhAnh;
    private BigDecimal giaDien;
    private BigDecimal giaNuoc;
}


