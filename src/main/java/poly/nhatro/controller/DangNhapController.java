/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package poly.nhatro.controller;

import poly.nhatro.util.XDialog;

/**
 *
 * @author THACH VAN BACH
 */
public interface DangNhapController extends MainController{
    void login();
    default void exit(){
        if(XDialog.confirm("Bạn có chắc muốn thoát ?")){
            System.exit(0);
        }
    }
}
