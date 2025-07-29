/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package poly.nhatro.controller;

import javax.swing.JDialog;
import javax.swing.JFrame;
import poly.nhatro.ui.DangNhapDialog;
import poly.nhatro.ui.QuenMatKhauJDiaLog;

/**
 *
 * @author Phu Pham
 */
public interface MainController {
        default void showJDialog(JDialog dialog) {
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    default void showLoginJDialog(JFrame frame) {
        this.showJDialog(new DangNhapDialog(frame, true));
    }
    
    default void showQuenMatKhauJDialog(JFrame frame) {
        this.showJDialog(new QuenMatKhauJDiaLog(frame, true));
    }
    
}
