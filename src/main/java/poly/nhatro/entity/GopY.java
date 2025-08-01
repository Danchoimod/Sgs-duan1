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
    private String tenNguoiDung; // New field for user name
    private String tenChiNhanh; // New field for branch name

    public GopY() {
    }

    // Constructor for creating new GopY (ID_GopY is auto-generated)
    public GopY(String noiDung, int idNguoiDung, int idChiNhanh) {
        this.noiDung = noiDung;
        this.idNguoiDung = idNguoiDung;
        this.idChiNhanh = idChiNhanh;
    }

    // Full constructor including names (for data retrieved from DB with JOINs)
    public GopY(int idGopY, String noiDung, int idNguoiDung, int idChiNhanh, String tenNguoiDung, String tenChiNhanh) {
        this.idGopY = idGopY;
        this.noiDung = noiDung;
        this.idNguoiDung = idNguoiDung;
        this.idChiNhanh = idChiNhanh;
        this.tenNguoiDung = tenNguoiDung;
        this.tenChiNhanh = tenChiNhanh;
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

    public String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }

    public String getTenChiNhanh() {
        return tenChiNhanh;
    }

    public void setTenChiNhanh(String tenChiNhanh) {
        this.tenChiNhanh = tenChiNhanh;
    }

    @Override
    public String toString() {
        return "GopY{" +
                "idGopY=" + idGopY +
                ", noiDung='" + noiDung + '\'' +
                ", idNguoiDung=" + idNguoiDung +
                ", idChiNhanh=" + idChiNhanh +
                ", tenNguoiDung='" + tenNguoiDung + '\'' +
                ", tenChiNhanh='" + tenChiNhanh + '\'' +
                '}';
    }
}
