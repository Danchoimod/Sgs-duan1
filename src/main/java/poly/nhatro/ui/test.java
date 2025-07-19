/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.nhatro.ui;

import poly.nhatro.entity.Phong;
import java.util.List;
import poly.nhatro.entity.NguoiThue;
import poly.nhatro.util.HttpUtils;
import poly.nhatro.util.SupabaseConfig;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import java.time.LocalDateTime;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;
import poly.nhatro.dao.impl.PhongDAOImpl;
import poly.nhatro.service.PhongService;
import com.google.gson.FieldNamingPolicy;
import poly.nhatro.service.NguoiThueService;

/**
 *
 * @author tranthithuyngan
 */
public class test {
    public static void main(String[] args) {
        PhongService phongService = new PhongService();
        List<Phong> danhSachPhong = phongService.layPhongTrong();
        System.out.println("Danh sách phòng trống:");
        for (Phong p : danhSachPhong) {
            System.out.println(p);
        }
        // --- In tất cả phòng ---
        List<Phong> tatCaPhong = new PhongDAOImpl().findAll();
        System.out.println("Tất cả phòng:");
        for (Phong p : tatCaPhong) {
            System.out.println(p);
        }
        // --- Truy vấn người thuê qua service ---
        NguoiThueService nguoiThueService = new NguoiThueService();
        List<NguoiThue> danhSachNguoiThue = nguoiThueService.findAll();
        System.out.println("Danh sách người thuê:");
        for (NguoiThue nt : danhSachNguoiThue) {
            System.out.println(nt);
        }
    }

    // Custom TypeAdapter cho LocalDateTime
    public static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        @Override
        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_DATE_TIME);
        }
    }
}
