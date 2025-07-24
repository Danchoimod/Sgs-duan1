package poly.nhatro.ui;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import poly.nhatro.dao.ChiNhanhDAO;
import poly.nhatro.dao.HoaDonDAO;
import poly.nhatro.dao.NguoiThueDAO;
import poly.nhatro.dao.impl.ChiNhanhDAOImpl;
import poly.nhatro.dao.impl.NguoiThueDaoImpl;
import poly.nhatro.dao.impl.hoaDonDAOImpl;
import poly.nhatro.entity.HoaDon;
import poly.nhatro.util.XDialog;

/**
 * Panel quản lý hóa đơn
 * 
 * @author tranthuyngan
 */
public class HoaDonPanel extends javax.swing.JPanel implements HoaDonController {
    
    private final HoaDonDAO dao = new hoaDonDAOImpl();
    private final NguoiThueDAO nguoiThueDAO = new NguoiThueDaoImpl();
    private final ChiNhanhDAO chiNhanhDAO = new ChiNhanhDAOImpl();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private HoaDon currentHoaDon = null;
    
    public HoaDonPanel() {
        initComponents();
        setupForm();
    }
    
    /**
     * Thiết lập form ban đầu
     */
    private void setupForm() {
        addCalculationListeners();
        setupRadioButtons();
        populateBranchComboBox();
        txtHoten.setEditable(true);  
        txtTongTienDien.setEditable(false);
        txtTongTienNuoc.setEditable(false);
        txtTienPhong.setEditable(false);
        txtTongTien.setEditable(false);
    }
    
