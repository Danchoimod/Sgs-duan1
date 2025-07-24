package poly.nhatro.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import poly.nhatro.entity.NguoiThue;

/**
 * Lớp tiện ích hỗ trợ truy vấn và chuyển đổi sang đối tượng
 *
 * @author NghiemN
 * @version 1.0
 */
public class XQuery {

    /**
     * Truy vấn 1 đối tượng
     *
     * @param <B> kiểu của đối tượng cần chuyển đổi
     * @param beanClass lớp của đối tượng kết quả
     * @param sql câu lệnh truy vấn
     * @param values các giá trị cung cấp cho các tham số của SQL
     * @return kết quả truy vấn
     * @throws RuntimeException lỗi truy vấn
     */
    public static <B> B getSingleBean(Class<B> beanClass, String sql, Object... values) {
        List<B> list = XQuery.getBeanList(beanClass, sql, values);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * Truy vấn nhiều đối tượng
     *
     * @param <B> kiểu của đối tượng cần chuyển đổi
     * @param beanClass lớp của đối tượng kết quả
     * @param sql câu lệnh truy vấn
     * @param values các giá trị cung cấp cho các tham số của SQL
     * @return kết quả truy vấn
     * @throws RuntimeException lỗi truy vấn
     */
    public static <B> List<B> getBeanList(Class<B> beanClass, String sql, Object... values) {
        List<B> list = new ArrayList<>();
        try {
            ResultSet resultSet = XJdbc.executeQuery(sql, values);
            while (resultSet.next()) {
                list.add(XQuery.readBean(resultSet, beanClass));
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }

    /**
     * Tạo bean với dữ liệu đọc từ bản ghi hiện tại
     *
     * @param <B> kiểu của đối tượng cần chuyển đổi
     * @param resultSet tập bản ghi cung cấp dữ liệu
     * @param beanClass lớp của đối tượng kết quả
     * @return kết quả truy vấn
     * @throws RuntimeException lỗi truy vấn
     */
    private static <B> B readBean(ResultSet resultSet, Class<B> beanClass) throws Exception {
        B bean = beanClass.getDeclaredConstructor().newInstance();
        Method[] methods = beanClass.getDeclaredMethods();
        for(Method method: methods){
            String name = method.getName();
            if (name.startsWith("set") && method.getParameterCount() == 1) {
                String fieldName = name.substring(3); // Remove "set" prefix
                Object value = null;
                boolean found = false;
                
                // Try different column name variations to handle case sensitivity
                String[] columnVariations = {
                    fieldName,            // Original (e.g., "Gia")
                    fieldName.toLowerCase(), // Lowercase (e.g., "gia")
                    fieldName.toUpperCase()  // Uppercase (e.g., "GIA")
                };
                
                // Try each column name variation
                for (String colName : columnVariations) {
                    try {
                        value = resultSet.getObject(colName);
                        found = true;
                        break; // Found a match, exit the loop
                    } catch (SQLException e) {
                        // Continue trying other variations
                    }
                }
                
                if (found) {
                    try {
                        Class<?> paramType = method.getParameterTypes()[0];
                        Object convertedValue = convertValue(value, paramType);
                        method.invoke(bean, convertedValue);
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        System.out.printf("+ Error setting value for '%s': %s\r\n", fieldName, e.getMessage());
                    }
                } else {
                    System.out.printf("+ Column '%s' not found!\r\n", fieldName);
                }
            }
        }
        return bean;
    }
    
    /**
     * Convert a database value to the appropriate type for a Java field
     * 
     * @param value The value from the database
     * @param targetType The target Java type
     * @return The converted value
     */
    private static Object convertValue(Object value, Class<?> targetType) {
        if (value == null) return null;
        
        // Handle BigDecimal to Number conversion
        if (value instanceof BigDecimal) {
            BigDecimal bd = (BigDecimal) value;
            if (targetType == Double.class || targetType == double.class) {
                return bd.doubleValue();
            } else if (targetType == Float.class || targetType == float.class) {
                return bd.floatValue();
            } else if (targetType == Long.class || targetType == long.class) {
                return bd.longValue();
            } else if (targetType == Integer.class || targetType == int.class) {
                return bd.intValue();
            }
        }
        
        return value;
    }
    
    public static void main(String[] args) {
        demo1();
        demo2();
    }

    private static void demo1() {
        String sql = "SELECT * FROM NGUOI_THUE WHERE HoTen LIKE ?";
        NguoiThue nguoiThue = XQuery.getSingleBean(NguoiThue.class, sql, "%Nguyễn%");
    }

    private static void demo2() {
        String sql = "SELECT * FROM NGUOI_THUE WHERE SoDienThoai LIKE ?";
        List<NguoiThue> list = XQuery.getBeanList(NguoiThue.class, sql, "%09%");
    }
}