package poly.nhatro.util;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Service class để lấy danh sách tỉnh thành Việt Nam từ API
 * @author tranthuyngan
 */
@Slf4j
public class ProvinceApiService {
    
    private static final String API_URL = "https://provinces.open-api.vn/api/";
    
    /**
     * Model cho tỉnh thành sử dụng Lombok
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Province {
        private String code;
        private String name;
        
        @Override
        public String toString() {
            return name;
        }
    }
    
    /**
     * Lấy danh sách tất cả tỉnh thành từ API
     * @return List<Province> danh sách tỉnh thành
     */
    public static List<Province> getProvinces() {
        List<Province> provinces = new ArrayList<>();
        
        try {
            URL url = new URL(API_URL + "p/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                // Parse JSON response
                JSONArray jsonArray = new JSONArray(response.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject provinceObj = jsonArray.getJSONObject(i);
                    // Xử lý code có thể là String hoặc Integer
                    String code;
                    try {
                        code = provinceObj.getString("code");
                    } catch (Exception e) {
                        // Nếu code là số, chuyển thành String
                        code = String.valueOf(provinceObj.get("code"));
                    }
                    String name = provinceObj.getString("name");
                    provinces.add(new Province(code, name));
                }
                
                log.info("Đã tải thành công {} tỉnh thành từ API", provinces.size());
            } else {
                log.error("Lỗi khi gọi API: HTTP {}", responseCode);
            }
            
            connection.disconnect();
            
        } catch (Exception e) {
            log.error("Lỗi khi lấy danh sách tỉnh thành từ API: {}", e.getMessage());
            
            // Fallback: Sử dụng danh sách 34 tỉnh thành theo cơ cấu mới nếu API không hoạt động
            provinces.addAll(getDefaultProvinces());
            log.info("Sử dụng danh sách tỉnh thành mới với {} tỉnh", provinces.size());
        }
        
        return provinces;
    }
    
    /**
     * Danh sách tỉnh thành mặc định theo cơ cấu hành chính mới (34 tỉnh thành)
     * @return List<Province> danh sách tỉnh thành mặc định
     */
    private static List<Province> getDefaultProvinces() {
        List<Province> defaultProvinces = new ArrayList<>();
        
        // 5 Thành phố trực thuộc trung ương
        defaultProvinces.add(new Province("01", "Hà Nội"));
        defaultProvinces.add(new Province("79", "Thành phố Hồ Chí Minh"));
        defaultProvinces.add(new Province("31", "Hải Phòng"));
        defaultProvinces.add(new Province("48", "Đà Nẵng"));
        defaultProvinces.add(new Province("92", "Cần Thơ"));
        
        // 29 Tỉnh theo cơ cấu mới
        defaultProvinces.add(new Province("08", "Tuyên Quang"));
        defaultProvinces.add(new Province("10", "Lào Cai"));
        defaultProvinces.add(new Province("19", "Thái Nguyên"));
        defaultProvinces.add(new Province("25", "Phú Thọ"));
        defaultProvinces.add(new Province("27", "Bắc Ninh"));
        defaultProvinces.add(new Province("33", "Hưng Yên"));
        defaultProvinces.add(new Province("37", "Ninh Bình"));
        defaultProvinces.add(new Province("45", "Quảng Trị"));
        defaultProvinces.add(new Province("51", "Quảng Ngãi"));
        defaultProvinces.add(new Province("56", "Khánh Hòa"));
        defaultProvinces.add(new Province("68", "Lâm Đồng"));
        defaultProvinces.add(new Province("66", "Đắk Lắk"));
        defaultProvinces.add(new Province("64", "Gia Lai"));
        defaultProvinces.add(new Province("72", "Tây Ninh"));
        defaultProvinces.add(new Province("75", "Đồng Nai"));
        defaultProvinces.add(new Province("86", "Vĩnh Long"));
        defaultProvinces.add(new Province("87", "Đồng Tháp"));
        defaultProvinces.add(new Province("96", "Cà Mau"));
        defaultProvinces.add(new Province("89", "An Giang"));
        defaultProvinces.add(new Province("12", "Lai Châu"));
        defaultProvinces.add(new Province("11", "Điện Biên"));
        defaultProvinces.add(new Province("14", "Sơn La"));
        defaultProvinces.add(new Province("20", "Lạng Sơn"));
        defaultProvinces.add(new Province("22", "Quảng Ninh"));
        defaultProvinces.add(new Province("38", "Thanh Hóa"));
        defaultProvinces.add(new Province("40", "Nghệ An"));
        defaultProvinces.add(new Province("42", "Hà Tĩnh"));
        defaultProvinces.add(new Province("04", "Cao Bằng"));
        defaultProvinces.add(new Province("46", "Thừa Thiên Huế"));
        
        return defaultProvinces;
    }
    
    
    /**
     * Tìm tỉnh thành theo tên
     * @param provinces danh sách tỉnh thành
     * @param provinceName tên tỉnh thành cần tìm
     * @return Province nếu tìm thấy, null nếu không tìm thấy
     */
    public static Province findProvinceByName(List<Province> provinces, String provinceName) {
        if (provinceName == null || provinceName.trim().isEmpty()) {
            return null;
        }
        
        String searchName = provinceName.trim();
        
        // Thử tìm khớp chính xác trước
        Province exactMatch = provinces.stream()
                .filter(province -> province.getName().equalsIgnoreCase(searchName))
                .findFirst()
                .orElse(null);
                
        if (exactMatch != null) {
            return exactMatch;
        }
        
        // Nếu không tìm thấy, thử chuẩn hóa tên và tìm lại
        String normalizedSearchName = normalizeProvinceName(searchName);
        return provinces.stream()
                .filter(province -> normalizeProvinceName(province.getName()).equalsIgnoreCase(normalizedSearchName))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Chuẩn hóa tên tỉnh bằng cách loại bỏ tiền tố "Thành phố" và "Tỉnh"
     * @param provinceName Tên tỉnh gốc
     * @return Tên tỉnh đã chuẩn hóa
     */
    private static String normalizeProvinceName(String provinceName) {
        if (provinceName == null) {
            return "";
        }
        
        String normalized = provinceName.trim();
        
        // Loại bỏ tiền tố "Thành phố "
        if (normalized.startsWith("Thành phố ")) {
            normalized = normalized.substring("Thành phố ".length());
        }
        // Loại bỏ tiền tố "Tỉnh "
        else if (normalized.startsWith("Tỉnh ")) {
            normalized = normalized.substring("Tỉnh ".length());
        }
        
        return normalized.trim();
    }
    
    /**
     * Tìm tỉnh thành theo mã
     * @param provinces danh sách tỉnh thành
     * @param provinceCode mã tỉnh thành cần tìm
     * @return Province nếu tìm thấy, null nếu không tìm thấy
     */
    public static Province findProvinceByCode(List<Province> provinces, String provinceCode) {
        if (provinceCode == null || provinceCode.trim().isEmpty()) {
            return null;
        }
        
        return provinces.stream()
                .filter(province -> province.getCode().equals(provinceCode.trim()))
                .findFirst()
                .orElse(null);
    }
}
