package poly.nhatro.ui;

import poly.nhatro.dao.HopDongDAO;
import poly.nhatro.dao.impl.HopDongImpl;
import poly.nhatro.entity.HopDong;
// Removed HopDongService and HopDongServiceImpl imports
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Calendar; // Needed for calculateEndDate logic

/**
 *
 * @author Phu Pham (Updated by Gemini)
 */
public class HopDongPanel extends javax.swing.JPanel {

    // Thay đổi từ HopDongService sang HopDongDAO
    private HopDongDAO hopDongDAO; // Changed to HopDongDAO
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // Date format for text fields

    private int currentIndex = -1; // To keep track of selected row

    // Custom added variables for models
    private DefaultTableModel modelActive;
    private DefaultTableModel modelExpired;

    /**
     * Creates new form HopDongPanel
     */
    public HopDongPanel() {
        initComponents();
        // Khởi tạo HopDongDAO thay vì HopDongService
        this.hopDongDAO = new HopDongImpl(); // Initialized HopDongDAO
        init(); // Call custom initialization
    }

    private void init() {
        // Load dummy data for ComboBoxes (These should ideally come from DB)
        fillChiNhanhComboBox();
        fillNguoiDungComboBox();
        fillPhongComboBox();

        // Setup table models and fill data
        // jTable3 is "CÒN HẠN" (lblHopDong2)
        // jTable2 is "HẾT HẠN" (lblHopDong1)
        modelActive = (DefaultTableModel) jTable3.getModel();
        modelExpired = (DefaultTableModel) jTable2.getModel();
        
        fillTableActiveContracts();
        fillTableExpiredContracts();
        clearForm(); // Set initial state
        updateStatus(); // Update button status initially
    }

    private void fillChiNhanhComboBox() {
        cboChiNhanh.removeAllItems();
        // Trong một ứng dụng thực tế, bạn sẽ lấy dữ liệu này từ CSDL thông qua ChiNhanhDAO
        // Ví dụ: List<ChiNhanh> chiNhanhs = chiNhanhDAO.findAll();
        // for (ChiNhanh cn : chiNhanhs) { cboChiNhanh.addItem(cn.getID_ChiNhanh() + " (" + cn.getTenChiNhanh() + ")"); }
        cboChiNhanh.addItem("1 (Quận Ninh Kiều)");
        cboChiNhanh.addItem("2 (Quận Cái Răng)");
        cboChiNhanh.addItem("3 (Quận Bình Thủy)");
        // Add more dummy branches as needed
    }

    private void fillNguoiDungComboBox() {
        cboNguoiKiHopDong.removeAllItems();
        // Trong một ứng dụng thực tế, bạn sẽ lấy dữ liệu này từ CSDL thông qua NguoiDungDAO
        // Ví dụ: List<NguoiDung> nguoiDungs = nguoiDungDAO.findAll();
        // for (NguoiDung nd : nguoiDungs) { cboNguoiKiHopDong.addItem(nd.getID_NguoiDung() + " (" + nd.getTenNguoiDung() + ")"); }
        cboNguoiKiHopDong.addItem("1 (Nguyễn Văn A)");
        cboNguoiKiHopDong.addItem("2 (Trần Thị B)");
        cboNguoiKiHopDong.addItem("3 (Lê Văn C)");
        cboNguoiKiHopDong.addItem("4 (Phạm Thị D)");
        cboNguoiKiHopDong.addItem("5 (Hoàng Văn E)");
        cboNguoiKiHopDong.addItem("6 (Vũ Thị F)");
        cboNguoiKiHopDong.addItem("7 (Đặng Văn G)");
        cboNguoiKiHopDong.addItem("8 (Bùi Thị H)");
        cboNguoiKiHopDong.addItem("9 (Đỗ Văn I)");
        // Add more dummy user IDs (adjust if your NguoiDung table has more)
    }

