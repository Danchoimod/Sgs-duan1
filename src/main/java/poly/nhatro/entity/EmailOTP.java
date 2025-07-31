/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.nhatro.entity;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author THACH VAN BACH
 */
public class EmailOTP {

    private static final String EMAIL_GUI = "01228116641a@gmail.com"; // 👉 thay bằng email thật
    private static final String MAT_KHAU_EMAIL = "xros pprl ogcz lpsk"; // 👉 thay bằng app password Gmail

    public static void guiOTPQuaEmail(String emailNhan, String maOTP) {
        String tieuDe = "Mã OTP Xác Nhận Tài Khoản";
        String noiDung = "Xin chào,\n\nMã OTP của bạn là: " + maOTP + "\nVui lòng không chia sẻ mã này cho bất kỳ ai.\n\nTrân trọng.";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_GUI, MAT_KHAU_EMAIL);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_GUI));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailNhan));
            message.setSubject(tieuDe);
            message.setText(noiDung);

            Transport.send(message);
            System.out.println("Đã gửi OTP thành công đến " + emailNhan);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
