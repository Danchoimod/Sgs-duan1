/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package poly.nhatro.dao;

import java.util.Date;
import java.util.List;
import poly.nhatro.entity.DoanhThu;

/**
 *
 * @author Admin
 */
public interface DoanhThuDao {

    List<DoanhThu> getAll();

    List<DoanhThu> getByDateRange(Date tuNgay, Date denNgay);
    
    DoanhThu getById(int idHoaDon);
    
    boolean update(DoanhThu dt);
}
