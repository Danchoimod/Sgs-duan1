/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.nhatro.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import poly.nhatro.entity.OTP;
import poly.nhatro.dao.OPTDAO;
import poly.nhatro.util.XJdbc;

/**
 *
 * @author THACH VAN BACH
 */
public class OTPDAOImpl implements OPTDAO {

    @Override
    public boolean insert(OTP otp) {
        String sql = "INSERT INTO OTP(maOtp, email, ngayTao) VALUES (?, ?, ?)";
        try (Connection con = XJdbc.openConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, otp.getMaOtp());
            ps.setString(2, otp.getEmail());
            ps.setTimestamp(3, otp.getNgayTao());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<OTP> findValidOtpByEmail(String email, int timeoutInSeconds) {
        String sql = "SELECT TOP 1 * FROM OTP WHERE email = ? AND DATEDIFF(SECOND, ngayTao, GETDATE()) <= ? ORDER BY ngayTao DESC";
        try (Connection con = XJdbc.openConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setInt(2, timeoutInSeconds);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                OTP otp = new OTP(
                        rs.getInt("ID_OTP"),
                        rs.getString("maOtp"),
                        rs.getString("email"),
                        rs.getTimestamp("ngayTao")
                );
                return Optional.of(otp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public OTP findByEmailAndMaOTP(String email, String otpCode) {
        String sql = "SELECT * FROM OTP WHERE email = ? AND maOtp = ?";
        try (Connection con = XJdbc.openConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, otpCode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return OTP.builder()
                        .id(rs.getInt("ID_OTP"))
                        .maOtp(rs.getString("maOtp"))
                        .email(rs.getString("email"))
                        .ngayTao(rs.getTimestamp("ngayTao"))
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM OTP WHERE ID_OTP = ?";
        try (Connection con = XJdbc.openConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteByEmail(String email) {
        String sql = "DELETE FROM OTP WHERE email = ?";
        try (Connection con = XJdbc.openConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
