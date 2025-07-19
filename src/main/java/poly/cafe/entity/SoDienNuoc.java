/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.cafe.entity;

import lombok.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SoDienNuoc {
    private UUID maSo;
    private UUID maPhong;
    private int thang;
    private int nam;
    private int dienCu;
    private int dienMoi;
    private int nuocCu;
    private int nuocMoi;
}

