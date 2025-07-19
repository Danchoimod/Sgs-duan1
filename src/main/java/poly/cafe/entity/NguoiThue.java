/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.cafe.entity;


import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NguoiThue {
    private String maNguoiThue;
    private String hoTen;
    private String soDienThoai;
    private String cmndCccd;
    private LocalDateTime ngayTao;
}

