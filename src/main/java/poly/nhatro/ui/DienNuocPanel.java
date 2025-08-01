package poly.nhatro.ui;

import poly.nhatro.entity.DienNuoc;
import poly.nhatro.dao.impl.DienNuocDAOImpl;
import poly.nhatro.dao.DienNuocDAO;
import poly.nhatro.dao.ChiNhanhDAO;
import poly.nhatro.dao.impl.ChiNhanhDAOImpl;
import poly.nhatro.entity.ChiNhanh;
import poly.nhatro.dao.impl.PhongDaoImpl;
import poly.nhatro.entity.Phong;
import java.util.*;
import java.util.HashSet;
import java.util.Set;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.swing.JFileChooser;
import java.io.*;
import java.text.SimpleDateFormat;

public class DienNuocPanel extends javax.swing.JPanel implements dienNuocController {

    private DienNuocDAO dienNuocDao;
    private ChiNhanhDAO chiNhanhDao;
    private PhongDaoImpl phongDao;
    private Map<String, Integer> chiNhanhMap;
    private Map<String, Integer> phongMap;
    private int currentIndex = -1;
    private List<DienNuoc> currentList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private boolean isSettingForm = false; // Flag để tránh trigger events khi đang set form
    
    /**
     * Tìm bản ghi điện nước của tháng trước cho phòng được chỉ định
     * @param idPhong ID phòng
     * @param currentDate Ngày tháng hiện tại
     * @return Bản ghi điện nước tháng trước, null nếu không tìm thấy
     */
    private DienNuoc findPreviousMonthRecord(Integer idPhong, Date currentDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.MONTH, -1); // Lùi về tháng trước
        
        int previousMonth = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH starts from 0
        int previousYear = calendar.get(Calendar.YEAR);
        
