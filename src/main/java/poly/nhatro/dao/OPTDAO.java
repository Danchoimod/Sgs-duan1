/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package poly.nhatro.dao;

import java.util.Optional;
import poly.nhatro.entity.OTP;

/**
 *
 * @author THACH VAN BACH
 */
public interface OPTDAO {

    boolean insert(OTP otp);

    Optional<OTP> findValidOtpByEmail(String email, int timeoutInSeconds);
    OTP findByEmailAndMaOTP(String email, String maOtp);
    void deleteById(int id);
    void deleteByEmail(String email);
}
