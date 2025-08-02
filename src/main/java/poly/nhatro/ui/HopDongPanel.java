package poly.nhatro.ui;

import poly.nhatro.dao.HopDongDAO;
import poly.nhatro.dao.impl.HopDongImpl;
import poly.nhatro.entity.HopDong;
import poly.nhatro.dao.ChiNhanhDAO;
import poly.nhatro.dao.impl.ChiNhanhDAOImpl;
import poly.nhatro.entity.ChiNhanh;
import poly.nhatro.dao.impl.PhongDaoImpl;
import poly.nhatro.entity.Phong;
import poly.nhatro.dao.NguoiDungDAO;
import poly.nhatro.dao.impl.NguoiDungDAOImpl;
import poly.nhatro.entity.NguoiDung;
import poly.nhatro.dao.DienNuocDAO;
import poly.nhatro.dao.impl.DienNuocDAOImpl;
import poly.nhatro.entity.DienNuoc;
// Removed HopDongService and HopDongServiceImpl imports
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Calendar; // Needed for calculateEndDate logic
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 *
 * @author Phu Pham (Updated by Gemini)
 */
public class HopDongPanel extends javax.swing.JPanel {

    // Thay đổi từ HopDongService sang HopDongDAO
    private HopDongDAO hopDongDAO; // Changed to HopDongDAO
    private ChiNhanhDAO chiNhanhDAO; // Added ChiNhanhDAO
    private PhongDaoImpl phongDAO; // Added PhongDAO
    private NguoiDungDAO nguoiDungDAO; // Added NguoiDungDAO
    private DienNuocDAO dienNuocDAO; // Added DienNuocDAO
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // Date format for text fields