        return dienNuocDao.findByPhongThangNam(idPhong, previousMonth, previousYear);
    }
    
    /**
     * Kiểm tra xem có bản ghi nào tồn tại cho phòng được chỉ định
     * @param idPhong ID phòng
     * @return true nếu có ít nhất 1 bản ghi, false nếu không có
     */
    private boolean hasAnyRecordForRoom(Integer idPhong) {
        try {
            // Sử dụng SQL để đếm số bản ghi
            List<DienNuoc> allRecords = dienNuocDao.findAll();
            return allRecords.stream().anyMatch(record -> record.getIdPhong() == idPhong);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Tự động fill số điện nước cũ dựa trên phòng và thời gian được chọn
     */
    private void autoFillSoDienNuocCu() {
        // Tránh gọi nhiều lần khi đang thiết lập form
        if (isSettingForm) {
            return;
        }
        
        try {
            // Lấy thông tin phòng được chọn
            if (cboMaPhong.getSelectedItem() == null || cboMaPhong.getSelectedItem().toString().isEmpty()) {
                // Nếu chưa chọn phòng thì xóa số điện nước cũ
                txtSoDienCu.setText("");
                txtSoNuocCu.setText("");
                return;
            }
            
            String selectedSoPhong = cboMaPhong.getSelectedItem().toString();
            
            // Kiểm tra phongMap có được khởi tạo chưa
            if (phongMap == null || phongMap.isEmpty()) {
                return; // Không thể xử lý nếu phongMap chưa sẵn sàng
            }
            
            Integer idPhong = phongMap.get(selectedSoPhong);
            
            if (idPhong == null) {
                txtSoDienCu.setText("");
                txtSoNuocCu.setText("");
                return;
            }
            
            // Lấy thời gian được chọn, nếu null thì dùng thời gian hiện tại
            Date selectedDate = dateThoiGianTao.getDate();
            if (selectedDate == null) {
                selectedDate = new Date();
            }
            
            // Tìm bản ghi tháng trước
            DienNuoc previousMonthRecord = findPreviousMonthRecord(idPhong, selectedDate);
            
            if (previousMonthRecord != null) {
                // Nếu có bản ghi tháng trước, fill số điện/nước cũ = số điện/nước mới tháng trước
                int soDienCu = previousMonthRecord.getSoDienMoi();
                int soNuocCu = previousMonthRecord.getSoNuocMoi();
                
                txtSoDienCu.setText(String.valueOf(soDienCu));
                txtSoNuocCu.setText(String.valueOf(soNuocCu));
            } else {
                // Nếu không có bản ghi tháng trước, fill số 0 (tháng đầu tiên)
                txtSoDienCu.setText("0");
                txtSoNuocCu.setText("0");
            }
            
        } catch (Exception e) {
            // Nếu có lỗi, đặt về 0
            txtSoDienCu.setText("0");
            txtSoNuocCu.setText("0");
        }
    }

    public DienNuocPanel() {
        initComponents();
        this.dienNuocDao = new DienNuocDAOImpl();
        this.chiNhanhDao = new ChiNhanhDAOImpl();
        this.phongDao = new PhongDaoImpl(); // Khởi tạo PhongDaoImpl
        this.chiNhanhMap = new HashMap<>();
        this.phongMap = new HashMap<>(); // Khởi tạo phongMap
        init();
        // Vô hiệu hóa các trường tự động tính
        txtSoDienCu.setEditable(false);
        txtSoNuocCu.setEditable(false);
        txtSoDienCu.setEnabled(false); // Hoàn toàn vô hiệu hóa
        txtSoNuocCu.setEnabled(false); // Hoàn toàn vô hiệu hóa
    }

    @Override
    public void init() {
        loadChiNhanhToComboBox();
        txtID_DienNuoc.setEditable(false); // ID Điện nước không cho sửa
        dateThoiGianTao.setDate(new Date()); // Đặt ngày hiện tại làm mặc định cho JDateChooser
        filterAndFillTable(); // Gọi phương thức này để lọc và điền dữ liệu ban đầu
        clear();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        lblHopDong5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDienNuoc = new javax.swing.JTable();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        btnExcel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtSoDienCu = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cboChiNhanh = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        txtID_DienNuoc = new javax.swing.JTextField();
        txtSoDienMoi = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtSoNuocMoi = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cboMaPhong = new javax.swing.JComboBox<>();
        dateThoiGianTao = new com.toedter.calendar.JDateChooser();
        txtSoNuocCu = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));

        lblHopDong5.setFont(new java.awt.Font("Segoe UI Black", 3, 36)); // NOI18N
        lblHopDong5.setForeground(new java.awt.Color(0, 0, 255));
        lblHopDong5.setText("Điện nước");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblHopDong5, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblHopDong5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblDienNuoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                " Tên chi nhánh", "Mã điện nước", "Tên phòng", "Số điện cũ", "Số điện mới", "Số nước cũ", "Số nước mới", "Thời gian tạo"
            }
        ));
        tblDienNuoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDienNuocMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDienNuoc);

        btnThem.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnLamMoi.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        btnLamMoi.setText("Làm mới");
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        btnExcel.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        btnExcel.setText("Xuất Excel");
        btnExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(153, 218, 250));

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 3, 18)); // NOI18N
        jLabel2.setText("Tên phòng");

        jLabel6.setFont(new java.awt.Font("Helvetica Neue", 3, 18)); // NOI18N
        jLabel6.setText("Số nước cũ");

        jLabel7.setFont(jLabel7.getFont().deriveFont((jLabel7.getFont().getStyle() | java.awt.Font.ITALIC) | java.awt.Font.BOLD, jLabel7.getFont().getSize()+5));
        jLabel7.setText("Số điện cũ");

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 3, 18)); // NOI18N
        jLabel1.setText("Tên chi nhánh");

        cboChiNhanh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboChiNhanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboChiNhanhActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Helvetica Neue", 3, 18)); // NOI18N
        jLabel8.setText("Mã Điện Nước");

        jLabel9.setFont(new java.awt.Font("Helvetica Neue", 3, 18)); // NOI18N
        jLabel9.setText("Số điện mới");

        jLabel10.setFont(new java.awt.Font("Helvetica Neue", 3, 18)); // NOI18N
        jLabel10.setText("Số nước mới");

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 3, 18)); // NOI18N
        jLabel3.setText("Thời gian tạo");

        cboMaPhong.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboMaPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMaPhongActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(104, 104, 104)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtID_DienNuoc)
                    .addComponent(cboMaPhong, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cboChiNhanh, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(dateThoiGianTao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(82, 82, 82)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtSoDienMoi)
                        .addComponent(txtSoDienCu, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                        .addComponent(txtSoNuocMoi))
                    .addComponent(txtSoNuocCu, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtSoNuocCu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(txtSoDienMoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(75, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(cboChiNhanh, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel7)
                                .addComponent(txtSoDienCu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cboMaPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dateThoiGianTao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtID_DienNuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10)
                            .addComponent(txtSoNuocMoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20))))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 231, Short.MAX_VALUE)
                        .addComponent(btnExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnLamMoi, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                        .addComponent(btnExcel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnXoa, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSua, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnThem, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(21, 21, 21)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        this.create();
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        this.update();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        this.delete();
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        // TODO add your handling code here:
        this.clear();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void btnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelActionPerformed
        this.Excel();
    }//GEN-LAST:event_btnExcelActionPerformed

    private void tblDienNuocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDienNuocMouseClicked
        if (evt.getClickCount() == 1) { // Chỉ xử lý khi là một click đơn
            edit(); // Gọi phương thức edit() để hiển thị dữ liệu lên form
        }
        txtSoDienCu.setEditable(false);
    txtSoNuocCu.setEditable(false);
    }//GEN-LAST:event_tblDienNuocMouseClicked

    private void cboChiNhanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboChiNhanhActionPerformed
        if (!isSettingForm) { // Chỉ xử lý khi không phải đang set form
            loadPhongToComboBox();
            filterAndFillTable();
        }
    }//GEN-LAST:event_cboChiNhanhActionPerformed

    private void cboMaPhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMaPhongActionPerformed
        if (!isSettingForm) { // Chỉ xử lý khi không phải đang set form
            autoFillSoDienNuocCu();
        }
    }//GEN-LAST:event_cboMaPhongActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExcel;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboChiNhanh;
    private javax.swing.JComboBox<String> cboMaPhong;
    private com.toedter.calendar.JDateChooser dateThoiGianTao;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblHopDong5;
    private javax.swing.JTable tblDienNuoc;
    private javax.swing.JTextField txtID_DienNuoc;
    private javax.swing.JTextField txtSoDienCu;
    private javax.swing.JTextField txtSoDienMoi;
    private javax.swing.JTextField txtSoNuocCu;
    private javax.swing.JTextField txtSoNuocMoi;
    // End of variables declaration//GEN-END:variables

    @Override
    public void open() {
// Method can be used to open panel, no specific logic needed here
    }

    @Override
    public void setForm(DienNuoc entity) {
        isSettingForm = true; // Bật flag để tránh trigger events
        try {
            if (entity != null) {
                txtID_DienNuoc.setText(String.valueOf(entity.getIdDienNuoc()));
                if (entity.getTenChiNhanh() != null) {
                    cboChiNhanh.setSelectedItem(entity.getTenChiNhanh());
                    // Load phòng theo chi nhánh được chọn để đảm bảo có đủ dữ liệu
                    loadPhongToComboBox();
                } else {
                    cboChiNhanh.setSelectedIndex(0);
                }
    // Đặt mã phòng vào cboMaPhong
                if (entity.getSoPhong() != null) {
                    cboMaPhong.setSelectedItem(entity.getSoPhong()); // Sử dụng soPhong để chọn item
                } else {
                    cboMaPhong.setSelectedIndex(0);
                }
                txtSoDienCu.setText(String.valueOf(entity.getSoDienCu()));
                txtSoDienMoi.setText(String.valueOf(entity.getSoDienMoi()));
                txtSoNuocCu.setText(String.valueOf(entity.getSoNuocCu()));
                txtSoNuocMoi.setText(String.valueOf(entity.getSoNuocMoi()));
                if (entity.getThangNam() != null) {
                    dateThoiGianTao.setDate(entity.getThangNam());
                } else {
                    dateThoiGianTao.setDate(new Date());
                }
            } else {
                clear();
            }
        } finally {
            isSettingForm = false; // Tắt flag sau khi hoàn thành
        }
    }

    public boolean validateForm() {
// This validation will be handled differently for create and update actions
// We'll keep a base validation here but specific checks will be done in create() and update()
        if (cboChiNhanh.getSelectedItem() == null || cboChiNhanh.getSelectedItem().equals("Tất cả")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một Chi nhánh hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            cboChiNhanh.requestFocusInWindow();
            return false;
        }
        if (cboMaPhong.getSelectedItem() == null || cboMaPhong.getSelectedItem().toString().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã phòng không được để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            cboMaPhong.requestFocusInWindow();
            return false;
        }
        try {
            String selectedSoPhong = cboMaPhong.getSelectedItem().toString();
            Integer idPhong = phongMap.get(selectedSoPhong);
            if (idPhong == null) {
                JOptionPane.showMessageDialog(this, "Mã phòng không tồn tại trong chi nhánh đã chọn.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                cboMaPhong.requestFocusInWindow();
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi kiểm tra Mã phòng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public DienNuoc getForm() {
// Phương thức này giờ sẽ chỉ lấy những dữ liệu cơ bản
        DienNuoc dienNuoc = new DienNuoc();
// Giữ lại ID_DienNuoc nếu đang ở chế độ sửa
        if (currentIndex >= 0 && currentIndex < currentList.size()) {
            dienNuoc.setIdDienNuoc(currentList.get(currentIndex).getIdDienNuoc());
        }
        String selectedSoPhong = cboMaPhong.getSelectedItem().toString();
        Integer idPhong = phongMap.get(selectedSoPhong); // Lấy ID phòng từ map
        if (idPhong == null) {
            JOptionPane.showMessageDialog(this, "Mã phòng không hợp lệ hoặc không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        dienNuoc.setIdPhong(idPhong);
        try {
            if (txtID_DienNuoc.getText() != null && !txtID_DienNuoc.getText().trim().isEmpty()) {
                dienNuoc.setIdDienNuoc(Integer.parseInt(txtID_DienNuoc.getText().trim()));
            }
            dienNuoc.setSoDienCu((int) Double.parseDouble(txtSoDienCu.getText().trim()));
            dienNuoc.setSoDienMoi((int) Double.parseDouble(txtSoDienMoi.getText().trim()));
            dienNuoc.setSoNuocCu((int) Double.parseDouble(txtSoNuocCu.getText().trim()));
            dienNuoc.setSoNuocMoi((int) Double.parseDouble(txtSoNuocMoi.getText().trim()));
            dienNuoc.setThangNam(dateThoiGianTao.getDate());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số điện hoặc số nước không hợp lệ. Vui lòng nhập số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu từ form: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
        return dienNuoc;
    }

    @Override
    public void create() {
        if (!validateForm()) {
            return;
        }
        
        // Validation cho nút thêm
        if (txtSoDienMoi.getText().trim().isEmpty() || txtSoNuocMoi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số điện mới và Số nước mới không được để trống khi thêm.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String selectedSoPhong = cboMaPhong.getSelectedItem().toString();
        Integer idPhong = phongMap.get(selectedSoPhong);
        if (idPhong == null) {
            JOptionPane.showMessageDialog(this, "Mã phòng không hợp lệ hoặc không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Date selectedDate = dateThoiGianTao.getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thời gian tạo hóa đơn.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Kiểm tra trùng lặp bản ghi cho tháng hiện tại
            DienNuoc existingDienNuoc = dienNuocDao.findByPhongThangNam(idPhong,
                    selectedDate.getMonth() + 1, selectedDate.getYear() + 1900);
            if (existingDienNuoc != null) {
                JOptionPane.showMessageDialog(this, "Đã tồn tại bản ghi điện nước cho phòng này trong tháng và năm đã chọn.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Tìm bản ghi tháng trước để lấy số điện/nước cũ
            DienNuoc previousMonthRecord = findPreviousMonthRecord(idPhong, selectedDate);
            
            int soDienCu = 0;
            int soNuocCu = 0;
            
            if (previousMonthRecord != null) {
                // Nếu có bản ghi tháng trước, số điện/nước cũ = số điện/nước mới tháng trước
                soDienCu = previousMonthRecord.getSoDienMoi();
                soNuocCu = previousMonthRecord.getSoNuocMoi();
                
                // Kiểm tra tính liên tục tháng (không được bỏ qua tháng nào)
                Calendar prevCal = Calendar.getInstance();
                prevCal.setTime(previousMonthRecord.getThangNam());
                prevCal.add(Calendar.MONTH, 1); // Tháng kế tiếp sau tháng trước
                
                Calendar currentCal = Calendar.getInstance();
                currentCal.setTime(selectedDate);
                
                // So sánh tháng và năm (bỏ qua ngày)
                if (prevCal.get(Calendar.YEAR) != currentCal.get(Calendar.YEAR) || 
                    prevCal.get(Calendar.MONTH) != currentCal.get(Calendar.MONTH)) {
                    String expectedMonth = String.format("%02d/%d", 
                        prevCal.get(Calendar.MONTH) + 1, prevCal.get(Calendar.YEAR));
                    String actualMonth = String.format("%02d/%d", 
                        currentCal.get(Calendar.MONTH) + 1, currentCal.get(Calendar.YEAR));
                    
                    JOptionPane.showMessageDialog(this, 
                        "Không thể bỏ qua tháng! Tháng tiếp theo phải là " + expectedMonth + 
                        " thay vì " + actualMonth + ".\n" +
                        "Vui lòng tạo hóa đơn theo thứ tự tháng liên tục.", 
                        "Lỗi tính liên tục", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                // Nếu không có bản ghi tháng trước, kiểm tra xem có bản ghi nào cho phòng này không
                if (hasAnyRecordForRoom(idPhong)) {
                    JOptionPane.showMessageDialog(this, 
                        "Phòng này đã có các bản ghi điện nước từ trước. \n" +
                        "Bạn phải tạo hóa đơn liên tục theo tháng, không được bỏ qua tháng nào.\n" +
                        "Vui lòng tạo hóa đơn cho tháng tiếp theo sau tháng cuối cùng đã có.", 
                        "Lỗi tính liên tục", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Nếu đây là tháng đầu tiên cho phòng này thì số cũ = 0
                soDienCu = 0;
                soNuocCu = 0;
            }
            
            // Tạo bản ghi mới
            DienNuoc newDienNuoc = new DienNuoc();
            newDienNuoc.setIdPhong(idPhong);
            newDienNuoc.setSoDienCu(soDienCu);
            newDienNuoc.setSoNuocCu(soNuocCu);
            newDienNuoc.setSoDienMoi((int) Double.parseDouble(txtSoDienMoi.getText().trim()));
            newDienNuoc.setSoNuocMoi((int) Double.parseDouble(txtSoNuocMoi.getText().trim()));
            newDienNuoc.setThangNam(selectedDate);
            
            // Validation: số mới phải >= số cũ
            if (newDienNuoc.getSoDienMoi() < newDienNuoc.getSoDienCu()) {
                JOptionPane.showMessageDialog(this, 
                    "Số điện mới (" + newDienNuoc.getSoDienMoi() + ") không thể nhỏ hơn số điện cũ (" + newDienNuoc.getSoDienCu() + ")!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (newDienNuoc.getSoNuocMoi() < newDienNuoc.getSoNuocCu()) {
                JOptionPane.showMessageDialog(this, 
                    "Số nước mới (" + newDienNuoc.getSoNuocMoi() + ") không thể nhỏ hơn số nước cũ (" + newDienNuoc.getSoNuocCu() + ")!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Tạo mới và lấy ID tự động từ DAO
            DienNuoc createdDienNuoc = dienNuocDao.create(newDienNuoc);
            
            // Hiển thị ID trên form ngay lập tức
            String successMessage = "Thêm DienNuoc thành công.";
            if (createdDienNuoc != null && createdDienNuoc.getIdDienNuoc() > 0) {
                txtID_DienNuoc.setText(String.valueOf(createdDienNuoc.getIdDienNuoc()));
                successMessage += " ID: " + createdDienNuoc.getIdDienNuoc();
            } else {
                txtID_DienNuoc.setText("");
                successMessage += " ID sẽ được hiển thị sau khi làm mới.";
            }
            
            // Tải lại bảng để hiển thị bản ghi mới
            filterAndFillTable();
            
            JOptionPane.showMessageDialog(this, successMessage, "Thành công", JOptionPane.INFORMATION_MESSAGE);
            
            // Cập nhật hiển thị số điện/nước cũ trên form để người dùng thấy
            txtSoDienCu.setText(String.valueOf(soDienCu));
            txtSoNuocCu.setText(String.valueOf(soNuocCu));
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số điện hoặc số nước không hợp lệ. Vui lòng nhập số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm mới: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

  @Override
public void update() {
    if (currentIndex < 0) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn một bản ghi để cập nhật.");
        return;
    }

    String soDienMoiStr = txtSoDienMoi.getText().trim();
    String soNuocMoiStr = txtSoNuocMoi.getText().trim();
    String soDienCuStr = txtSoDienCu.getText().trim();
    String soNuocCuStr = txtSoNuocCu.getText().trim();

    // Thêm validation để kiểm tra các trường không được để trống
    if (soDienMoiStr.isEmpty() || soNuocMoiStr.isEmpty() || soDienCuStr.isEmpty() || soNuocCuStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Số điện cũ, Số nước cũ, Số điện mới và Số nước mới không được để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Thêm kiểm tra ID_Phong từ cboMaPhong để tránh lỗi FOREIGN KEY
    String selectedSoPhong = (String) cboMaPhong.getSelectedItem();
    Integer idPhong = phongMap.get(selectedSoPhong);
    if (idPhong == null) {
        JOptionPane.showMessageDialog(this, "Mã phòng không hợp lệ hoặc không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        DienNuoc updatedDienNuoc = currentList.get(currentIndex);
        
        // Cập nhật ID_Phong mới nhất
        updatedDienNuoc.setIdPhong(idPhong);
        
        // Cập nhật giá trị mới từ các trường văn bản
        updatedDienNuoc.setSoDienMoi((int) Double.parseDouble(soDienMoiStr));
        updatedDienNuoc.setSoNuocMoi((int) Double.parseDouble(soNuocMoiStr));
        updatedDienNuoc.setSoDienCu((int) Double.parseDouble(soDienCuStr));
        updatedDienNuoc.setSoNuocCu((int) Double.parseDouble(soNuocCuStr));
        
        // Cập nhật ngày tháng
        if (dateThoiGianTao.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Tháng năm không được để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        updatedDienNuoc.setThangNam(dateThoiGianTao.getDate());

        // Kiểm tra trùng lặp tháng năm và phòng, nhưng bỏ qua bản ghi hiện tại đang sửa
        DienNuoc existingDienNuoc = dienNuocDao.findByPhongThangNam(updatedDienNuoc.getIdPhong(), updatedDienNuoc.getThangNam().getMonth() + 1, updatedDienNuoc.getThangNam().getYear() + 1900);
        if (existingDienNuoc != null && existingDienNuoc.getIdDienNuoc() != updatedDienNuoc.getIdDienNuoc()) {
            JOptionPane.showMessageDialog(this, "Đã tồn tại bản ghi điện nước cho phòng này trong tháng và năm đã chọn.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        dienNuocDao.update(updatedDienNuoc);
        filterAndFillTable(); // Cập nhật lại bảng sau khi sửa
        clear();
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Số điện hoặc số nước không hợp lệ. Vui lòng chỉ nhập số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
    } catch (RuntimeException e) {
        JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

    @Override
    public void delete() {
        if (currentIndex < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một bản ghi để xóa.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa bản ghi này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int dienNuocIdToDelete = currentList.get(currentIndex).getIdDienNuoc();
                dienNuocDao.deleteById(dienNuocIdToDelete);
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                filterAndFillTable(); // Cập nhật lại bảng sau khi xóa
                clear();
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi không xác định khi xóa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void clear() {
        cboChiNhanh.setSelectedIndex(0);
        cboMaPhong.setSelectedIndex(0); // Đặt lại cboMaPhong về mặc định
        txtSoDienCu.setText("");
        txtSoDienMoi.setText("");
        txtSoNuocCu.setText("");
        txtSoNuocMoi.setText("");
        txtID_DienNuoc.setText("");
        dateThoiGianTao.setDate(new Date()); // Đặt lại ngày hiện tại cho JDateChooser
        currentIndex = -1;
        setEditable(true);
        txtID_DienNuoc.setEditable(false); // Đảm bảo ID không sửa được
    }

    @Override
    public void edit() {
        int selectedRow = tblDienNuoc.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < currentList.size()) {
            currentIndex = selectedRow;
            DienNuoc selectedDienNuoc = currentList.get(selectedRow);
            setForm(selectedDienNuoc);
            // Không cần gọi setEditable(true) vì setForm() đã thiết lập đúng rồi
            // Chỉ đảm bảo ID Điện nước và các trường tự động tính không được sửa
            txtID_DienNuoc.setEditable(false);
            txtSoDienCu.setEditable(false);
            txtSoDienCu.setEnabled(false);
            txtSoNuocCu.setEditable(false);
            txtSoNuocCu.setEnabled(false);
        } else {
            clear();
        }
    }

    @Override
    public void setEditable(boolean editable) {
        cboMaPhong.setEnabled(editable); // Điều khiển cboMaPhong
        // Các trường tự động tính luôn bị vô hiệu hóa
        txtSoDienCu.setEditable(false);
        txtSoDienCu.setEnabled(false);
        txtSoNuocCu.setEditable(false);
        txtSoNuocCu.setEnabled(false);
        // Các trường có thể nhập
        txtSoDienMoi.setEditable(editable);
        txtSoNuocMoi.setEditable(editable);
        dateThoiGianTao.setEnabled(editable); // dateThoiGianTao có thể chỉnh sửa
        cboChiNhanh.setEnabled(editable);
        btnThem.setEnabled(editable);
        btnSua.setEnabled(editable);
        btnXoa.setEnabled(editable);
        txtID_DienNuoc.setEditable(false); // Mã điện nước không cho sửa
    }
// Phương thức mới để vừa lọc vừa điền dữ liệu vào bảng

    public void filterAndFillTable() {
        DefaultTableModel model = (DefaultTableModel) tblDienNuoc.getModel();
        model.setRowCount(0); // Xóa tất cả các hàng hiện có
        String selectedChiNhanhName = (String) cboChiNhanh.getSelectedItem();
        try {
            List<DienNuoc> filteredList;
            if (selectedChiNhanhName == null || selectedChiNhanhName.equals("Tất cả")) {
                filteredList = dienNuocDao.findAll(); // Lấy tất cả nếu chọn "Tất cả"
            } else {
                Integer idChiNhanh = chiNhanhMap.get(selectedChiNhanhName);
                if (idChiNhanh != null) {
                    filteredList = dienNuocDao.findByChiNhanhId(idChiNhanh); // Lọc theo ID chi nhánh
                } else {
// Trường hợp không tìm thấy ID chi nhánh (có thể xảy ra nếu dữ liệu bị lỗi)
                    filteredList = new ArrayList<>(); // Trả về danh sách rỗng
                    JOptionPane.showMessageDialog(this, "Không tìm thấy ID cho chi nhánh đã chọn: " + selectedChiNhanhName, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
            currentList = filteredList;
            
            // Điền dữ liệu vào bảng từ currentList đã lọc
            for (DienNuoc dn : currentList) {
                Object[] row = {
                    dn.getTenChiNhanh(),
                    dn.getIdDienNuoc(),
                    dn.getSoPhong(),
                    dn.getSoDienCu(),
                    dn.getSoDienMoi(),
                    dn.getSoNuocCu(),
                    dn.getSoNuocMoi(),
                    dn.getThangNam() != null ? dateFormat.format(dn.getThangNam()) : "" // Định dạng ngày tháng
                };
                model.addRow(row);
            }
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lọc và tải dữ liệu vào bảng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi không xác định khi lọc và tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
// fillToTable giờ chỉ gọi filterAndFillTable()

    @Override
    public void fillToTable() {
        filterAndFillTable();
    }

    private void loadChiNhanhToComboBox() {
        cboChiNhanh.removeAllItems();
        cboChiNhanh.addItem("Tất cả");
        chiNhanhMap.clear();
        try {
            List<ChiNhanh> chiNhanhs = chiNhanhDao.getAll();
            for (ChiNhanh cn : chiNhanhs) {
                cboChiNhanh.addItem(cn.getTenChiNhanh());
                chiNhanhMap.put(cn.getTenChiNhanh(), cn.getID_ChiNhanh());
            }
// Sau khi tải chi nhánh, tải phòng cho chi nhánh đầu tiên (hoặc "Tất cả")
            loadPhongToComboBox();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách chi nhánh: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
// Phương thức mới để tải mã phòng vào cboMaPhong dựa trên chi nhánh được chọn

    private void loadPhongToComboBox() {
        cboMaPhong.removeAllItems();
        phongMap.clear();
        String selectedChiNhanhName = (String) cboChiNhanh.getSelectedItem();
        
        // Sử dụng Set để tránh duplicate
        Set<String> addedRooms = new HashSet<>();
        
        if (selectedChiNhanhName == null || selectedChiNhanhName.equals("Tất cả")) {
            try {
                List<Phong> phongs = phongDao.findAll();
                for (Phong p : phongs) {
                    // Chỉ thêm nếu chưa có trong Set (tránh duplicate)
                    if (!addedRooms.contains(p.getSoPhong())) {
                        cboMaPhong.addItem(p.getSoPhong());
                        phongMap.put(p.getSoPhong(), p.getIdPhong());
                        addedRooms.add(p.getSoPhong());
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách phòng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            Integer idChiNhanh = chiNhanhMap.get(selectedChiNhanhName);
            if (idChiNhanh != null) {
                try {
                    List<Phong> phongs = phongDao.findByChiNhanh(idChiNhanh);
                    for (Phong p : phongs) {
                        // Chỉ thêm nếu chưa có trong Set (tránh duplicate)
                        if (!addedRooms.contains(p.getSoPhong())) {
                            cboMaPhong.addItem(p.getSoPhong());
                            phongMap.put(p.getSoPhong(), p.getIdPhong());
                            addedRooms.add(p.getSoPhong());
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách phòng theo chi nhánh: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    @Override
    public void checkAll() {
        tblDienNuoc.selectAll();
    }

    @Override
    public void uncheckAll() {
        tblDienNuoc.clearSelection();
    }

    @Override
    public void deleteCheckedItems() {
        int[] selectedRows = tblDienNuoc.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một bản ghi để xóa.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa " + selectedRows.length + " bản ghi đã chọn?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
// Xóa từ dưới lên để tránh lỗi index khi xóa
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    int modelRow = tblDienNuoc.convertRowIndexToModel(selectedRows[i]);
                    int dienNuocIdToDelete = currentList.get(modelRow).getIdDienNuoc();
                    dienNuocDao.deleteById(dienNuocIdToDelete);
                }
                JOptionPane.showMessageDialog(this, "Đã xóa các bản ghi đã chọn thành công!");
                filterAndFillTable(); // Cập nhật lại bảng sau khi xóa
                clear();
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa các bản ghi đã chọn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi không xác định khi xóa các bản ghi đã chọn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void moveFirst() {
        if (!currentList.isEmpty()) {
            currentIndex = 0;
            tblDienNuoc.setRowSelectionInterval(currentIndex, currentIndex);
            setForm(currentList.get(currentIndex));
        }
    }

    @Override
    public void movePrevious() {
        if (!currentList.isEmpty() && currentIndex > 0) {
            currentIndex--;
            tblDienNuoc.setRowSelectionInterval(currentIndex, currentIndex);
            setForm(currentList.get(currentIndex));
        }
    }

    @Override
    public void moveNext() {
        if (!currentList.isEmpty() && currentIndex < currentList.size() - 1) {
            currentIndex++;
            tblDienNuoc.setRowSelectionInterval(currentIndex, currentIndex);
            setForm(currentList.get(currentIndex));
        }
    }

    @Override
    public void moveLast() {
        if (!currentList.isEmpty()) {
            currentIndex = currentList.size() - 1;
            tblDienNuoc.setRowSelectionInterval(currentIndex, currentIndex);
            setForm(currentList.get(currentIndex));
        }
    }

    @Override
    public void moveTo(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < currentList.size()) {
            currentIndex = rowIndex;
            tblDienNuoc.setRowSelectionInterval(currentIndex, currentIndex);
            setForm(currentList.get(currentIndex));
        }
    }

    private void Excel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu file Excel");
        fileChooser.setSelectedFile(new File("DuLieuDienNuoc.xlsx"));
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getAbsolutePath().toLowerCase().endsWith(".xlsx")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".xlsx");
            }
            try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream(fileToSave)) {
                Sheet sheet = workbook.createSheet("Dữ liệu Điện Nước");
                DefaultTableModel model = (DefaultTableModel) tblDienNuoc.getModel();
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < model.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(model.getColumnName(i));
                }
                for (int r = 0; r < model.getRowCount(); r++) {
                    Row dataRow = sheet.createRow(r + 1);
                    for (int c = 0; c < model.getColumnCount(); c++) {
                        Cell cell = dataRow.createCell(c);
                        Object value = model.getValueAt(r, c);
                        if (value != null) {
                            if (value instanceof String) {
                                cell.setCellValue((String) value);
                            } else if (value instanceof Number) {
                                cell.setCellValue(((Number) value).doubleValue());
                            } else {
                                cell.setCellValue(value.toString());
                            }
                        } else {
                            cell.setCellValue("");
                        }
                    }
                }
                workbook.write(fileOut);
                JOptionPane.showMessageDialog(this, "Xuất file Excel thành công!\nĐường dẫn: " + fileToSave.getAbsolutePath(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất file Excel: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
