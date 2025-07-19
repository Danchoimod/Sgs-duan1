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
public class Phong {
    private UUID maPhong;
    private String tenPhong;
    private BigDecimal giaPhong;
    private boolean dangTrong;
    private UUID maChiNhanh;
}
