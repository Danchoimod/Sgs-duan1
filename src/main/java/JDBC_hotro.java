



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author tranthithuyngan
 */
public class JDBC_hotro {
    private static String driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String dburl="jdbc:sqlserver://localhost:1433;database=demo1;integratedSecurity=false;encrypt=true;trustServerCertificate=true;";
    private static String username="sa";
    private static String password="123";

    static {
        try {
            Class.forName(driver);
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    //Hàm dùng cho trường hợp chỉ đọc dữ liệu (Select)
    public static ResultSet truyVan(String sql, Object...listThamSo) {
        try {
            Connection conn =
                DriverManager.getConnection(dburl, username, password);
            PreparedStatement stm = conn.prepareStatement(sql);
            for (int i=0; i<listThamSo.length; i++) {
                stm.setObject(i+1, listThamSo[i]);
            }
            return stm.executeQuery();
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    //Hàm dùng cho trường hợp thêm-sửa-xoá dữ liệu
    public static int capNhat(String sql,Object...listThamSo) {
        try {
            Connection conn = DriverManager.getConnection(dburl, username, password);
            PreparedStatement stm = conn.prepareStatement(sql);
            for(int i = 0; i < listThamSo.length; i++) {
                stm.setObject(i + 1, listThamSo[i]);
            }
            return stm.executeUpdate();
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
