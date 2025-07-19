/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.cafe.dao;

import poly.cafe.entity.Phong;
import java.util.List;
import java.util.UUID;

public interface PhongDAO {
    List<Phong> findAll();
    Phong findById(UUID maPhong);
    void insert(Phong p);
    void update(Phong p);
    void delete(UUID maPhong);
}

