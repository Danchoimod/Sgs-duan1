package poly.cafe.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import poly.cafe.entity.Category;

/**
 * Lớp tiện ích hỗ trợ làm việc với CSDL quan hệ
 *
 * @author NghiemN
 * @version 1.0
 */
public class XJdbc {
    private static Connection connection;
    private static final String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String dburl = "jdbc:sqlserver://localhost:1433;database=PolyCafe;encrypt=true;trustServerCertificate=true;";
    private static final String username = "sa";
    private static final String password = "123";

    /**
     * Mở kết nối nếu chưa mở hoặc đã đóng
     *
     * @return Kết nối đã sẵn sàng
     */
    public static Connection openConnection() {
        try {
            if (!XJdbc.isReady()) {
                Class.forName(driver);
                connection = DriverManager.getConnection(dburl, username, password);
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    /**
     * Đóng kết nối
     */
    public static void closeConnection() {
        try {
            if (XJdbc.isReady()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Kiểm tra kết nối đã sẵn sàng hay chưa
     *
     * @return true nếu kết nối đã được mở
     */
    public static boolean isReady() {
        try {
            return (connection != null && !connection.isClosed());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Thao tác dữ liệu (INSERT, UPDATE, DELETE)
     *
     * @param sql câu lệnh SQL
     * @param values các giá trị tham số
     * @return số bản ghi bị ảnh hưởng
     */
    public static int executeUpdate(String sql, Object... values) {
        try {
            var stmt = XJdbc.getStmt(sql, values);
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Truy vấn dữ liệu (SELECT)
     *
     * @param sql câu lệnh SQL
     * @param values các giá trị tham số
     * @return tập kết quả truy vấn
     */
    public static ResultSet executeQuery(String sql, Object... values) {
        try {
            var stmt = XJdbc.getStmt(sql, values);
            return stmt.executeQuery();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Truy vấn một giá trị đơn
     *
     * @param <T> kiểu dữ liệu trả về
     * @param sql câu lệnh SQL
     * @param values tham số
     * @return giá trị đầu tiên của kết quả
     */
    public static <T> T getValue(String sql, Object... values) {
        try {
            var resultSet = XJdbc.executeQuery(sql, values);
            if (resultSet.next()) {
                return (T) resultSet.getObject(1);
            }
            return null;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Tạo PreparedStatement từ câu SQL và tham số
     *
     * @param sql câu lệnh SQL hoặc PROCEDURE
     * @param values tham số
     * @return PreparedStatement đã cấu hình
     */
    private static PreparedStatement getStmt(String sql, Object... values) throws SQLException {
        var conn = XJdbc.openConnection();
        var stmt = sql.trim().startsWith("{") ? conn.prepareCall(sql) : conn.prepareStatement(sql);
        for (int i = 0; i < values.length; i++) {
            stmt.setObject(i + 1, values[i]);
        }
        return stmt;
    }

    /**
     * Hàm main để test
     */
    public static void main(String[] args) {
        demo1();
        demo2();
        demo3();
    }

    private static void demo1() {
        String sql = "SELECT * FROM Drinks WHERE UnitPrice BETWEEN ? AND ?";
        try (var rs = XJdbc.executeQuery(sql, 1.5, 5.0)) {
            while (rs.next()) {
                System.out.println("Drink: " + rs.getString("DrinkName") + " - Price: " + rs.getDouble("UnitPrice"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void demo2() {
        String sql = "SELECT max(UnitPrice) FROM Drinks WHERE UnitPrice > ?";
        Double maxPrice = XJdbc.getValue(sql, 1.5);
        System.out.println("Max Price > 1.5 = " + maxPrice);
    }

    private static void demo3() {
        String sql = "DELETE FROM Drinks WHERE UnitPrice < ?";
        int count = XJdbc.executeUpdate(sql, 0.0);
        System.out.println("Deleted rows: " + count);
    }

    public static List<Category> getBeanList(Class<Category> clazz, String sql, Object... args) {
        List<Category> list = new ArrayList<>();
        try {
            ResultSet rs = executeQuery(sql, args);
            while (rs.next()) {
                Category bean = new Category();
                bean.setId(rs.getString("Id"));
                bean.setName(rs.getString("Name"));
                list.add(bean);
            }
            rs.getStatement().getConnection().close(); 
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public static Category getSingleBean(Class<Category> clazz, String sql, Object... args) {
        List<Category> list = getBeanList(clazz, sql, args);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    
}


