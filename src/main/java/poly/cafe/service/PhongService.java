/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.cafe.service;

import poly.cafe.dao.PhongDAO;
import poly.cafe.dao.impl.PhongDAOImpl;
import poly.cafe.entity.Phong;

import java.util.List;
import java.util.stream.Collectors;

public class PhongService {
    private final PhongDAO dao = new PhongDAOImpl();

    public List<Phong> layPhongTrong() {
        return dao.findAll().stream()
                  .filter(Phong::isDangTrong)
                  .collect(Collectors.toList());
    }
}