    private int currentIndex = -1; // To keep track of selected row
    private int currentHopDongId = -1; // To keep track of current contract ID when editing

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
        this.chiNhanhDAO = new ChiNhanhDAOImpl(); // Initialize ChiNhanhDAO
        this.phongDAO = new PhongDaoImpl(); // Initialize PhongDAO
        this.nguoiDungDAO = new NguoiDungDAOImpl(); // Initialize NguoiDungDAO
        this.dienNuocDAO = new DienNuocDAOImpl(); // Initialize DienNuocDAO
        init(); // Call custom initialization
    }

    private void init() {
        // Load data for ComboBoxes from database
        fillChiNhanhComboBox();
        fillNguoiDungComboBox();
        fillPhongComboBox();
        
        // Add event listener for chi nhanh selection
        cboChiNhanh.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    onChiNhanhSelected();
                }
            }
        });

        // Add event listener for phong selection to auto-populate meter readings
        cboSoPhong.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    onPhongSelected();
                }
            }
        });

        // Add document listeners for automatic date calculation
        setupDateCalculationListeners();

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

    /**
     * Setup listeners for automatic date calculation
     */
    private void setupDateCalculationListeners() {
        // Add property change listener for dateBatDau (Ngày bắt đầu)
        dateBatDau.addPropertyChangeListener("date", evt -> {
            if (evt.getNewValue() != null) {
                calculateEndDate();
            }
        });

        // Add document listener for txtTimKiem1 (Thời hạn)
        txtTimKiem1.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                calculateEndDate();
            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                calculateEndDate();
            }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                calculateEndDate();
            }
        });
    }

    /**
     * Calculate and set end date based on start date and duration
     */
    private void calculateAndSetEndDate() {
        try {
            Date startDate = dateBatDau.getDate();
            String durationStr = txtTimKiem1.getText().trim();
            
            if (startDate != null && !durationStr.isEmpty()) {
                int duration = Integer.parseInt(durationStr);
                
                Date endDate = calculateEndDate(startDate, duration);
                if (endDate != null) {
                    dateKetThucHopDong.setDate(endDate);
                }
            } else {
                // Clear end date if start date or duration is empty
                dateKetThucHopDong.setDate(null);
            }
        } catch (NumberFormatException e) {
            // Clear end date if parsing fails
            dateKetThucHopDong.setDate(null);
        }
    }

    /**
     * Wrapper method to handle swing timer for date calculation
     */
    private void calculateEndDate() {
        // Use swing timer to avoid multiple rapid calculations
        javax.swing.Timer timer = new javax.swing.Timer(300, e -> calculateAndSetEndDate());
        timer.setRepeats(false);
        timer.start();
    }

    private void fillChiNhanhComboBox() {
        cboChiNhanh.removeAllItems();
        cboChiNhanh.addItem("Tất cả"); // Default selection showing all
        
        try {
            List<ChiNhanh> chiNhanhs = chiNhanhDAO.getAll();
            for (ChiNhanh cn : chiNhanhs) {
                cboChiNhanh.addItem(cn.getTenChiNhanh());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách chi nhánh: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void fillNguoiDungComboBox() {
        cboNguoiKiHopDong.removeAllItems();
        cboNguoiKiHopDong.addItem("-- Chọn người dùng --"); // Default empty selection
        
        try {
            // Chỉ lấy những người dùng chưa có hợp đồng đang hoạt động
            List<NguoiDung> nguoiDungs = nguoiDungDAO.findAvailableForContract();
            for (NguoiDung nd : nguoiDungs) {
                cboNguoiKiHopDong.addItem(nd.getID_NguoiDung() + " (" + nd.getTenNguoiDung() + ")");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách người dùng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Fill combo box for editing contract - includes all active users and the current contract user
     */
    private void fillNguoiDungComboBoxForEdit(int currentUserId) {
        cboNguoiKiHopDong.removeAllItems();
        cboNguoiKiHopDong.addItem("-- Chọn người dùng --"); // Default empty selection
        
        try {
            // Lấy người dùng hiện tại của hợp đồng
            NguoiDung currentUser = nguoiDungDAO.findById(currentUserId);
            if (currentUser != null) {
                cboNguoiKiHopDong.addItem(currentUser.getID_NguoiDung() + " (" + currentUser.getTenNguoiDung() + ")");
            }

            // Thêm những người dùng chưa có hợp đồng đang hoạt động
            List<NguoiDung> availableUsers = nguoiDungDAO.findAvailableForContract();
            for (NguoiDung nd : availableUsers) {
                // Tránh thêm trùng lặp người dùng hiện tại
                if (nd.getID_NguoiDung() != currentUserId) {
                    cboNguoiKiHopDong.addItem(nd.getID_NguoiDung() + " (" + nd.getTenNguoiDung() + ")");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách người dùng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void fillPhongComboBox() {
        cboSoPhong.removeAllItems();
        cboSoPhong.addItem("-- Chọn phòng --"); // Default empty selection
        
        // Initially load all available rooms (since chi nhanh defaults to "Tất cả")
        try {
            List<Phong> phongs = phongDAO.findAllAvailable(); // Get all available rooms
            for (Phong p : phongs) {
                cboSoPhong.addItem(p.getSoPhong());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách phòng trống: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Handle chi nhanh selection to filter phong list and update contract tables
     */
    private void onChiNhanhSelected() {
        String selectedChiNhanh = (String) cboChiNhanh.getSelectedItem();
        cboSoPhong.removeAllItems();
        cboSoPhong.addItem("-- Chọn phòng --");
        
        if (selectedChiNhanh == null || selectedChiNhanh.equals("Tất cả")) {
            // If "Tất cả" selected, show all contracts and all available rooms
            fillTableActiveContracts();
            fillTableExpiredContracts();
            
            // Load all available rooms when "Tất cả" is selected
            try {
                List<Phong> phongs = phongDAO.findAllAvailable();
                for (Phong p : phongs) {
                    cboSoPhong.addItem(p.getSoPhong());
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách tất cả phòng trống: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
            return;
        }
        
        try {
            // Find the selected chi nhanh ID
            List<ChiNhanh> chiNhanhs = chiNhanhDAO.getAll();
            int selectedChiNhanhId = -1;
            for (ChiNhanh cn : chiNhanhs) {
                if (cn.getTenChiNhanh().equals(selectedChiNhanh)) {
                    selectedChiNhanhId = cn.getID_ChiNhanh();
                    break;
                }
            }
            
            if (selectedChiNhanhId != -1) {
                // Load only available rooms for the selected chi nhanh
                List<Phong> phongs = phongDAO.findAvailableByChiNhanh(selectedChiNhanhId);
                for (Phong p : phongs) {
                    cboSoPhong.addItem(p.getSoPhong());
                }
                
                // Filter and display contracts by selected chi nhanh
                fillTableByChiNhanh(selectedChiNhanhId);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách phòng trống theo chi nhánh: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void fillTableActiveContracts() {
        modelActive.setRowCount(0); // Clear existing data
        try {
            List<HopDong> list = hopDongDAO.selectActiveContracts(); // Changed to hopDongDAO
            for (HopDong hd : list) {
                // Lấy tên người dùng
                String tenNguoiKy = "N/A";
                try {
                    NguoiDung nguoiDung = nguoiDungDAO.findById(hd.getID_NguoiDung());
                    if (nguoiDung != null) {
                        tenNguoiKy = nguoiDung.getTenNguoiDung();
                    }
                } catch (Exception e) {
                    System.err.println("Lỗi khi lấy tên người dùng ID " + hd.getID_NguoiDung() + ": " + e.getMessage());
                }
                
                // Lấy số phòng
                String soPhong = "N/A";
                try {
                    Phong phong = phongDAO.findById(hd.getID_Phong());
                    if (phong != null) {
                        soPhong = phong.getSoPhong();
                    }
                } catch (Exception e) {
                    System.err.println("Lỗi khi lấy thông tin phòng ID " + hd.getID_Phong() + ": " + e.getMessage());
                }
                
                // Calculate end date
                Date ngayKetThuc = calculateEndDate(hd.getNgayTao(), hd.getThoiHan());
                
                Object[] row = {
                    hd.getID_HopDong(),
                    tenNguoiKy, // Tên người ký
                    soPhong, // Số phòng
                    sdf.format(hd.getNgayTao()), // Ngày bắt đầu
                    ngayKetThuc != null ? sdf.format(ngayKetThuc) : "N/A", // Ngày kết thúc
                    hd.getTienCoc(), // Tiền cọc
                    hd.getDienBanDau(), // Điện ban đầu
                    hd.getNuocBanDau() // Nước ban đầu
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
                // Lấy tên người dùng
                String tenNguoiKy = "N/A";
                try {
                    NguoiDung nguoiDung = nguoiDungDAO.findById(hd.getID_NguoiDung());
                    if (nguoiDung != null) {
                        tenNguoiKy = nguoiDung.getTenNguoiDung();
                    }
                } catch (Exception e) {
                    System.err.println("Lỗi khi lấy tên người dùng ID " + hd.getID_NguoiDung() + ": " + e.getMessage());
                }
                
                // Lấy số phòng
                String soPhong = "N/A";
                try {
                    Phong phong = phongDAO.findById(hd.getID_Phong());
                    if (phong != null) {
                        soPhong = phong.getSoPhong();
                    }
                } catch (Exception e) {
                    System.err.println("Lỗi khi lấy thông tin phòng ID " + hd.getID_Phong() + ": " + e.getMessage());
                }
                
                // Calculate end date
                Date ngayKetThuc = calculateEndDate(hd.getNgayTao(), hd.getThoiHan());
                
                Object[] row = {
                    hd.getID_HopDong(),
                    tenNguoiKy, // Tên người ký
                    soPhong, // Số phòng
                    sdf.format(hd.getNgayTao()), // Ngày bắt đầu
                    ngayKetThuc != null ? sdf.format(ngayKetThuc) : "N/A", // Ngày kết thúc
                    hd.getTienCoc(), // Tiền cọc
                    hd.getDienBanDau(), // Điện ban đầu
                    hd.getNuocBanDau() // Nước ban đầu
                };
                modelExpired.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu hợp đồng hết hạn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Fill tables with contracts filtered by chi nhanh
     */
    private void fillTableByChiNhanh(int chiNhanhId) {
        // Clear both tables first
        modelActive.setRowCount(0);
        modelExpired.setRowCount(0);
        
        try {
            // Get all active contracts
            List<HopDong> activeContracts = hopDongDAO.selectActiveContracts();
            // Get all expired contracts
            List<HopDong> expiredContracts = hopDongDAO.selectExpiredContracts();
            
            // Filter active contracts by chi nhanh
            for (HopDong hd : activeContracts) {
                // Get room info to check chi nhanh
                Phong phong = phongDAO.findById(hd.getID_Phong());
                if (phong != null && phong.getIdChiNhanh() == chiNhanhId) {
                    // Lấy tên người dùng
                    String tenNguoiKy = "N/A";
                    try {
                        NguoiDung nguoiDung = nguoiDungDAO.findById(hd.getID_NguoiDung());
                        if (nguoiDung != null) {
                            tenNguoiKy = nguoiDung.getTenNguoiDung();
                        }
                    } catch (Exception e) {
                        System.err.println("Lỗi khi lấy tên người dùng ID " + hd.getID_NguoiDung() + ": " + e.getMessage());
                    }
                    
                    // Calculate end date
                    Date ngayKetThuc = calculateEndDate(hd.getNgayTao(), hd.getThoiHan());
                    
                    Object[] row = {
                        hd.getID_HopDong(),
                        tenNguoiKy, // Tên người ký
                        phong.getSoPhong(), // Số phòng
                        sdf.format(hd.getNgayTao()), // Ngày bắt đầu
                        ngayKetThuc != null ? sdf.format(ngayKetThuc) : "N/A", // Ngày kết thúc
                        hd.getTienCoc(), // Tiền cọc
                        hd.getDienBanDau(), // Điện ban đầu
                        hd.getNuocBanDau() // Nước ban đầu
                    };
                    modelActive.addRow(row);
                }
            }
            
            // Filter expired contracts by chi nhanh
            for (HopDong hd : expiredContracts) {
                // Get room info to check chi nhanh
                Phong phong = phongDAO.findById(hd.getID_Phong());
                if (phong != null && phong.getIdChiNhanh() == chiNhanhId) {
                    // Lấy tên người dùng
                    String tenNguoiKy = "N/A";
                    try {
                        NguoiDung nguoiDung = nguoiDungDAO.findById(hd.getID_NguoiDung());
                        if (nguoiDung != null) {
                            tenNguoiKy = nguoiDung.getTenNguoiDung();
                        }
                    } catch (Exception e) {
                        System.err.println("Lỗi khi lấy tên người dùng ID " + hd.getID_NguoiDung() + ": " + e.getMessage());
                    }
                    
                    // Calculate end date
                    Date ngayKetThuc = calculateEndDate(hd.getNgayTao(), hd.getThoiHan());
                    
                    Object[] row = {
                        hd.getID_HopDong(),
                        tenNguoiKy, // Tên người ký
                        phong.getSoPhong(), // Số phòng
                        sdf.format(hd.getNgayTao()), // Ngày bắt đầu
                        ngayKetThuc != null ? sdf.format(ngayKetThuc) : "N/A", // Ngày kết thúc
                        hd.getTienCoc(), // Tiền cọc
                        hd.getDienBanDau(), // Điện ban đầu
                        hd.getNuocBanDau() // Nước ban đầu
                    };
                    modelExpired.addRow(row);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lọc hợp đồng theo chi nhánh: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
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
            // dateBatDau: Ngày tạo (ngayTao)
            // txtTimKiem1: Thời hạn (thoiHan)
            // txtTimKiem3: Tiền cọc (tienCoc)
            // txtTimKiem4: Nước ban đầu (nuocBanDau)
            // txtTimKiem5: Điện ban đầu (dienBanDau)
            // cboNguoiKiHopDong: Người kí hợp đồng (ID_NguoiDung)
            // cboSoPhong: Mã phòng (ID_Phong)
            
            Date ngayTao = dateBatDau.getDate();
            if (ngayTao == null) {
                throw new IllegalArgumentException("Vui lòng chọn ngày bắt đầu");
            }
            
            int thoiHan = Integer.parseInt(txtTimKiem1.getText().trim()); // Assuming txtTimKiem1 is for ThoiHan
            int tienCoc = Integer.parseInt(txtTimKiem3.getText().trim());
            int nuocBanDau = Integer.parseInt(txtTimKiem4.getText().trim());
            int dienBanDau = Integer.parseInt(txtTimKiem5.getText().trim());
            
            // Validate end date matches calculated date
            Date calculatedEndDate = calculateEndDate(ngayTao, thoiHan);
            Date selectedEndDate = dateKetThucHopDong.getDate();
            if (calculatedEndDate != null && selectedEndDate != null) {
                // Allow small differences (same day)
                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();
                cal1.setTime(calculatedEndDate);
                cal2.setTime(selectedEndDate);
                
                if (cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR) ||
                    cal1.get(Calendar.DAY_OF_YEAR) != cal2.get(Calendar.DAY_OF_YEAR)) {
                    // If dates don't match, update the date picker with calculated date
                    dateKetThucHopDong.setDate(calculatedEndDate);
                }
            }
            
            // Extract ID from ComboBox string (e.g., "1 (Nguyễn Văn A)" -> "1")
            String selectedUser = (String) cboNguoiKiHopDong.getSelectedItem();
            if (selectedUser == null || selectedUser.equals("-- Chọn người dùng --")) {
                throw new IllegalArgumentException("Vui lòng chọn người dùng");
            }
            int idNguoiDung = Integer.parseInt(selectedUser.split(" ")[0]); 
            
            // Get room ID from room name (since we now store just room names in combo box)
            String selectedRoomName = (String) cboSoPhong.getSelectedItem();
            if (selectedRoomName == null || selectedRoomName.equals("-- Chọn phòng --")) {
                throw new IllegalArgumentException("Vui lòng chọn phòng");
            }
            
            // Find room ID by room name
            int idPhong = -1;
            List<Phong> phongs = phongDAO.findAll(); // Search in all rooms, not just available ones
            for (Phong p : phongs) {
                if (p.getSoPhong().equals(selectedRoomName)) {
                    idPhong = p.getIdPhong();
                    break;
                }
            }
            
            if (idPhong == -1) {
                throw new IllegalArgumentException("Không tìm thấy phòng đã chọn");
            }

            HopDong hopDong = new HopDong();
            // If currentHopDongId is set, it means we are updating, otherwise adding
            if (currentHopDongId != -1) {
                hopDong.setID_HopDong(currentHopDongId);
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu từ form: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }

    private void setForm(HopDong hd) {
        // Store the contract ID for editing
        currentHopDongId = hd.getID_HopDong();
        
        dateBatDau.setDate(hd.getNgayTao()); // Set Ngay tao
        txtTimKiem1.setText(String.valueOf(hd.getThoiHan())); // Set Thoi han
        txtTimKiem3.setText(String.valueOf(hd.getTienCoc())); // Set Tien coc
        txtTimKiem4.setText(String.valueOf(hd.getNuocBanDau())); // Set Nuoc ban dau
        txtTimKiem5.setText(String.valueOf(hd.getDienBanDau())); // Set Dien ban dau
        
        // Thiết lập txtTimKiem7 (Mã người dùng)

        // Set end date based on start date and duration
        Date endDate = calculateEndDate(hd.getNgayTao(), hd.getThoiHan());
        if (endDate != null) {
            dateKetThucHopDong.setDate(endDate);
        }

        // Khi chỉnh sửa hợp đồng, cần load lại danh sách người dùng bao gồm cả người đã có hợp đồng
        // để có thể chỉnh sửa hợp đồng hiện tại
        fillNguoiDungComboBoxForEdit(hd.getID_NguoiDung());
        
        // Set selected item in ComboBoxes based on ID
        setComboBoxSelectedItem(cboNguoiKiHopDong, String.valueOf(hd.getID_NguoiDung()));
        
        // Set room selection by room name instead of ID
        try {
            Phong phong = phongDAO.findById(hd.getID_Phong());
            if (phong != null) {
                // Set the chi nhanh first
                ChiNhanh chiNhanh = chiNhanhDAO.getById(phong.getIdChiNhanh());
                if (chiNhanh != null) {
                    for (int i = 0; i < cboChiNhanh.getItemCount(); i++) {
                        if (cboChiNhanh.getItemAt(i).equals(chiNhanh.getTenChiNhanh())) {
                            cboChiNhanh.setSelectedIndex(i);
                            break;
                        }
                    }
                    
                    // Temporarily load all rooms for this branch to include the current room
                    // even if it's rented (for editing existing contracts)
                    cboSoPhong.removeAllItems();
                    cboSoPhong.addItem("-- Chọn phòng --");
                    List<Phong> allRoomsInBranch = phongDAO.findByChiNhanh(chiNhanh.getID_ChiNhanh());
                    for (Phong p : allRoomsInBranch) {
                        cboSoPhong.addItem(p.getSoPhong());
                    }
                }
                
                // Set the room
                for (int i = 0; i < cboSoPhong.getItemCount(); i++) {
                    if (cboSoPhong.getItemAt(i).equals(phong.getSoPhong())) {
                        cboSoPhong.setSelectedIndex(i);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thiết lập thông tin phòng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
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
        // Reset contract ID for new contract
        currentHopDongId = -1;
        
        dateBatDau.setDate(new Date()); // Set current date for ngayTao
        txtTimKiem1.setText(""); // Clear Thoi han
        txtTimKiem3.setText(""); // Clear Tien coc
        txtTimKiem4.setText(""); // Clear Nuoc ban dau - will be auto-populated when room is selected
        txtTimKiem5.setText(""); // Clear Dien ban dau - will be auto-populated when room is selected
        txtTimKiem.setText(""); // Clear search field (if used)
        dateKetThucHopDong.setDate(null); // Clear end date

        // Reload the user combo box to show only available users for new contracts
        fillNguoiDungComboBox();
        
        // Set to default selections (first item which should be "-- Chọn ... --")
        cboChiNhanh.setSelectedIndex(0);
        if (cboNguoiKiHopDong.getItemCount() > 0) {
            cboNguoiKiHopDong.setSelectedIndex(0);
        }
        cboSoPhong.setSelectedIndex(0); // This will trigger onPhongSelected() and clear meter fields
        
        // Reset tables to show all contracts
        fillTableActiveContracts();
        fillTableExpiredContracts();
        
        // Calculate end date for current date and empty duration
        calculateAndSetEndDate();
        
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
            clearForm(); // This will reload the user combo box with available users
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

        if (currentHopDongId == -1) {
            JOptionPane.showMessageDialog(this, "Không có hợp đồng nào được chọn để cập nhật.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        HopDong hd = getForm();
        if (hd == null) return;

        try {
            // The ID is already set in getForm() method from currentHopDongId
            hopDongDAO.update(hd); // Changed to hopDongDAO
            JOptionPane.showMessageDialog(this, "Cập nhật hợp đồng thành công!");
            fillTableActiveContracts(); // Refresh tables
            fillTableExpiredContracts();
            clearForm(); // This will reload the user combo box with available users
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

        if (currentHopDongId == -1) {
            JOptionPane.showMessageDialog(this, "Không có hợp đồng nào được chọn để xóa.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa hợp đồng này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            hopDongDAO.delete(currentHopDongId); // Use stored contract ID
            JOptionPane.showMessageDialog(this, "Xóa hợp đồng thành công!");
            fillTableActiveContracts(); // Refresh tables
            fillTableExpiredContracts();
            clearForm(); // This will reload the user combo box with available users
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa hợp đồng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void editHopDong(JTable table) {
        currentIndex = table.getSelectedRow();
        if (currentIndex >= 0) {
            try {
                // Get ID_HopDong from the table model (first column)
                int hopDongId = (int) table.getValueAt(currentIndex, 0); 
                HopDong hd = hopDongDAO.selectById(hopDongId); // Changed to hopDongDAO
                if (hd != null) {
                    setForm(hd);
                    updateStatus();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu hợp đồng lên form: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    // Helper to update button states
    private void updateStatus() {
        // If currentHopDongId is -1, it's a new record (add mode)
        // If it has a value, it's an existing record (edit/delete mode)
        boolean isAdding = currentHopDongId == -1;

        btnThem.setEnabled(isAdding);
        btnSua.setEnabled(!isAdding);
        btnXoa.setEnabled(!isAdding);
        
        // Make txtTimKiem7 (Mã người dùng) non-editable
    }

    // New search method
    private void search() {
        String keyword = txtTimKiem.getText().trim();
        String selectedChiNhanh = (String) cboChiNhanh.getSelectedItem();
        
        if (keyword.isEmpty()) {
            // If search box is empty, check if chi nhanh is selected
            if (selectedChiNhanh != null && !selectedChiNhanh.equals("Tất cả")) {
                // Filter by selected chi nhanh
                try {
                    List<ChiNhanh> chiNhanhs = chiNhanhDAO.getAll();
                    int selectedChiNhanhId = -1;
                    for (ChiNhanh cn : chiNhanhs) {
                        if (cn.getTenChiNhanh().equals(selectedChiNhanh)) {
                            selectedChiNhanhId = cn.getID_ChiNhanh();
                            break;
                        }
                    }
                    if (selectedChiNhanhId != -1) {
                        fillTableByChiNhanh(selectedChiNhanhId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // Show all contracts if "Tất cả" selected and no search keyword
                fillTableActiveContracts();
                fillTableExpiredContracts();
            }
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

            // If chi nhanh is selected (not "Tất cả"), also filter by chi nhanh
            int selectedChiNhanhId = -1;
            if (selectedChiNhanh != null && !selectedChiNhanh.equals("Tất cả")) {
                List<ChiNhanh> chiNhanhs = chiNhanhDAO.getAll();
                for (ChiNhanh cn : chiNhanhs) {
                    if (cn.getTenChiNhanh().equals(selectedChiNhanh)) {
                        selectedChiNhanhId = cn.getID_ChiNhanh();
                        break;
                    }
                }
            }

            for (HopDong hd : searchResults) {
                // Check if need to filter by chi nhanh
                boolean includeContract = true;
                Phong phong = null;
                if (selectedChiNhanhId != -1) {
                    try {
                        phong = phongDAO.findById(hd.getID_Phong());
                        if (phong == null || phong.getIdChiNhanh() != selectedChiNhanhId) {
                            includeContract = false;
                        }
                    } catch (Exception e) {
                        includeContract = false;
                    }
                }
                
                if (includeContract) {
                    // Lấy thông tin phòng nếu chưa có
                    if (phong == null) {
                        try {
                            phong = phongDAO.findById(hd.getID_Phong());
                        } catch (Exception e) {
                            System.err.println("Lỗi khi lấy thông tin phòng ID " + hd.getID_Phong() + ": " + e.getMessage());
                        }
                    }
                    
                    // Lấy tên người dùng
                    String tenNguoiKy = "N/A";
                    try {
                        NguoiDung nguoiDung = nguoiDungDAO.findById(hd.getID_NguoiDung());
                        if (nguoiDung != null) {
                            tenNguoiKy = nguoiDung.getTenNguoiDung();
                        }
                    } catch (Exception e) {
                        System.err.println("Lỗi khi lấy tên người dùng ID " + hd.getID_NguoiDung() + ": " + e.getMessage());
                    }
                    
                    String soPhong = phong != null ? phong.getSoPhong() : "N/A";
                    Date ngayKetThuc = calculateEndDate(hd.getNgayTao(), hd.getThoiHan());
                    
                    Object[] row = {
                        hd.getID_HopDong(),
                        tenNguoiKy, // Tên người ký
                        soPhong, // Số phòng
                        sdf.format(hd.getNgayTao()), // Ngày bắt đầu
                        ngayKetThuc != null ? sdf.format(ngayKetThuc) : "N/A", // Ngày kết thúc
                        hd.getTienCoc(), // Tiền cọc
                        hd.getDienBanDau(), // Điện ban đầu
                        hd.getNuocBanDau() // Nước ban đầu
                    };
                    if (ngayKetThuc != null && ngayKetThuc.after(new Date())) {
                        modelActive.addRow(row);
                    } else {
                        modelExpired.addRow(row);
                    }
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
        txtTimKiem1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtTimKiem3 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtTimKiem4 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtTimKiem5 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cboNguoiKiHopDong = new javax.swing.JComboBox<>();
        cboSoPhong = new javax.swing.JComboBox<>();
        btnTimKiem = new javax.swing.JButton();
        dateKetThucHopDong = new com.toedter.calendar.JDateChooser();
        dateBatDau = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
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
        cboChiNhanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboChiNhanhActionPerformed(evt);
            }
        });

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

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel11.setText("Số phòng");

        cboNguoiKiHopDong.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboSoPhong.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        jLabel2.setText("Ngày kết thúc");

        jLabel3.setText("Tháng");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboChiNhanh, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnTimKiem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addComponent(txtTimKiem5, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTimKiem1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateKetThucHopDong, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboNguoiKiHopDong, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboSoPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(334, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cboChiNhanh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimKiem))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTimKiem1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(dateBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel11)
                            .addComponent(cboNguoiKiHopDong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboSoPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(dateKetThucHopDong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã hợp đồng", "Tên người ký", "Số phòng", "Ngày bắt đầu", "Ngày kết thúc", "Tiền cọc", "Điện ban đầu", "Nước ban đầu"
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
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã hợp đồng", "Tên người ký", "Số phòng", "Ngày bắt đầu", "Ngày kết thúc", "Tiền cọc", "Điện ban đầu", "Nước ban đầu"
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

    private void cboChiNhanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboChiNhanhActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboChiNhanhActionPerformed

    // Auto-populate meter readings when room is selected
    private void onPhongSelected() {
        String selectedPhong = (String) cboSoPhong.getSelectedItem();
        if (selectedPhong == null || selectedPhong.equals("-- Chọn phòng --")) {
            // Clear meter reading fields if no room selected
            txtTimKiem4.setText(""); // Nước
            txtTimKiem5.setText(""); // Điện
            return;
        }

        try {
            System.out.println("Auto-populating meter readings for room: " + selectedPhong);
            
            // Find the room by room number to get room ID
            List<Phong> phongs = phongDAO.findAll();
            Integer roomId = null;
            for (Phong phong : phongs) {
                if (phong.getSoPhong().equals(selectedPhong)) {
                    roomId = phong.getIdPhong();
                    break;
                }
            }

            if (roomId != null) {
                System.out.println("Found room ID: " + roomId + " for room: " + selectedPhong);
                
                // Get latest meter reading for this room
                DienNuoc latestMeterReading = dienNuocDAO.findLatestByPhong(roomId);
                if (latestMeterReading != null) {
                    // Auto-populate with latest meter readings (soDienMoi and soNuocMoi from previous period)
                    int latestWater = latestMeterReading.getSoNuocMoi();
                    int latestElectric = latestMeterReading.getSoDienMoi();
                    
                    txtTimKiem4.setText(String.valueOf(latestWater)); // Nước ban đầu
                    txtTimKiem5.setText(String.valueOf(latestElectric)); // Điện ban đầu
                    
                    System.out.println("Auto-populated: Water=" + latestWater + ", Electric=" + latestElectric);
                } else {
                    // No previous meter readings found, set to 0 or clear fields
                    txtTimKiem4.setText("0");
                    txtTimKiem5.setText("0");
                    System.out.println("No previous meter readings found for room: " + selectedPhong + ", set to 0");
                }
            } else {
                System.err.println("Could not find room ID for room: " + selectedPhong);
                txtTimKiem4.setText("");
                txtTimKiem5.setText("");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tự động điền số điện nước: " + e.getMessage());
            e.printStackTrace();
            // On error, clear the fields but don't show error dialog to user
            txtTimKiem4.setText("");
            txtTimKiem5.setText("");
        }
    }

    /**
     * Show information about the end date calculation
     */
    private void showEndDateInfo() {
        try {
            Date startDate = dateBatDau.getDate();
            String durationStr = txtTimKiem1.getText().trim();
            
            if (startDate != null && !durationStr.isEmpty()) {
                int duration = Integer.parseInt(durationStr);
                Date endDate = calculateEndDate(startDate, duration);
                
                if (endDate != null) {
                    String message = String.format(
                        "Ngày bắt đầu: %s\n" +
                        "Thời hạn: %d tháng\n" +
                        "Ngày kết thúc (tự động): %s", 
                        sdf.format(startDate), 
                        duration, 
                        sdf.format(endDate)
                    );
                    
                    JOptionPane.showMessageDialog(this, message, "Thông tin ngày kết thúc", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn ngày bắt đầu và nhập thời hạn để tính ngày kết thúc.", 
                    "Thông tin", 
                    JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi tính toán ngày kết thúc: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboChiNhanh;
    private javax.swing.JComboBox<String> cboNguoiKiHopDong;
    private javax.swing.JComboBox<String> cboSoPhong;
    private com.toedter.calendar.JDateChooser dateBatDau;
    private com.toedter.calendar.JDateChooser dateKetThucHopDong;
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
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
    private javax.swing.JTextField txtTimKiem3;
    private javax.swing.JTextField txtTimKiem4;
    private javax.swing.JTextField txtTimKiem5;
    // End of variables declaration//GEN-END:variables
}
