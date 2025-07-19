/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.nhatro.service;

import poly.nhatro.dao.PhongDAO;
import poly.nhatro.dao.impl.PhongDAOImpl;
import poly.nhatro.entity.Phong;

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