    /**
     * Thêm listeners cho các field tính toán
     */
    private void addCalculationListeners() {      
        txtMaHopDong.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calculateTienDien();
                calculateTienNuoc();
                calculateTienPhong();
                calculateTongTien();
            }
        });
        
        txtTongTienDien.setEditable(false);
        txtTongTienNuoc.setEditable(false);
        txtTongTien.setEditable(false);
    }
    
    /**
     * Thiết lập radio buttons cho trạng thái thanh toán
     */
    private void setupRadioButtons() {
        groupThanhToan.add(rdoDaThanhToan);
        groupThanhToan.add(rdoChuaThanhToan);
        rdoChuaThanhToan.setSelected(true); // Default selection
    }
    
    /**
     * Populate branch combo box với dữ liệu từ database
     */
    private void populateBranchComboBox() {
        try {
            cboTenChiNhanh.removeAllItems();
            cboTenChiNhanh.addItem("");
            
            List<String> branches = chiNhanhDAO.getAllBranchNames();
            for (String branch : branches) {
                cboTenChiNhanh.addItem(branch);
            }
            
            if (cboTenChiNhanh.getItemCount() > 1) {
                cboTenChiNhanh.setSelectedIndex(1);
            }
        } catch (Exception e) {
            System.err.println("Error populating branch combo box: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void calculateTienDien() {
        try {
            String soDienCuStr = txtSoDienCu.getText().trim();
            String soDienMoiStr = txtSoDienMoi.getText().trim();
            String maHopDongStr = txtMaHopDong.getText().trim();
            
            if (!soDienCuStr.isEmpty() && !soDienMoiStr.isEmpty() && !maHopDongStr.isEmpty()) {
                int soDienCu = Integer.parseInt(soDienCuStr);
                int soDienMoi = Integer.parseInt(soDienMoiStr);
                int hopDongId = Integer.parseInt(maHopDongStr);
                
                if (soDienMoi >= soDienCu) {
                    int chenhLechDien = soDienMoi - soDienCu;
                    
                    Integer chiNhanhId = dao.getChiNhanhIdByHopDongId(hopDongId);
                    if (chiNhanhId != null) {
                        BigDecimal giaDien = chiNhanhDAO.getGiaDienById(chiNhanhId);
                        BigDecimal tongTienDien = giaDien.multiply(BigDecimal.valueOf(chenhLechDien));
                        
                        txtTongTienDien.setText(String.format("%.0f", tongTienDien));
                    } else {
                        txtTongTienDien.setText("0");
                    }
                } else {
                    txtTongTienDien.setText("0");
                }
            } else {
                txtTongTienDien.setText("0");
            }
        } catch (NumberFormatException e) {
            txtTongTienDien.setText("0");
        } catch (Exception e) {
            txtTongTienDien.setText("0");
        }
    }
    
    private void calculateTienNuoc() {
        try {
            String soNuocCuStr = txtSoNuocCu.getText().trim();
            String soNuocMoiStr = txtSoNuocMoi.getText().trim();
            String maHopDongStr = txtMaHopDong.getText().trim();
            
            if (!soNuocCuStr.isEmpty() && !soNuocMoiStr.isEmpty() && !maHopDongStr.isEmpty()) {
                int soNuocCu = Integer.parseInt(soNuocCuStr);
                int soNuocMoi = Integer.parseInt(soNuocMoiStr);
                int hopDongId = Integer.parseInt(maHopDongStr);
                
                if (soNuocMoi >= soNuocCu) {
                    int chenhLechNuoc = soNuocMoi - soNuocCu;
                    
                    Integer chiNhanhId = dao.getChiNhanhIdByHopDongId(hopDongId);
                    if (chiNhanhId != null) {
                        BigDecimal giaNuoc = chiNhanhDAO.getGiaNuocById(chiNhanhId);
                        BigDecimal tongTienNuoc = giaNuoc.multiply(BigDecimal.valueOf(chenhLechNuoc));
                        
                        txtTongTienNuoc.setText(String.format("%.0f", tongTienNuoc));
                    } else {
                        txtTongTienNuoc.setText("0");
                    }
                } else {
                    txtTongTienNuoc.setText("0");
                }
            } else {
                txtTongTienNuoc.setText("0");
            }
        } catch (NumberFormatException e) {
            txtTongTienNuoc.setText("0");
        } catch (Exception e) {
            txtTongTienNuoc.setText("0");
        }
    }
    
    private void calculateTienPhong() {
        try {
            String maHopDongStr = txtMaHopDong.getText().trim();
            
            if (!maHopDongStr.isEmpty()) {
                int hopDongId = Integer.parseInt(maHopDongStr);
                
                double giaPhong = dao.getGiaPhongByHopDongId(hopDongId);
                txtTienPhong.setText(String.format("%.0f", giaPhong));
            } else {
                txtTienPhong.setText("0");
            }
        } catch (NumberFormatException e) {
            txtTienPhong.setText("0");
        } catch (Exception e) {
            txtTienPhong.setText("0");
        }
    }
    
    private void calculateTongTien() {
        try {
            String tienDienStr = txtTongTienDien.getText().trim();
            String tienNuocStr = txtTongTienNuoc.getText().trim();
            String tienPhongStr = txtTienPhong.getText().trim();
            
            double tongTien = 0;
            
            if (!tienDienStr.isEmpty()) {
                tongTien += Double.parseDouble(tienDienStr);
            }
            
            if (!tienNuocStr.isEmpty()) {
                tongTien += Double.parseDouble(tienNuocStr);
            }
            
            if (!tienPhongStr.isEmpty()) {
                tongTien += Double.parseDouble(tienPhongStr);
            }
            
            txtTongTien.setText(String.format("%.0f", tongTien));
        } catch (NumberFormatException e) {
            txtTongTien.setText("0");
        } catch (Exception e) {
            txtTongTien.setText("0");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        groupThanhToan = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        pnlMain = new poly.nhatro.util.RoundedPanel(30);
        ;
        jLabel2 = new javax.swing.JLabel();
        txtHoten = new poly.nhatro.util.RoundedTextField(20);
        ;
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cboTenChiNhanh = new poly.nhatro.util.RoundedComboBox<>(new String[] {"Option 1", "Option 2"});
        ;
        jLabel5 = new javax.swing.JLabel();
        txtNgayThanhToan = new poly.nhatro.util.RoundedTextField(20); ;
        jLabel6 = new javax.swing.JLabel();
        txtSoDienCu = new poly.nhatro.util.RoundedTextField(20);
        ;
        jLabel7 = new javax.swing.JLabel();
        txtSoNuocCu = new poly.nhatro.util.RoundedTextField(20);
        ;
        jLabel8 = new javax.swing.JLabel();
        txtSoDienMoi = new poly.nhatro.util.RoundedTextField(20);
        ;
        jLabel9 = new javax.swing.JLabel();
        txtSoNuocMoi = new poly.nhatro.util.RoundedTextField(20);
        ;
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        rdoDaThanhToan = new javax.swing.JRadioButton();
        rdoChuaThanhToan = new javax.swing.JRadioButton();
        txtTongTien = new poly.nhatro.util.RoundedTextField(20); ;
        jLabel13 = new javax.swing.JLabel();
        txtMaHopDong = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtTongTienDien = new poly.nhatro.util.RoundedTextField(20); ;
        jLabel15 = new javax.swing.JLabel();
        txtTongTienNuoc = new poly.nhatro.util.RoundedTextField(20); ;
        jLabel16 = new javax.swing.JLabel();
        txtTienPhong = new poly.nhatro.util.RoundedTextField(20);
        txtTenPhong = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        btnXuatHoaDon = new javax.swing.JButton(new com.formdev.flatlaf.extras.FlatSVGIcon("icons/export.svg", 24, 24));
        btnXoaHoaDon = new javax.swing.JButton(new com.formdev.flatlaf.extras.FlatSVGIcon("icons/delete.svg", 24, 24));
        btnSuaHoaDon = new javax.swing.JButton(new com.formdev.flatlaf.extras.FlatSVGIcon("icons/edit.svg", 24, 24));
        btnTaoHoaDon = new javax.swing.JButton(new com.formdev.flatlaf.extras.FlatSVGIcon("icons/newfile.svg", 24, 24));
        btnLamMoi = new javax.swing.JButton(new com.formdev.flatlaf.extras.FlatSVGIcon("icons/clear.svg", 24, 24));

        jLabel11.setText("jLabel11");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 3, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 204));
        jLabel1.setText("HÓA ĐƠN");

        pnlMain.setBackground(new java.awt.Color(153, 218, 250));

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        jLabel2.setText("Họ và tên khách hàng");

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        jLabel3.setText("Tên phòng");

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        jLabel4.setText("Chi nhánh");

        cboTenChiNhanh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", " ", " " }));

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        jLabel5.setText("Thời điểm thanh toán");

        txtNgayThanhToan.setFont(new java.awt.Font("Helvetica Neue", 3, 18)); // NOI18N
        txtNgayThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNgayThanhToanActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        jLabel6.setText("Số điện cũ");

        txtSoDienCu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSoDienCuActionPerformed(evt);
            }
        });
        txtSoDienCu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSoDienCuKeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        jLabel7.setText("Số nước cũ");

        txtSoNuocCu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSoNuocCuKeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        jLabel8.setText("Số điện mới");

        txtSoDienMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSoDienMoiActionPerformed(evt);
            }
        });
        txtSoDienMoi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSoDienMoiKeyReleased(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        jLabel9.setText("Số nước mới");

        txtSoNuocMoi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSoNuocMoiKeyReleased(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Helvetica Neue", 3, 24)); // NOI18N
        jLabel10.setText("Tổng tiền: ");

        jLabel12.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        jLabel12.setText("Trạng Thái Thanh Toán");

        groupThanhToan.add(rdoDaThanhToan);
        rdoDaThanhToan.setFont(new java.awt.Font("Helvetica Neue", 2, 13)); // NOI18N
        rdoDaThanhToan.setText("Đã thanh toán");

        groupThanhToan.add(rdoChuaThanhToan);
        rdoChuaThanhToan.setFont(new java.awt.Font("Helvetica Neue", 2, 13)); // NOI18N
        rdoChuaThanhToan.setSelected(true);
        rdoChuaThanhToan.setText("Chưa thanh toán");

        txtTongTien.setEditable(false);
        txtTongTien.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        txtTongTien.setForeground(new java.awt.Color(255, 0, 0));

        jLabel13.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        jLabel13.setText("Mã Hợp Đồng");

        jLabel14.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        jLabel14.setText("Tổng tiền điện:");

        jLabel15.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        jLabel15.setText("Tổng tiền nước");

        jLabel16.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        jLabel16.setText("Tiền phòng");

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtHoten, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainLayout.createSequentialGroup()
                                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3)
                                            .addComponent(txtTenPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(85, 85, 85)
                                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtMaHopDong))
                                        .addGap(5, 5, 5))))
                            .addGroup(pnlMainLayout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(txtNgayThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12)
                                    .addGroup(pnlMainLayout.createSequentialGroup()
                                        .addComponent(rdoDaThanhToan)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(rdoChuaThanhToan)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSoDienMoi)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8)
                            .addComponent(cboTenChiNhanh, 0, 202, Short.MAX_VALUE)
                            .addGroup(pnlMainLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel4))
                            .addComponent(txtSoDienCu)
                            .addComponent(jLabel16)
                            .addComponent(txtTienPhong)))
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSoNuocCu, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                            .addComponent(txtSoNuocMoi)
                            .addComponent(txtTongTienDien)
                            .addComponent(txtTongTienNuoc)
                            .addComponent(jLabel14)
                            .addComponent(jLabel9)
                            .addComponent(jLabel15)))
                    .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(63, Short.MAX_VALUE))
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSoNuocMoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel14)))
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtHoten, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSoDienCu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSoNuocCu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel3)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMaHopDong)
                            .addComponent(txtSoDienMoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTenPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12)
                        .addGap(11, 11, 11)))
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rdoChuaThanhToan)
                            .addComponent(rdoDaThanhToan))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNgayThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlMainLayout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(cboTenChiNhanh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlMainLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(txtTongTienDien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTongTienNuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTienPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(24, 24, 24))
        );

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã hoá đơn", "Mã phòng", "Họ và tên khách hàng", "Mã hợp đồng", "Chi nhánh", "Số điện cũ", "Số điện mới", "Số nước cũ", "Số nước mới", "Tổng tiền điện", "Tổng tiền nước", "Tiền Phòng", "Trạng thái thanh toán", "Thời điểm thanh toán", "Tổng tiền"
            }
        ));
        tblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblHoaDon);

        btnXuatHoaDon.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        btnXuatHoaDon.setText("Xuất hóa đơn");
        btnXuatHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatHoaDonActionPerformed(evt);
            }
        });

        btnXoaHoaDon.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        btnXoaHoaDon.setText("Xóa hóa đơn");
        btnXoaHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaHoaDonActionPerformed(evt);
            }
        });

        btnSuaHoaDon.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        btnSuaHoaDon.setText("Sửa hóa đơn");
        btnSuaHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaHoaDonActionPerformed(evt);
            }
        });

        btnTaoHoaDon.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
        btnTaoHoaDon.setText("Tạo hóa đơn");
        btnTaoHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoHoaDonActionPerformed(evt);
            }
        });

        btnLamMoi.setFont(new java.awt.Font("Helvetica Neue", 3, 14)); // NOI18N
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
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(463, 463, 463)
                            .addComponent(jLabel1))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(50, 50, 50)
                            .addComponent(pnlMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnXuatHoaDon)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(30, 30, 30)
                            .addComponent(btnXoaHoaDon)
                            .addGap(29, 29, 29)
                            .addComponent(btnSuaHoaDon)
                            .addGap(14, 14, 14)
                            .addComponent(btnTaoHoaDon))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 981, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(99, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnXoaHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSuaHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTaoHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXuatHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(61, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtSoDienCuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSoDienCuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSoDienCuActionPerformed

    private void txtNgayThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNgayThanhToanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNgayThanhToanActionPerformed

    private void btnXuatHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatHoaDonActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_btnXuatHoaDonActionPerformed

    private void tblHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 1) { 
            edit();
        }
    }//GEN-LAST:event_tblHoaDonMouseClicked

    private void btnTaoHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoHoaDonActionPerformed
        // TODO add your handling code here:
        this.create();
    }//GEN-LAST:event_btnTaoHoaDonActionPerformed

    private void btnSuaHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaHoaDonActionPerformed
        // TODO add your handling code here:
        if (currentHoaDon != null) {
            // Nếu đang ở chế độ edit, thì thực hiện update
            this.update();
        } else {
            // Nếu chưa edit, thì bắt đầu edit
            this.edit();
        }
    }//GEN-LAST:event_btnSuaHoaDonActionPerformed

    private void btnXoaHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaHoaDonActionPerformed
        // TODO add your handling code here:
        this.delete();
    }//GEN-LAST:event_btnXoaHoaDonActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        // TODO add your handling code here:
        this.clear();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void txtSoDienMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSoDienMoiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSoDienMoiActionPerformed

    private void txtSoDienCuKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSoDienCuKeyReleased
        // TODO add your handling code here:
        calculateTienDien();  
        calculateTongTien();  
    }//GEN-LAST:event_txtSoDienCuKeyReleased

    private void txtSoDienMoiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSoDienMoiKeyReleased
        // TODO add your handling code here:
        calculateTienDien();  
        calculateTongTien();  
    }//GEN-LAST:event_txtSoDienMoiKeyReleased

    private void txtSoNuocCuKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSoNuocCuKeyReleased
        // TODO add your handling code here:
        calculateTienNuoc();
        calculateTongTien();
    }//GEN-LAST:event_txtSoNuocCuKeyReleased

    private void txtSoNuocMoiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSoNuocMoiKeyReleased
        // TODO add your handling code here:
        calculateTienNuoc();
        calculateTongTien();
    }//GEN-LAST:event_txtSoNuocMoiKeyReleased

    @Override
    public void setForm(HoaDon entity) {
        currentHoaDon = entity; 
        
        if (entity == null) {
            clear();
            return;
        }
        
        try {
            txtSoDienCu.setText(String.valueOf(entity.getSoDienCu()));
            txtSoDienMoi.setText(String.valueOf(entity.getSoDienMoi()));
            txtSoNuocCu.setText(String.valueOf(entity.getSoNuocCu()));
            txtSoNuocMoi.setText(String.valueOf(entity.getSoNuocMoi()));           
            txtTongTienDien.setText(entity.getTienDien() != null ? entity.getTienDien().toString() : "0");
            txtTongTienNuoc.setText(entity.getTienNuoc() != null ? entity.getTienNuoc().toString() : "0");
            txtTienPhong.setText(entity.getTienPhong() != null ? entity.getTienPhong().toString() : "0");
            txtTongTien.setText(entity.getTongTien() != null ? entity.getTongTien().toString() : "0");            
            rdoDaThanhToan.setSelected(entity.isTrangThaiThanhToan()); 
            rdoChuaThanhToan.setSelected(!entity.isTrangThaiThanhToan());            
            txtNgayThanhToan.setText(entity.getNgayThanhToan() != null ? sdf.format(entity.getNgayThanhToan()) : "");          
            txtMaHopDong.setText(String.valueOf(entity.getID_HopDong()));
          
            int hoaDonId = entity.getID_HoaDon();
            
            String hoTen = dao.getHoTenByHoaDonId(hoaDonId);
            txtHoten.setText(hoTen != null ? hoTen : "");
            
            String tenPhong = dao.getTenPhongByHoaDonId(hoaDonId);
            
            if (tenPhong != null) {
                txtTenPhong.setText(tenPhong);
            }
            
            String tenChiNhanh = dao.getTenChiNhanhByHoaDonId(hoaDonId);
            
            if (tenChiNhanh != null) {
                boolean found = false;
                for (int i = 0; i < cboTenChiNhanh.getItemCount(); i++) {
                    if (tenChiNhanh.equals(cboTenChiNhanh.getItemAt(i))) {
                        cboTenChiNhanh.setSelectedIndex(i);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    cboTenChiNhanh.addItem(tenChiNhanh);
                    cboTenChiNhanh.setSelectedItem(tenChiNhanh);
                }
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    @Override
    public HoaDon getForm() {
        try {
            HoaDon hoaDon = new HoaDon();
            
            if (currentHoaDon != null) {
                hoaDon.setID_HoaDon(currentHoaDon.getID_HoaDon());
                hoaDon.setID_HopDong(currentHoaDon.getID_HopDong());
            } else {
                String maHopDongStr = txtMaHopDong.getText().trim();
                if (!maHopDongStr.isEmpty()) {
                    hoaDon.setID_HopDong(Integer.parseInt(maHopDongStr));
                }
            }
            
            hoaDon.setSoDienCu(Integer.parseInt(txtSoDienCu.getText().trim()));
            hoaDon.setSoDienMoi(Integer.parseInt(txtSoDienMoi.getText().trim()));
            hoaDon.setSoNuocCu(Integer.parseInt(txtSoNuocCu.getText().trim()));
            hoaDon.setSoNuocMoi(Integer.parseInt(txtSoNuocMoi.getText().trim()));
            
            String tienDienStr = txtTongTienDien.getText().trim();
            if (!tienDienStr.isEmpty()) {
                hoaDon.setTienDien(new java.math.BigDecimal(tienDienStr));
            }
            
            String tienNuocStr = txtTongTienNuoc.getText().trim();
            if (!tienNuocStr.isEmpty()) {
                hoaDon.setTienNuoc(new java.math.BigDecimal(tienNuocStr));
            }
            
            String tienPhongStr = txtTienPhong.getText().trim();
            if (!tienPhongStr.isEmpty()) {
                hoaDon.setTienPhong(new java.math.BigDecimal(tienPhongStr));
            }
            
            String tongTienStr = txtTongTien.getText().trim();
            if (!tongTienStr.isEmpty()) {
                hoaDon.setTongTien(new java.math.BigDecimal(tongTienStr));
            }
            
            hoaDon.setTrangThaiThanhToan(rdoDaThanhToan.isSelected());
            
            String ngayStr = txtNgayThanhToan.getText().trim();
            if (!ngayStr.isEmpty()) {
                try {
                    Date ngayThanhToan = sdf.parse(ngayStr);
                    hoaDon.setNgayThanhToan(ngayThanhToan);
                } catch (ParseException e) {
                    hoaDon.setNgayThanhToan(null);
                    XDialog.alert("Định dạng ngày không đúng. Vui lòng nhập theo định dạng yyyy-MM-dd");
                }
            } else {
                hoaDon.setNgayThanhToan(null);
            }
                                
            return hoaDon;
            
        } catch (NumberFormatException e) {
            XDialog.alert("Vui lòng nhập đúng định dạng số!");
            return null;
        } catch (Exception e) {
            XDialog.alert("Lỗi khi lấy dữ liệu từ form: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Clear tất cả dữ liệu trên form
     */
    
    /**
     * Kiểm tra tính hợp lệ của dữ liệu trong form
     * @return true nếu dữ liệu hợp lệ, false nếu có lỗi
     */
    private boolean validateForm() {
        try {
            // Kiểm tra họ tên khách hàng
            String hoTenStr = txtHoten.getText().trim();
            if (hoTenStr.isEmpty()) {
                XDialog.alert("Vui lòng nhập họ tên khách hàng!");
                txtHoten.requestFocus();
                return false;
            }
            if (hoTenStr.length() < 2) {
                XDialog.alert("Họ tên phải có ít nhất 2 ký tự!");
                txtHoten.requestFocus();
                return false;
            }
            
            // Kiểm tra tên phòng
            String tenPhongStr = txtTenPhong.getText().trim();
            if (tenPhongStr.isEmpty()) {
                XDialog.alert("Vui lòng nhập tên phòng!");
                txtTenPhong.requestFocus();
                return false;
            }
            
            // Kiểm tra chi nhánh
            if (cboTenChiNhanh.getSelectedIndex() <= 0 || cboTenChiNhanh.getSelectedItem() == null || cboTenChiNhanh.getSelectedItem().toString().trim().isEmpty()) {
                XDialog.alert("Vui lòng chọn chi nhánh!");
                cboTenChiNhanh.requestFocus();
                return false;
            }
            
            if (currentHoaDon == null) { 
                String maHopDongStr = txtMaHopDong.getText().trim();
                if (maHopDongStr.isEmpty()) {
                    XDialog.alert("Vui lòng nhập mã hợp đồng!");
                    txtMaHopDong.requestFocus();
                    return false;
                }
                try {
                    Integer.parseInt(maHopDongStr);
                } catch (NumberFormatException e) {
                    XDialog.alert("Mã hợp đồng phải là số nguyên!");
                    txtMaHopDong.requestFocus();
                    return false;
                }
            }
            
            String soDienCuStr = txtSoDienCu.getText().trim();
            if (soDienCuStr.isEmpty()) {
                XDialog.alert("Vui lòng nhập số điện cũ!");
                txtSoDienCu.requestFocus();
                return false;
            }
            int soDienCu;
            try {
                soDienCu = Integer.parseInt(soDienCuStr);
                if (soDienCu < 0) {
                    XDialog.alert("Số điện cũ không được âm!");
                    txtSoDienCu.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                XDialog.alert("Số điện cũ phải là số nguyên!");
                txtSoDienCu.requestFocus();
                return false;
            }
            
            String soDienMoiStr = txtSoDienMoi.getText().trim();
            if (soDienMoiStr.isEmpty()) {
                XDialog.alert("Vui lòng nhập số điện mới!");
                txtSoDienMoi.requestFocus();
                return false;
            }
            int soDienMoi;
            try {
                soDienMoi = Integer.parseInt(soDienMoiStr);
                if (soDienMoi < 0) {
                    XDialog.alert("Số điện mới không được âm!");
                    txtSoDienMoi.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                XDialog.alert("Số điện mới phải là số nguyên!");
                txtSoDienMoi.requestFocus();
                return false;
            }
            
            if (soDienMoi < soDienCu) {
                XDialog.alert("Số điện mới phải lớn hơn hoặc bằng số điện cũ!");
                txtSoDienMoi.requestFocus();
                return false;
            }
            
            String soNuocCuStr = txtSoNuocCu.getText().trim();
            if (soNuocCuStr.isEmpty()) {
                XDialog.alert("Vui lòng nhập số nước cũ!");
                txtSoNuocCu.requestFocus();
                return false;
            }
            int soNuocCu;
            try {
                soNuocCu = Integer.parseInt(soNuocCuStr);
                if (soNuocCu < 0) {
                    XDialog.alert("Số nước cũ không được âm!");
                    txtSoNuocCu.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                XDialog.alert("Số nước cũ phải là số nguyên!");
                txtSoNuocCu.requestFocus();
                return false;
            }
            
            String soNuocMoiStr = txtSoNuocMoi.getText().trim();
            if (soNuocMoiStr.isEmpty()) {
                XDialog.alert("Vui lòng nhập số nước mới!");
                txtSoNuocMoi.requestFocus();
                return false;
            }
            int soNuocMoi;
            try {
                soNuocMoi = Integer.parseInt(soNuocMoiStr);
                if (soNuocMoi < 0) {
                    XDialog.alert("Số nước mới không được âm!");
                    txtSoNuocMoi.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                XDialog.alert("Số nước mới phải là số nguyên!");
                txtSoNuocMoi.requestFocus();
                return false;
            }
            
            if (soNuocMoi < soNuocCu) {
                XDialog.alert("Số nước mới phải lớn hơn hoặc bằng số nước cũ!");
                txtSoNuocMoi.requestFocus();
                return false;
            }
            
            String tienDienStr = txtTongTienDien.getText().trim();
            if (tienDienStr.isEmpty() || tienDienStr.equals("0")) {
                XDialog.alert("Tiền điện chưa được tính toán! Vui lòng kiểm tra mã hợp đồng và số điện.");
                txtSoDienCu.requestFocus();
                return false;
            }
            
            String tienNuocStr = txtTongTienNuoc.getText().trim();
            if (tienNuocStr.isEmpty() || tienNuocStr.equals("0")) {
                XDialog.alert("Tiền nước chưa được tính toán! Vui lòng kiểm tra mã hợp đồng và số nước.");
                txtSoNuocCu.requestFocus();
                return false;
            }
            
            String tienPhongStr = txtTienPhong.getText().trim();
            if (tienPhongStr.isEmpty() || tienPhongStr.equals("0")) {
                XDialog.alert("Tiền phòng chưa được tính toán! Vui lòng kiểm tra mã hợp đồng.");
                txtMaHopDong.requestFocus();
                return false;
            }
            
            String ngayStr = txtNgayThanhToan.getText().trim();
            if (!ngayStr.isEmpty()) {
                try {
                    sdf.parse(ngayStr);
                } catch (ParseException e) {
                    XDialog.alert("Định dạng ngày không đúng! Vui lòng nhập theo định dạng yyyy-MM-dd");
                    txtNgayThanhToan.requestFocus();
                    return false;
                }
            }
            
            if (rdoDaThanhToan.isSelected() && ngayStr.isEmpty()) {
                XDialog.alert("Nếu đã thanh toán, vui lòng nhập ngày thanh toán!");
                txtNgayThanhToan.requestFocus();
                return false;
            }
            
            return true; 
            
        } catch (Exception e) {
            XDialog.alert("Lỗi khi kiểm tra dữ liệu: " + e.getMessage());
            return false;
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnSuaHoaDon;
    private javax.swing.JButton btnTaoHoaDon;
    private javax.swing.JButton btnXoaHoaDon;
    private javax.swing.JButton btnXuatHoaDon;
    private javax.swing.JComboBox<String> cboTenChiNhanh;
    private javax.swing.ButtonGroup groupThanhToan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JRadioButton rdoChuaThanhToan;
    private javax.swing.JRadioButton rdoDaThanhToan;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTextField txtHoten;
    private javax.swing.JTextField txtMaHopDong;
    private javax.swing.JTextField txtNgayThanhToan;
    private javax.swing.JTextField txtSoDienCu;
    private javax.swing.JTextField txtSoDienMoi;
    private javax.swing.JTextField txtSoNuocCu;
    private javax.swing.JTextField txtSoNuocMoi;
    private javax.swing.JTextField txtTenPhong;
    private javax.swing.JTextField txtTienPhong;
    private javax.swing.JTextField txtTongTien;
    private javax.swing.JTextField txtTongTienDien;
    private javax.swing.JTextField txtTongTienNuoc;
    // End of variables declaration//GEN-END:variables

    @Override
    public void init() {
        fillToTable();
        clear();
        
    }

    @Override
    public void open() {
        txtHoten.setEditable(true);  
        fillToTable();
    }

    public void setForm(int hoaDonId) {
        try {
            String hoTen = dao.getHoTenByHoaDonId(hoaDonId);
            String tenPhong = dao.getTenPhongByHoaDonId(hoaDonId);
            String tenChiNhanh = dao.getTenChiNhanhByHoaDonId(hoaDonId);
            
            txtHoten.setText(hoTen != null ? hoTen : "");
            
            if (tenPhong != null) {
                txtTenPhong.setText(tenPhong);
            }
            
            if (tenChiNhanh != null) {
                for (int i = 0; i < cboTenChiNhanh.getItemCount(); i++) {
                    if (tenChiNhanh.equals(cboTenChiNhanh.getItemAt(i))) {
                        cboTenChiNhanh.setSelectedIndex(i);
                        break;
                    }
                }
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create() {     
        try {
            if (!validateForm()) {
                return;
            }
            
            HoaDon entity = this.getForm();
            if (entity != null) {
                Integer chiNhanhId = dao.getChiNhanhIdByHopDongId(entity.getID_HopDong());
                if (chiNhanhId == null) {
                    XDialog.alert("Mã hợp đồng " + entity.getID_HopDong() + " không tồn tại trong hệ thống!");
                    txtMaHopDong.requestFocus();
                    return;
                }
                
                dao.create(entity);
                this.fillToTable();
                this.clear();
                XDialog.alert("Tạo mới hoá đơn thành công!");
            }
        } catch (NumberFormatException e) {
            XDialog.alert("ID Hợp Đồng phải là số nguyên!");
        } catch(Exception e) {
            XDialog.alert("Lỗi khi tạo mới hoá đơn: " + e.getMessage());   
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        if (!validateForm()) {
            return;
        }
        
        HoaDon entity = getForm();
        if(entity != null) {
            try {
                dao.update(entity);
                fillToTable();
                setEditable(false); 
                XDialog.alert("Cập nhật hoá đơn thành công!");
            }catch(Exception e) {
                XDialog.alert("Cập nhật thất bại! " + e.getMessage());
                e.printStackTrace();
            }         
        }              
    }

    @Override
    public void delete() {
        int selectRow = tblHoaDon.getSelectedRow();
        
        if(selectRow >= 0) {
            try {
                Object value = tblHoaDon.getValueAt(selectRow, 0);
                int hoaDonId = (Integer) value;
                
                if(XDialog.confirm("Bạn có thật sự muốn xoá hóa đơn ID: " + hoaDonId + "?")) {
                    dao.deleteById(hoaDonId);
                    fillToTable();
                    clear();
                    XDialog.alert("Xoá hoá đơn thành công!");
                }
            } catch(Exception e) {
                XDialog.alert("Xoá hoá đơn thất bại: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            XDialog.alert("Vui lòng chọn một hóa đơn để xóa!");
        }    
    }

    @Override
    public void clear() {
        currentHoaDon = null; 
        txtHoten.setText("");
        txtMaHopDong.setText("");
        txtSoDienCu.setText("");
        txtSoDienMoi.setText("");
        txtSoNuocCu.setText("");
        txtSoNuocMoi.setText("");
        txtTongTienDien.setText("");
        txtTongTienNuoc.setText("");
        txtTienPhong.setText("");
        txtTongTien.setText("");
        txtNgayThanhToan.setText("");      
        txtTenPhong.setText("");
        if (cboTenChiNhanh.getItemCount() > 0) {
            cboTenChiNhanh.setSelectedIndex(0);
        }     
        rdoDaThanhToan.setSelected(false);
        rdoChuaThanhToan.setSelected(true);
       
    }

    @Override
    public void edit() {
        int selectRow = tblHoaDon.getSelectedRow();
        
        if(selectRow >= 0) {
            try {
                Object value = tblHoaDon.getValueAt(selectRow, 0);
                
                int hoaDonId = (Integer) value; 
               
                HoaDon hoadon = dao.findById(hoaDonId);
                
                if(hoadon != null) {
                    setForm(hoadon);
                    setEditable(true); 
                } else {
                    XDialog.alert("Không tìm thấy hoá đơn với ID: " + hoaDonId);              
                }
                    
            } catch(Exception e) {
                e.printStackTrace();
                XDialog.alert("Lỗi khi tải dữ liệu: " + e.getMessage());
            }          
        } else {
            XDialog.alert("Vui lòng chọn một hóa đơn để chỉnh sửa!");
        }
        
    }

    @Override
    public void setEditable(boolean editable) {        
        txtHoten.setEditable(editable);  
        txtTenPhong.setEditable(editable);
        txtMaHopDong.setEditable(editable);
        cboTenChiNhanh.setEnabled(editable);
        txtNgayThanhToan.setEditable(editable);
        txtSoDienCu.setEditable(editable);
        txtSoDienMoi.setEditable(editable);
        txtSoNuocCu.setEditable(editable);
        txtSoNuocMoi.setEditable(editable);
        
        txtTongTienDien.setEditable(false);
        txtTongTienNuoc.setEditable(false);
        txtTienPhong.setEditable(false);
        txtTongTien.setEditable(false);       
        rdoDaThanhToan.setEnabled(editable);
        rdoChuaThanhToan.setEnabled(editable);            
    }

    @Override
    public void fillToTable() {
        try {
            List<HoaDon> hoaDonList = dao.findAll();
            
            DefaultTableModel model = new DefaultTableModel();
            
            String[] columnNames = {
                "Mã hoá đơn", "Mã phòng", "Họ và tên khách hàng", "Mã hợp đồng", "Chi nhánh", 
                "Số điện cũ", "Số điện mới", "Số nước cũ", "Số nước mới", 
                "Trạng thái thanh toán", "Thời điểm thanh toán", "Tổng tiền"
            };
            model.setColumnIdentifiers(columnNames);
            
            for (HoaDon hoaDon : hoaDonList) {
                String hoTen = dao.getHoTenByHoaDonId(hoaDon.getID_HoaDon());
                String tenPhong = dao.getTenPhongByHoaDonId(hoaDon.getID_HoaDon());
                String tenChiNhanh = dao.getTenChiNhanhByHoaDonId(hoaDon.getID_HoaDon());
                
                String ngayThanhToanDisplay;
                if (hoaDon.isTrangThaiThanhToan()) {
                    if (hoaDon.getNgayThanhToan() != null) {
                        ngayThanhToanDisplay = sdf.format(hoaDon.getNgayThanhToan());
                    } else {
                        ngayThanhToanDisplay = "Đã thanh toán";
                    }
                } else {
                    ngayThanhToanDisplay = "Chưa thanh toán";
                }
                
                Object[] tableRow = {
                    hoaDon.getID_HoaDon(),                                 
                    tenPhong != null ? tenPhong : "N/A",                  
                    hoTen != null ? hoTen : "N/A",                         
                    hoaDon.getID_HopDong(),                                 
                    tenChiNhanh != null ? tenChiNhanh : "N/A",            
                    hoaDon.getSoDienCu(),                                   
                    hoaDon.getSoDienMoi(),                                 
                    hoaDon.getSoNuocCu(),                                  
                    hoaDon.getSoNuocMoi(),                                 
                    hoaDon.isTrangThaiThanhToan() ? "Đã thanh toán" : "Chưa thanh toán", 
                    ngayThanhToanDisplay,                                   
                    hoaDon.getTongTien() != null ? hoaDon.getTongTien() : 0 
                };
                model.addRow(tableRow);
            }
            
            tblHoaDon.setModel(model);
            tblHoaDon.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
            
        } catch (Exception e) {
            XDialog.alert("Lỗi khi tải dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void checkAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void uncheckAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteCheckedItems() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void moveFirst() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void movePrevious() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void moveNext() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void moveLast() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void moveTo(int rowIndex) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
