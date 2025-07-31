/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.nhatro.entity;

import lombok.*;

/**
 *
 * @author THACH VAN BACH
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OTP {
    private int id;
    private String maOtp;
    private String email;
    private java.sql.Timestamp ngayTao;
}
