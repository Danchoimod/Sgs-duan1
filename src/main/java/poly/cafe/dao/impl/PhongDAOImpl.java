/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.cafe.dao.impl;

import poly.cafe.dao.PhongDAO;
import poly.cafe.entity.Phong;
import poly.cafe.util.HttpUtils;
import poly.cafe.util.SupabaseConfig;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import java.util.List;
import java.util.UUID;

public class PhongDAOImpl implements PhongDAO {
    @Override
    public List<Phong> findAll() {
        String response = HttpUtils.sendGet(SupabaseConfig.BASE_URL + "/phong?select=*", SupabaseConfig.API_KEY);
        Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
        return gson.fromJson(response, new TypeToken<List<Phong>>(){}.getType());
    }

    @Override
    public Phong findById(UUID maPhong) {
        // TODO: Implement logic
        return null;
    }

    @Override
    public void insert(Phong p) {
        // TODO: Implement logic
    }

    @Override
    public void update(Phong p) {
        // TODO: Implement logic
    }

    @Override
    public void delete(UUID maPhong) {
        // TODO: Implement logic
    }

    // Các hàm khác: insert, update, delete
}

