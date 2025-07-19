package poly.nhatro.dao.impl;

import poly.nhatro.dao.NguoiThueDAO;
import poly.nhatro.entity.NguoiThue;
import poly.nhatro.util.HttpUtils;
import poly.nhatro.util.SupabaseConfig;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class NguoiThueDAOImpl implements NguoiThueDAO {
    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new poly.nhatro.ui.test.LocalDateTimeAdapter())
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();

    @Override
    public List<NguoiThue> findAll() {
        String response = HttpUtils.sendGet(SupabaseConfig.BASE_URL + "/nguoi_thue?select=*", SupabaseConfig.API_KEY);
        return gson.fromJson(response, new TypeToken<List<NguoiThue>>(){}.getType());
    }
    @Override
    public NguoiThue findById(UUID id) {
        String url = SupabaseConfig.BASE_URL + "/nguoi_thue?ma_nguoi_thue=eq." + id + "&select=*";
        String response = HttpUtils.sendGet(url, SupabaseConfig.API_KEY);
        List<NguoiThue> list = gson.fromJson(response, new TypeToken<List<NguoiThue>>(){}.getType());
        return list.isEmpty() ? null : list.get(0);
    }
    @Override
    public void insert(NguoiThue nguoiThue) {
        java.util.Map<String, Object> insertFields = new java.util.HashMap<>();
        insertFields.put("ma_nguoi_thue", nguoiThue.getMaNguoiThue());
        insertFields.put("ho_ten", nguoiThue.getHoTen());
        insertFields.put("so_dien_thoai", nguoiThue.getSoDienThoai());
        insertFields.put("cmnd_cccd", nguoiThue.getCmndCccd());
        insertFields.put("email", nguoiThue.getEmail());
        insertFields.put("mat_khau", nguoiThue.getMatKhau());
        String insertJson = gson.toJson(insertFields);
        HttpUtils.sendPost(SupabaseConfig.BASE_URL + "/nguoi_thue", insertJson, SupabaseConfig.API_KEY);
    }
    @Override
    public void update(NguoiThue nguoiThue) {
        // Sử dụng DELETE + INSERT để đảm bảo cập nhật
        System.out.println("[DEBUG] Starting UPDATE for ID: " + nguoiThue.getMaNguoiThue());
        
        // Bước 1: Xóa record cũ
        String deleteUrl = SupabaseConfig.BASE_URL + "/nguoi_thue?ma_nguoi_thue=eq." + nguoiThue.getMaNguoiThue();
        System.out.println("[DEBUG] DELETE URL: " + deleteUrl);
        String deleteResponse = HttpUtils.sendDelete(deleteUrl, SupabaseConfig.API_KEY);
        System.out.println("[DEBUG] DELETE Response: " + deleteResponse);
        
        // Bước 2: Tạo record mới
        String insertUrl = SupabaseConfig.BASE_URL + "/nguoi_thue";
        java.util.Map<String, Object> insertFields = new java.util.HashMap<>();
        insertFields.put("ma_nguoi_thue", nguoiThue.getMaNguoiThue());
        insertFields.put("ho_ten", nguoiThue.getHoTen());
        insertFields.put("so_dien_thoai", nguoiThue.getSoDienThoai());
        insertFields.put("cmnd_cccd", nguoiThue.getCmndCccd());
        insertFields.put("email", nguoiThue.getEmail());
        insertFields.put("mat_khau", nguoiThue.getMatKhau());
        String insertJson = gson.toJson(insertFields);
        
        System.out.println("[DEBUG] INSERT URL: " + insertUrl);
        System.out.println("[DEBUG] INSERT BODY: " + insertJson);
        String insertResponse = HttpUtils.sendPost(insertUrl, insertJson, SupabaseConfig.API_KEY);
        System.out.println("[DEBUG] INSERT Response: " + insertResponse);
    }
    @Override
    public void delete(UUID id) {
        String url = SupabaseConfig.BASE_URL + "/nguoi_thue?ma_nguoi_thue=eq." + id;
        HttpUtils.sendDelete(url, SupabaseConfig.API_KEY);
    }
} 