    private void fillPhongComboBox() {
        cboSoPhong.removeAllItems();
        // Trong một ứng dụng thực tế, bạn sẽ lấy dữ liệu này từ CSDL thông qua PhongDAO
        // Ví dụ: List<Phong> phongs = phongDAO.findAll();
        // for (Phong p : phongs) { cboSoPhong.addItem(p.getID_Phong() + " (" + p.getSoPhong() + ")"); }
        cboSoPhong.addItem("1 (P101)");
        cboSoPhong.addItem("2 (P102)");
        cboSoPhong.addItem("3 (P201)");
        cboSoPhong.addItem("4 (P202)");
        cboSoPhong.addItem("5 (P301)");
        cboSoPhong.addItem("6 (P302)");
        cboSoPhong.addItem("7 (P401)");
        cboSoPhong.addItem("8 (P402)");
        cboSoPhong.addItem("9 (P501)");
        cboSoPhong.addItem("10 (P502)");
        // Add more dummy room IDs
    }

    private void fillTableActiveContracts() {
        modelActive.setRowCount(0); // Clear existing data
        try {
            List<HopDong> list = hopDongDAO.selectActiveContracts(); // Changed to hopDongDAO
            for (HopDong hd : list) {
                Date ngayKetThuc = calculateEndDate(hd.getNgayTao(), hd.getThoiHan()); // Use local method
                Object[] row = {
                    hd.getID_HopDong(),
                    sdf.format(hd.getNgayTao()),
                    hd.getThoiHan(), // Thêm cột Thời hạn
                    ngayKetThuc != null ? sdf.format(ngayKetThuc) : "N/A",
                    hd.getTienCoc(),
                    hd.getNuocBanDau(),
                    hd.getDienBanDau(),
                    hd.getID_NguoiDung(), // Hiển thị ID Người Dùng
                    hd.getID_Phong(), // Hiển thị ID Phòng
                    "CÒN HẠN" // Status based on table
                };
                modelActive.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu hợp đồng còn hạn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void fillTableExpiredContracts() {
        modelExpired.setRowCount(0); // Clear existing data
        try {
            List<HopDong> list = hopDongDAO.selectExpiredContracts(); // Changed to hopDongDAO
            for (HopDong hd : list) {
                Date ngayKetThuc = calculateEndDate(hd.getNgayTao(), hd.getThoiHan()); // Use local method
                Object[] row = {
                    hd.getID_HopDong(),
                    sdf.format(hd.getNgayTao()),
                    hd.getThoiHan(), // Thêm cột Thời hạn
                    ngayKetThuc != null ? sdf.format(ngayKetThuc) : "N/A",
                    hd.getTienCoc(),
                    hd.getNuocBanDau(),
                    hd.getDienBanDau(),
                    hd.getID_NguoiDung(), // Hiển thị ID Người Dùng
                    hd.getID_Phong(), // Hiển thị ID Phòng
                    "HẾT HẠN" // Status based on table
                };
                modelExpired.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu hợp đồng hết hạn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Phương thức tính toán ngày hết hạn hợp đồng (moved from service to panel)
    private Date calculateEndDate(Date ngayTao, int thoiHan) {
        if (ngayTao == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ngayTao);
        calendar.add(Calendar.MONTH, thoiHan);
        return calendar.getTime();
    }

    private HopDong getForm() {
        try {
            // Mapping fields based on the provided UI layout and labels:
            // txtTimKiem6: Mã hợp đồng (ID_HopDong)
            // txtTimKiem2: Ngày tạo (ngayTao)
            // txtTimKiem1: Thời hạn (thoiHan)
            // txtTimKiem3: Tiền cọc (tienCoc)
            // txtTimKiem4: Nước ban đầu (nuocBanDau)
            // txtTimKiem5: Điện ban đầu (dienBanDau)
            // cboNguoiKiHopDong: Người kí hợp đồng (ID_NguoiDung)
            // cboSoPhong: Mã phòng (ID_Phong)
            
            Date ngayTao = sdf.parse(txtTimKiem2.getText().trim());
            int thoiHan = Integer.parseInt(txtTimKiem1.getText().trim()); // Assuming txtTimKiem1 is for ThoiHan
            int tienCoc = Integer.parseInt(txtTimKiem3.getText().trim());
            int nuocBanDau = Integer.parseInt(txtTimKiem4.getText().trim());
            int dienBanDau = Integer.parseInt(txtTimKiem5.getText().trim());
            
            // Extract ID from ComboBox string (e.g., "1 (Nguyễn Văn A)" -> "1")
            int idNguoiDung = Integer.parseInt(((String) cboNguoiKiHopDong.getSelectedItem()).split(" ")[0]); 
            int idPhong = Integer.parseInt(((String) cboSoPhong.getSelectedItem()).split(" ")[0]);

            HopDong hopDong = new HopDong();
            // If txtTimKiem6 has a value, it means we are updating, otherwise adding
            if (!txtTimKiem6.getText().trim().isEmpty()) {
                hopDong.setID_HopDong(Integer.parseInt(txtTimKiem6.getText().trim()));
            }
            hopDong.setNgayTao(ngayTao);
            hopDong.setThoiHan(thoiHan);
            hopDong.setTienCoc(tienCoc);
            hopDong.setNuocBanDau(nuocBanDau);
            hopDong.setDienBanDau(dienBanDau);
            hopDong.setID_NguoiDung(idNguoiDung);
            hopDong.setID_Phong(idPhong);
            return hopDong;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số cho thời hạn, tiền cọc, chỉ số nước/điện và ID.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng ngày (dd/MM/yyyy) cho 'Ngày tạo'.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu từ form: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }

    private void setForm(HopDong hd) {
        txtTimKiem6.setText(String.valueOf(hd.getID_HopDong())); // Set ID_HopDong
        txtTimKiem2.setText(sdf.format(hd.getNgayTao())); // Set Ngay tao
        txtTimKiem1.setText(String.valueOf(hd.getThoiHan())); // Set Thoi han
        txtTimKiem3.setText(String.valueOf(hd.getTienCoc())); // Set Tien coc
        txtTimKiem4.setText(String.valueOf(hd.getNuocBanDau())); // Set Nuoc ban dau
        txtTimKiem5.setText(String.valueOf(hd.getDienBanDau())); // Set Dien ban dau
        
        // Thiết lập txtTimKiem7 (Mã người dùng)
        txtTimKiem7.setText(String.valueOf(hd.getID_NguoiDung()));

        // Set selected item in ComboBoxes based on ID (and possibly name part)
        setComboBoxSelectedItem(cboNguoiKiHopDong, String.valueOf(hd.getID_NguoiDung()));
        setComboBoxSelectedItem(cboSoPhong, String.valueOf(hd.getID_Phong()));
        // cboChiNhanh is not directly set by HopDong, it's usually derived from Phong
        // If you need to set cboChiNhanh based on the selected HopDong, you'd need to fetch Phong details
        // and then ChiNhanh details. For now, it remains as is based on the provided code structure.
    }

    // Helper method to set selected item for ComboBoxes that might have "ID (Name)" format
    private void setComboBoxSelectedItem(JComboBox<String> comboBox, String id) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            String item = comboBox.getItemAt(i);
            if (item.startsWith(id + " ") || item.equals(id)) { // Match "ID (Name)" or just "ID"
                comboBox.setSelectedIndex(i);
                return;
            }
        }
        comboBox.setSelectedIndex(0); // Default to first item if not found (or -1 if you want no selection)
    }

    private void clearForm() {
        txtTimKiem6.setText(""); // Clear ID_HopDong
        txtTimKiem2.setText(sdf.format(new Date())); // Set current date for ngayTao
        txtTimKiem1.setText(""); // Clear Thoi han
        txtTimKiem3.setText(""); // Clear Tien coc
        txtTimKiem4.setText(""); // Clear Nuoc ban dau
        txtTimKiem5.setText(""); // Clear Dien ban dau
        txtTimKiem7.setText(""); // Clear Mã người dùng
        txtTimKiem.setText(""); // Clear search field (if used)

        cboChiNhanh.setSelectedIndex(0);
        cboNguoiKiHopDong.setSelectedIndex(0);
        cboSoPhong.setSelectedIndex(0);
        
        currentIndex = -1;
        updateStatus();
    }

    private void addHopDong() {
        HopDong hd = getForm();
        if (hd == null) return;

        try {
            hopDongDAO.insert(hd); // Changed to hopDongDAO
            JOptionPane.showMessageDialog(this, "Thêm hợp đồng thành công!");
            fillTableActiveContracts(); // Refresh tables
            fillTableExpiredContracts();
            clearForm();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Lỗi dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm hợp đồng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateHopDong() {
        if (currentIndex == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hợp đồng cần cập nhật.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        HopDong hd = getForm();
        if (hd == null) return;

        try {
            // Get ID_HopDong from the text field, as it's the primary key for update
            int ID_HopDong = Integer.parseInt(txtTimKiem6.getText().trim());
            hd.setID_HopDong(ID_HopDong); // Ensure ID is set for update

            hopDongDAO.update(hd); // Changed to hopDongDAO
            JOptionPane.showMessageDialog(this, "Cập nhật hợp đồng thành công!");
            fillTableActiveContracts(); // Refresh tables
            fillTableExpiredContracts();
            clearForm();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Mã hợp đồng không hợp lệ. Vui lòng chọn hợp đồng từ bảng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Lỗi dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật hợp đồng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deleteHopDong() {
        if (currentIndex == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hợp đồng cần xóa.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa hợp đồng này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int ID_HopDong = Integer.parseInt(txtTimKiem6.getText().trim()); // Get ID from text field
            hopDongDAO.delete(ID_HopDong); // Changed to hopDongDAO
            JOptionPane.showMessageDialog(this, "Xóa hợp đồng thành công!");
            fillTableActiveContracts(); // Refresh tables
            fillTableExpiredContracts();
            clearForm();

            XDialog.alert("Thêm mới thành công!");
        } catch (RuntimeException e) {
            String errorMessage = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
            if (errorMessage != null) {
                if (errorMessage.contains("FOREIGN KEY constraint") && errorMessage.contains("NguoiDung")) {
                    XDialog.alert("Thêm mới thất bại: Mã người dùng không tồn tại. Vui lòng kiểm tra lại Mã người dùng.");
                } else if (errorMessage.contains("FOREIGN KEY constraint") && errorMessage.contains("Phong")) {
                    XDialog.alert("Thêm mới thất bại: Mã phòng không tồn tại. Vui lòng kiểm tra lại Mã phòng.");
                } else if (errorMessage.contains("FOREIGN KEY constraint") && errorMessage.contains("ChiNhanh")) {
                    XDialog.alert("Thêm mới thất bại: Mã chi nhánh không tồn tại. Vui lòng kiểm tra lại Mã chi nhánh.");
                } else {
                    XDialog.alert("Thêm mới thất bại: " + errorMessage);
                }
            } else {
                XDialog.alert("Thêm mới thất bại: Đã xảy ra lỗi không xác định.");
            }

            e.printStackTrace();
        }
    }


    private void update() {
        try {
            HopDong hd = readForm();
            if (hd.getSoTienCoc() < 0) {
                XDialog.alert("Số tiền cọc không được âm.");
                return;
            }
            if (hd.getNgayBatDau().after(hd.getNgayKetThuc())) {
                XDialog.alert("Ngày bắt đầu không được sau ngày kết thúc.");
                return;
            }

            hopDongDAO.update(hd);
            fillTable();
            clearForm();
            XDialog.alert("Cập nhật thành công!");
        } catch (RuntimeException e) {
            String errorMessage = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
            if (errorMessage != null) {
                if (errorMessage.contains("FOREIGN KEY constraint") && errorMessage.contains("NguoiDung")) {
                    XDialog.alert("Cậ̣p nhật thất bại: Mã người dùng không tồn tại. Vui lòng kiểm tra lại Mã người dùng.");
                } else if (errorMessage.contains("FOREIGN KEY constraint") && errorMessage.contains("Phong")) {
                    XDialog.alert("Cập nhật thất bại: Mã phòng không tồn tại. Vui lòng kiểm tra lại Mã phòng.");
                } else if (errorMessage.contains("FOREIGN KEY constraint") && errorMessage.contains("ChiNhanh")) {
                    XDialog.alert("Cập nhật thất bại: Mã chi nhánh không tồn tại. Vui lòng kiểm tra lại Mã chi nhánh.");
                } else {
                    XDialog.alert("Cập nhật thất bại: ");

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu hợp đồng lên form: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    // Helper to update button states
    private void updateStatus() {
        // If txtTimKiem6 (ID_HopDong) is empty, it's a new record (add mode)
        // If it has a value, it's an existing record (edit/delete mode)
        boolean isAdding = txtTimKiem6.getText().trim().isEmpty();

        btnThem.setEnabled(isAdding);
        btnSua.setEnabled(!isAdding);
        btnXoa.setEnabled(!isAdding);
        
        // Make txtTimKiem7 (Mã người dùng) non-editable
        txtTimKiem7.setEditable(false);
    }

    // New search method
    private void search() {
        String keyword = txtTimKiem.getText().trim();
        if (keyword.isEmpty()) {
            fillTableActiveContracts(); // If search box is empty, show all active contracts
            fillTableExpiredContracts(); // If search box is empty, show all expired contracts
            return;
        }

        try {
            List<HopDong> searchResults = hopDongDAO.searchContracts(keyword);

            // Clear both tables before displaying search results
            modelActive.setRowCount(0);
            modelExpired.setRowCount(0);

            if (searchResults.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy hợp đồng nào phù hợp với từ khóa: \"" + keyword + "\"", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (HopDong hd : searchResults) {
                Date ngayKetThuc = calculateEndDate(hd.getNgayTao(), hd.getThoiHan());
                Object[] row = {
                    hd.getID_HopDong(),
                    sdf.format(hd.getNgayTao()),
                    hd.getThoiHan(),
                    ngayKetThuc != null ? sdf.format(ngayKetThuc) : "N/A",
                    hd.getTienCoc(),
                    hd.getNuocBanDau(),
                    hd.getDienBanDau(),
                    hd.getID_NguoiDung(),
                    hd.getID_Phong(),
                    ngayKetThuc != null && ngayKetThuc.after(new Date()) ? "CÒN HẠN" : "HẾT HẠN"
                };
                if (ngayKetThuc != null && ngayKetThuc.after(new Date())) {
                    modelActive.addRow(row);
                } else {
                    modelExpired.addRow(row);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm hợp đồng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCalendar1 = new com.toedter.calendar.JCalendar();
        pnlHopDong = new javax.swing.JPanel();
        lblHopDong = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cboChiNhanh = new javax.swing.JComboBox<>();
        txtTimKiem = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTimKiem1 = new javax.swing.JTextField();
        txtTimKiem2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtTimKiem3 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtTimKiem4 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtTimKiem5 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtTimKiem6 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtTimKiem7 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cboNguoiKiHopDong = new javax.swing.JComboBox<>();
        cboSoPhong = new javax.swing.JComboBox<>();
        btnTimKiem = new javax.swing.JButton();
        lblHopDong1 = new javax.swing.JLabel();
        lblHopDong2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();

        pnlHopDong.setBackground(new java.awt.Color(255, 255, 255));

        lblHopDong.setFont(new java.awt.Font("Segoe UI Black", 0, 24)); // NOI18N
        lblHopDong.setText("HỢP ĐỒNG");

        javax.swing.GroupLayout pnlHopDongLayout = new javax.swing.GroupLayout(pnlHopDong);
        pnlHopDong.setLayout(pnlHopDongLayout);
        pnlHopDongLayout.setHorizontalGroup(
            pnlHopDongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHopDongLayout.createSequentialGroup()
                .addGap(443, 443, 443)
                .addComponent(lblHopDong)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlHopDongLayout.setVerticalGroup(
            pnlHopDongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHopDongLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblHopDong)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Chi nhánh");

        cboChiNhanh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Ngày bắt đầu");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Tháng hết hạn");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Người kí hợp đồng");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Tiền cọc");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Nước ban đầu");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setText("Điện ban đầu");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setText("Mã hợp đồng");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setText("Mã người dùng");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel11.setText("Mã phòng");

        cboNguoiKiHopDong.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboSoPhong.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTimKiem2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTimKiem6, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboNguoiKiHopDong, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(39, 39, 39)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTimKiem7, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtTimKiem1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cboChiNhanh, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(20, 20, 20)))
                                .addGap(19, 19, 19)
                                .addComponent(btnTimKiem)
                                .addGap(18, 18, 18)
                                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTimKiem3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTimKiem4, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboSoPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtTimKiem5, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(169, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cboChiNhanh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtTimKiem6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimKiem))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtTimKiem1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKiem2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel10)
                    .addComponent(txtTimKiem7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(cboNguoiKiHopDong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSoPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtTimKiem3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtTimKiem4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtTimKiem5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        lblHopDong1.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        lblHopDong1.setText("HẾT HẠN");

        lblHopDong2.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        lblHopDong2.setText("CÒN HẠN");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã hợp đồng", "Ngày tạo", "Thời hạn", "Tiền cọc", "Nước ban đầu", "Điện ban đầu", "Mã người dùng", "Mã phòng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã hợp đồng", "Ngày tạo", "thời hạn", "Tiền cọc", "Nước ban đầu", "Điện ban đầu", "Mã người dùng", "Mã phòng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnLamMoi.setText("Làm mới");
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHopDong, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane3)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblHopDong2)
                    .addComponent(lblHopDong1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnThem)
                .addGap(18, 18, 18)
                .addComponent(btnSua)
                .addGap(18, 18, 18)
                .addComponent(btnXoa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLamMoi)
                .addGap(14, 14, 14))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlHopDong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblHopDong2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblHopDong1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnSua)
                    .addComponent(btnXoa)
                    .addComponent(btnLamMoi))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
       addHopDong();
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
   updateHopDong();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
  deleteHopDong();
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        clearForm();
     
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
     editHopDong(jTable3);
    }//GEN-LAST:event_jTable3MouseClicked

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
     editHopDong(jTable2);
    }//GEN-LAST:event_jTable2MouseClicked

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
      search();
    }//GEN-LAST:event_btnTimKiemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboChiNhanh;
    private javax.swing.JComboBox<String> cboNguoiKiHopDong;
    private javax.swing.JComboBox<String> cboSoPhong;
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JLabel lblHopDong;
    private javax.swing.JLabel lblHopDong1;
    private javax.swing.JLabel lblHopDong2;
    private javax.swing.JPanel pnlHopDong;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txtTimKiem1;
    private javax.swing.JTextField txtTimKiem2;
    private javax.swing.JTextField txtTimKiem3;
    private javax.swing.JTextField txtTimKiem4;
    private javax.swing.JTextField txtTimKiem5;
    private javax.swing.JTextField txtTimKiem6;
    private javax.swing.JTextField txtTimKiem7;
    // End of variables declaration//GEN-END:variables
}
