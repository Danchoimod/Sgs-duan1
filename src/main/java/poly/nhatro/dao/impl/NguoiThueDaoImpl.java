package poly.nhatro.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import poly.nhatro.dao.NguoiThueDAO;
import poly.nhatro.entity.NguoiThue;
import poly.nhatro.util.XQuery;
import poly.nhatro.util.XJdbc;

/**
 *
 * @author tranthuyngan
 */
public class NguoiThueDaoImpl implements NguoiThueDAO {

    
    String createSql = "INSERT INTO NguoiDung(tenNguoiDung, soDienThoai, email, matKhau, namSinh, diaChi, cccdCmnn, anhTruocCccd, anhSauCccd, vaiTro, trangThai) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String updateSql = "UPDATE NguoiDung SET tenNguoiDung = ?, soDienThoai = ?, email = ?, matKhau = ?, namSinh = ?, diaChi = ?, cccdCmnn = ?, anhTruocCccd = ?, anhSauCccd = ?, vaiTro = ?, trangThai = ? WHERE ID_NguoiDung = ?";
    // Soft delete: cập nhật trạng thái thay vì xóa bản ghi
    String deleteSql = "UPDATE NguoiDung SET trangThai = N'Đã xóa' WHERE ID_NguoiDung = ?";
    // Mặc định không lấy các bản ghi đã xóa mềm
    String findAllSql = "SELECT * FROM NguoiDung WHERE ISNULL(trangThai, '') <> N'Đã xóa'";
    String findByIdSql = "SELECT * FROM NguoiDung WHERE ID_NguoiDung = ?";

    @Override
    public List<NguoiThue> findAll() {
        return XQuery.getBeanList(NguoiThue.class, findAllSql);
    }

    public NguoiThue findById(int id) {
        return XQuery.getSingleBean(NguoiThue.class, findByIdSql, id);
    }

    @Override
    public NguoiThue findById(Integer id) {
        return findById(id.intValue());
    }

    @Override
    public NguoiThue create(NguoiThue entity) {
        XJdbc.executeUpdate(createSql,
                entity.getTenNguoiDung(),
                entity.getSoDienThoai(),
                entity.getEmail(),
                entity.getMatKhau(),
                entity.getNamSinh(),
                entity.getDiaChi(),
                entity.getCccdCmnn(),
                entity.getAnhTruocCccd(),
                entity.getAnhSauCccd(),
                entity.getVaiTro(),
                entity.getTrangThai()
        );
        return entity;
    }

    @Override
    public void update(NguoiThue entity) {
        XJdbc.executeUpdate(updateSql,
                entity.getTenNguoiDung(),
                entity.getSoDienThoai(),
                entity.getEmail(),
                entity.getMatKhau(),
                entity.getNamSinh(),
                entity.getDiaChi(),
                entity.getCccdCmnn(),
                entity.getAnhTruocCccd(),
                entity.getAnhSauCccd(),
                entity.getVaiTro(),
                entity.getTrangThai(),
                entity.getID_NguoiDung());
    }

