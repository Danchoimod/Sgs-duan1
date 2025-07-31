package poly.nhatro.entity;
/**
 *
 * @author Gia Bao
 */
public class GopY {
    private int idGopY;
    private String noiDung;
    private int idNguoiDung;
    private int idChiNhanh;


    public GopY() {
    }

    // Constructor cho việc lấy dữ liệu đầy đủ (bao gồm tên)
    // Nếu không dùng nữa thì có thể bỏ qua việc gọi constructor này
    public GopY(int idGopY, String noiDung, int idNguoiDung, int idChiNhanh, String tenNguoiDung, String tenChiNhanh) {
        this.idGopY = idGopY;
        this.noiDung = noiDung;
        this.idNguoiDung = idNguoiDung;
        this.idChiNhanh = idChiNhanh;
       
    }
    
    // Constructor cho việc tạo mới (ID_GopY tự động sinh) hoặc khi chỉ cần các ID
    public GopY(String noiDung, int idNguoiDung, int idChiNhanh) {
        this.noiDung = noiDung;
        this.idNguoiDung = idNguoiDung;
        this.idChiNhanh = idChiNhanh;
    }

    // Getters and Setters
    public int getIdGopY() {
        return idGopY;
    }

    public void setIdGopY(int idGopY) {
        this.idGopY = idGopY;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public int getIdNguoiDung() {
        return idNguoiDung;
    }

    public void setIdNguoiDung(int idNguoiDung) {
        this.idNguoiDung = idNguoiDung;
    }

    public int getIdChiNhanh() {
        return idChiNhanh;
    }

    public void setIdChiNhanh(int idChiNhanh) {
        this.idChiNhanh = idChiNhanh;
    }

  
    @Override
    public String toString() {
        return "GopY{" +
                "idGopY=" + idGopY +
                ", noiDung='" + noiDung + '\'' +
                ", idNguoiDung=" + idNguoiDung +
                ", idChiNhanh=" + idChiNhanh +
                // Bỏ hoặc giữ các trường tên tùy ý
                '}';
    }
}