    @Override
    public void deleteById(Integer id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public boolean kiemTraNguoiDungHopLe(String ten, String cccd, String email) {
        String sql = "SELECT * FROM NguoiDung WHERE tenNguoiDung = ? AND cccdCmnn = ? AND email = ?";
        try (Connection con = XJdbc.openConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ten);
            ps.setString(2, cccd);
            ps.setString(3, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePasswordByEmail(String email, String newPassword) {
        String sql = "UPDATE NguoiDung SET matKhau = ? WHERE email = ?";
        try (Connection con = XJdbc.openConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setString(2, email);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Object[]> laySoLuongNguoiOTheoChiNhanh(int idChiNhanh) {
        List<Object[]> list = new ArrayList<>();
        String sql = """
        SELECT P.soPhong, COUNT(NTHD.ID_NguoiDung)
        FROM Phong P
        JOIN HopDong HD ON P.ID_Phong = HD.ID_Phong
        JOIN NguoiThue_HopDong NTHD ON HD.ID_HopDong = NTHD.ID_HopDong
        WHERE P.ID_ChiNhanh = ?
        GROUP BY P.soPhong
        """;
        try (ResultSet rs = XJdbc.executeQuery(sql, idChiNhanh)) {
            while (rs.next()) {
                list.add(new Object[]{rs.getString(1), rs.getInt(2)});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Object[]> layDanhSachNguoiChuaO(String soPhong, int idChiNhanh) {
        List<Object[]> list = new ArrayList<>();
        // Câu lệnh SQL này đã được kiểm tra và hoạt động chính xác
                String sql = """
                SELECT ND.tenNguoiDung
                FROM NguoiDung ND
                WHERE ND.vaiTro = N'Người thuê'
                    AND ISNULL(ND.trangThai, '') <> N'Đã xóa'
                    AND ND.ID_NguoiDung NOT IN (
                            SELECT NTHD.ID_NguoiDung
                            FROM NguoiThue_HopDong NTHD
                            JOIN HopDong HD ON NTHD.ID_HopDong = HD.ID_HopDong
                            JOIN Phong P ON HD.ID_Phong = P.ID_Phong
                            WHERE P.soPhong = ? AND P.ID_ChiNhanh = ?
                    )
        """;
        try (ResultSet rs = XJdbc.executeQuery(sql, soPhong, idChiNhanh)) {
            while (rs.next()) {
                list.add(new Object[]{rs.getString(1)});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Object[]> layDanhSachNguoiDangO(String soPhong, int idChiNhanh) {
        List<Object[]> list = new ArrayList<>();
                String sql = """
                        SELECT ND.tenNguoiDung
                        FROM NguoiDung ND
                        JOIN NguoiThue_HopDong NTHD ON ND.ID_NguoiDung = NTHD.ID_NguoiDung
                        JOIN HopDong HD ON HD.ID_HopDong = NTHD.ID_HopDong
                        JOIN Phong P ON P.ID_Phong = HD.ID_Phong
                        WHERE P.soPhong = ? AND P.ID_ChiNhanh = ?
                            AND ISNULL(ND.trangThai, '') <> N'Đã xóa'
                """;

        try (ResultSet rs = XJdbc.executeQuery(sql, soPhong, idChiNhanh)) {
            while (rs.next()) {
                list.add(new Object[]{rs.getString("tenNguoiDung")});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public boolean themNguoiOChung(int idNguoiDung, String soPhong, int idChiNhanh) {
        String sql = """
        INSERT INTO NguoiThue_HopDong (ID_HopDong, ID_NguoiDung)
        SELECT hd.ID_HopDong, ?
        FROM HopDong hd
        JOIN Phong p ON p.ID_Phong = hd.ID_Phong
        WHERE p.soPhong = ? AND p.ID_ChiNhanh = ?
    """;

        try {
            int rows = XJdbc.executeUpdate(sql, idNguoiDung, soPhong, idChiNhanh);
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean xoaNguoiOChung(int idNguoiDung, String soPhong, int idChiNhanh) {
        String sql = """
            DELETE FROM NguoiThue_HopDong
            WHERE ID_NguoiDung = ?
              AND ID_HopDong = (
                  -- Lấy ID hợp đồng đang còn hiệu lực
                  SELECT hd.ID_HopDong
                  FROM HopDong hd
                  JOIN Phong p ON p.ID_Phong = hd.ID_Phong
                  WHERE p.soPhong = ?
                    AND p.ID_ChiNhanh = ?
              );
    """;

        try {
            // In ra log để kiểm tra
            System.out.println("Thử xóa người ở chung:");
            System.out.println("- ID_NguoiDung: " + idNguoiDung);
            System.out.println("- soPhong: " + soPhong);
            System.out.println("- ID_ChiNhanh: " + idChiNhanh);

            int rows = XJdbc.executeUpdate(sql, idNguoiDung, soPhong, idChiNhanh);
            System.out.println("=> Số dòng bị ảnh hưởng: " + rows);

            return rows > 0;
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa người ở:");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int timIdTheoTen(String tenNguoi) {
    String sql = "SELECT ID_NguoiDung FROM NGUOIDUNG WHERE TenNguoiDung = ? AND ISNULL(trangThai, '') <> N'Đã xóa'";
        try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tenNguoi);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("ID_NguoiDung");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Không tìm thấy
    }

    @Override
    public boolean laNguoiKyHopDong(String tenNguoiDung, String soPhong) {
        // Câu lệnh SQL này tìm ID_NguoiDung đã ký hợp đồng của phòng
        // bằng cách nối bảng HopDong và Phong.
        String sql = """
        SELECT HD.ID_NguoiDung
        FROM HopDong HD
        JOIN Phong P ON HD.ID_Phong = P.ID_Phong
        WHERE P.soPhong = ?
    """;

        // Tìm ID của người được chọn từ ComboBox
        int idNguoiDuocChon = timIdTheoTen(tenNguoiDung);

        try (ResultSet rs = XJdbc.executeQuery(sql, soPhong)) {
            if (rs.next()) {
                // Lấy ID_NguoiDung đã ký hợp đồng
                int idNguoiKyHopDong = rs.getInt(1);

                // So sánh hai ID
                return idNguoiDuocChon == idNguoiKyHopDong;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean hasAnyContract(int idNguoiDung) {
                // Chỉ chặn xóa nếu người dùng đang có HỢP ĐỒNG ĐANG HOẠT ĐỘNG (trangThai = 0)
                // 1) Là người ký hợp đồng chính và hợp đồng đó đang active
                // 2) Hoặc là người ở chung thuộc một hợp đồng đang active
                String sql = """
                        SELECT 1
                        WHERE EXISTS (
                                            SELECT 1 FROM HopDong hd
                                            WHERE hd.ID_NguoiDung = ?
                                                AND ISNULL(hd.trangThai, 0) = 0
                                    )
                             OR EXISTS (
                                            SELECT 1
                                            FROM NguoiThue_HopDong nthd
                                            JOIN HopDong hd2 ON hd2.ID_HopDong = nthd.ID_HopDong
                                            WHERE nthd.ID_NguoiDung = ?
                                                AND ISNULL(hd2.trangThai, 0) = 0
                                    )
                """;
        try (ResultSet rs = XJdbc.executeQuery(sql, idNguoiDung, idNguoiDung)) {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            // An toàn: nếu lỗi, coi như có hợp đồng để tránh xóa nhầm
            return true;
        }
    }

}
