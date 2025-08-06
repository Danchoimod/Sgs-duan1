/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package poly.nhatro.ui;

import poly.nhatro.entity.ChiNhanh;
import poly.nhatro.entity.Phong;
import poly.nhatro.entity.HopDong;
import poly.nhatro.entity.HoaDon;
import poly.nhatro.service.ChiNhanhService;
import poly.nhatro.service.ChiNhanhServiceImpl;
import poly.nhatro.dao.PhongDao;
import poly.nhatro.dao.impl.PhongDaoImpl;
import poly.nhatro.dao.HopDongDAO;
import poly.nhatro.dao.impl.HopDongImpl;
import poly.nhatro.dao.NguoiThueDAO;
import poly.nhatro.dao.impl.NguoiThueDaoImpl;
import poly.nhatro.dao.HoaDonDAO;
import poly.nhatro.dao.impl.hoaDonDAOImpl;
import poly.nhatro.util.XJdbc;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Phu Pham
 */
public class ChiNhanhPanel extends javax.swing.JPanel {

    private ChiNhanhService chiNhanhService;
    private PhongDao phongDao;
    private HopDongDAO hopDongDAO;
    private NguoiThueDAO nguoiThueDAO;
    private HoaDonDAO hoaDonDAO;
    private DefaultTableModel tableModel;

    /**
     * Creates new form ChiNhanhPanel
     */
    public ChiNhanhPanel() {
        initComponents(); // Phải gọi trước để khởi tạo các thành phần Swing
        chiNhanhService = new ChiNhanhServiceImpl();
        phongDao = new PhongDaoImpl();
        hopDongDAO = new HopDongImpl();
        nguoiThueDAO = new NguoiThueDaoImpl();
        hoaDonDAO = new hoaDonDAOImpl();
        initTable();
        loadDataToTable();
        
        // Thêm sự kiện sau khi các thành phần đã được khởi tạo
    }

    private void initTable() {
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] columns = {"ID", "Tên chi nhánh", "Địa chỉ", "Giá điện", "Giá nước"};
        tableModel.setColumnIdentifiers(columns);
        tblChiNhanh.setModel(tableModel);
    }

    private void loadDataToTable() {
        tableModel.setRowCount(0);
        List<ChiNhanh> list = chiNhanhService.getAll();
        for (ChiNhanh cn : list) {
            tableModel.addRow(new Object[]{
                cn.getID_ChiNhanh(),
                cn.getTenChiNhanh(),
                cn.getDiaChi(),
                cn.getGiaDien(),
                cn.getGiaNuoc()
            });
        }
    }

    private void search() {
        String keyword = txtTimKiem.getText().trim();
        List<ChiNhanh> list = chiNhanhService.search(keyword);
        tableModel.setRowCount(0);
        for (ChiNhanh cn : list) {
            tableModel.addRow(new Object[]{
                cn.getID_ChiNhanh(),
                cn.getTenChiNhanh(),
                cn.getDiaChi(),
                cn.getGiaDien(),
                cn.getGiaNuoc()
            });
        }
    }

    private void add() {
        try {
            ChiNhanh cn = new ChiNhanh();
            cn.setTenChiNhanh(btnTenChiNhanh.getText().trim());
            cn.setDiaChi(txtDiaChi.getText().trim()); // Bạn cần thêm field địa chỉ vào form
            cn.setGiaDien(new java.math.BigDecimal(txtGiaDien.getText().trim()));
            cn.setGiaNuoc(new java.math.BigDecimal(txtGiaNuoc.getText().trim()));
            if (chiNhanhService.add(cn)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công");
                loadDataToTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    private void update() {
        int row = tblChiNhanh.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chi nhánh cần sửa");
            return;
        }

        try {
            int id = (int) tblChiNhanh.getValueAt(row, 0);
            ChiNhanh cn = new ChiNhanh();
            cn.setID_ChiNhanh(id);
            cn.setTenChiNhanh(btnTenChiNhanh.getText().trim());
            cn.setDiaChi(txtDiaChi.getText().trim()); // Bạn cần thêm field địa chỉ vào form
            cn.setGiaDien(new java.math.BigDecimal(txtGiaDien.getText().trim()));
            cn.setGiaNuoc(new java.math.BigDecimal(txtGiaNuoc.getText().trim()));

            if (chiNhanhService.update(cn)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                loadDataToTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    private void delete() {
        int row = tblChiNhanh.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chi nhánh cần xóa");
            return;
        }

        int id = (int) tblChiNhanh.getValueAt(row, 0);
        String tenChiNhanh = (String) tblChiNhanh.getValueAt(row, 1);
        
        try {
            // Lấy danh sách phòng trong chi nhánh
            List<Phong> phongList = ((PhongDaoImpl) phongDao).findByChiNhanh(id);
            
            // Đếm tổng số hợp đồng
            int totalHopDong = 0;
            for (Phong phong : phongList) {
                List<HopDong> hopDongList = hopDongDAO.selectByRoom(phong.getIdPhong());
                totalHopDong += hopDongList.size();
            }
            
            // Hiển thị thông báo xác nhận với thông tin chi tiết
            String message = "CẢNH BÁO: Xóa chi nhánh sẽ xóa TẤT CẢ dữ liệu liên quan!\n\n" +
                           "Chi nhánh \"" + tenChiNhanh + "\" có:\n" +
                           "• " + phongList.size() + " phòng\n" +
                           "• " + totalHopDong + " hợp đồng\n" +
                           "• Tất cả hóa đơn, điện nước, góp ý liên quan\n\n" +
                           "Dữ liệu sẽ bị XÓA VĨNH VIỄN và KHÔNG THỂ KHÔI PHỤC!\n" +
                           "Bạn có chắc chắn muốn tiếp tục?";
            
            int confirm = JOptionPane.showConfirmDialog(this, message, 
                "Xác nhận xóa chi nhánh", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);
                
            if (confirm == JOptionPane.YES_OPTION) {
                int deletedHopDong = 0;
                int deletedPhong = 0;
                
                // Bước 1: Xóa tất cả hợp đồng và dữ liệu liên quan
                for (Phong phong : phongList) {
                    List<HopDong> hopDongList = hopDongDAO.selectByRoom(phong.getIdPhong());
                    for (HopDong hopDong : hopDongList) {
                        // Bước 1a: Xóa tất cả dữ liệu liên quan đến hợp đồng trước
                        deleteAllDataByHopDongId(hopDong.getID_HopDong());
                        
                        // Bước 1b: Xóa quan hệ NguoiThue_HopDong trước
                        deleteNguoiThueHopDongByHopDongId(hopDong.getID_HopDong());
                        
                        // Bước 1c: Xóa hợp đồng
                        hopDongDAO.delete(hopDong.getID_HopDong());
                        deletedHopDong++;
                    }
                    
                    // Bước 2: Xóa phòng
                    phongDao.deleteById(phong.getIdPhong());
                    deletedPhong++;
                }
                
                // Bước 3: Xóa tất cả góp ý thuộc chi nhánh này
                deleteGopYByChiNhanhId(id);
                
                // Bước 4: Xóa chi nhánh
                if (chiNhanhService.delete(id)) {
                    String successMessage = "XÓA THÀNH CÔNG!\n\n" +
                                          "Đã xóa:\n" +
                                          "• 1 chi nhánh\n" +
                                          "• " + deletedPhong + " phòng\n" +
                                          "• " + deletedHopDong + " hợp đồng\n" +
                                          "• Tất cả dữ liệu liên quan";
                    
                    JOptionPane.showMessageDialog(this, successMessage, "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    loadDataToTable();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi: Không thể xóa chi nhánh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi xóa chi nhánh: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Xóa tất cả dữ liệu liên quan đến hợp đồng theo ID_HopDong
     */
    private void deleteAllDataByHopDongId(int hopDongId) {
        try {
            // Xóa theo thứ tự ràng buộc khóa ngoại
            
            // 1. Xóa hóa đơn
            String sqlHoaDon = "DELETE FROM HoaDon WHERE ID_HopDong = ?";
            XJdbc.executeUpdate(sqlHoaDon, hopDongId);
            
            // 2. Xóa điện nước
            String sqlDienNuoc = "DELETE FROM DienNuoc WHERE ID_HopDong = ?";
            XJdbc.executeUpdate(sqlDienNuoc, hopDongId);
            
            // 3. Xóa góp ý
            String sqlGopY = "DELETE FROM GopY WHERE ID_HopDong = ?";
            XJdbc.executeUpdate(sqlGopY, hopDongId);
            
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa dữ liệu liên quan cho hợp đồng ID " + hopDongId + ": " + e.getMessage());
            // Không throw exception để tiếp tục quá trình xóa
        }
    }

    /**
     * Xóa tất cả quan hệ NguoiThue_HopDong theo ID_HopDong
     */
    private void deleteNguoiThueHopDongByHopDongId(int hopDongId) {
        try {
            String sql = "DELETE FROM NguoiThue_HopDong WHERE ID_HopDong = ?";
            XJdbc.executeUpdate(sql, hopDongId);
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa NguoiThue_HopDong cho hợp đồng ID " + hopDongId + ": " + e.getMessage());
            // Không throw exception để tiếp tục quá trình xóa
        }
    }

    /**
     * Xóa tất cả góp ý thuộc chi nhánh theo ID_ChiNhanh
     */
    private void deleteGopYByChiNhanhId(int chiNhanhId) {
        try {
            String sql = "DELETE FROM GopY WHERE ID_ChiNhanh = ?";
            XJdbc.executeUpdate(sql, chiNhanhId);
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa GopY cho chi nhánh ID " + chiNhanhId + ": " + e.getMessage());
            // Không throw exception để tiếp tục quá trình xóa
        }
    }

    private void clearForm() {
        btnTenChiNhanh.setText("");
        txtGiaDien.setText("");
        txtGiaNuoc.setText("");
        txtDiaChi.setText("");
    }

    private void showSelectedRow() {
        int row = tblChiNhanh.getSelectedRow();
        if (row >= 0) {
            btnTenChiNhanh.setText(tblChiNhanh.getValueAt(row, 1).toString());
            txtGiaDien.setText(tblChiNhanh.getValueAt(row, 3).toString());
            txtGiaNuoc.setText(tblChiNhanh.getValueAt(row, 4).toString());
            txtDiaChi.setText(tblChiNhanh.getValueAt(row, 2).toString());
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnTenChiNhanh = new javax.swing.JTextField();
        txtGiaDien = new javax.swing.JTextField();
        txtGiaNuoc = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel(new com.formdev.flatlaf.extras.FlatSVGIcon("icons/search.svg", 24, 24));
        btnTao = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        txtDiaChi = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblChiNhanh = new javax.swing.JTable();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Quản lý chi nhánh");

        jLabel2.setText("Tên chi nhánh");

        jLabel3.setText("Giá điện");

        jLabel4.setText("Giá nước");

        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });

        jLabel5.setText("Tìm kiếm");

        jLabel6.setText(" ");
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        btnTao.setText("Tạo");
        btnTao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoActionPerformed(evt);
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

        jLabel7.setText("Địa chỉ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnTenChiNhanh, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtGiaDien, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtGiaNuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnTao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSua)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXoa)))
                .addContainerGap(145, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel7))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnTenChiNhanh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGiaDien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGiaNuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnTao)
                            .addComponent(btnSua)
                            .addComponent(btnXoa))))
                .addGap(18, 18, 18))
        );

        tblChiNhanh.setModel(new javax.swing.table.DefaultTableModel(
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
        tblChiNhanh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblChiNhanhMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblChiNhanh);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemActionPerformed

    private void tblChiNhanhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblChiNhanhMouseClicked
        // TODO add your handling code here:
        showSelectedRow();
    }//GEN-LAST:event_tblChiNhanhMouseClicked

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        // TODO add your handling code here:
        search();
    }//GEN-LAST:event_jLabel6MouseClicked

    private void btnTaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoActionPerformed
        // TODO add your handling code here:
        add();
    }//GEN-LAST:event_btnTaoActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        update();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        delete();
    }//GEN-LAST:event_btnXoaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnTao;
    private javax.swing.JTextField btnTenChiNhanh;
    private javax.swing.JButton btnXoa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblChiNhanh;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtGiaDien;
    private javax.swing.JTextField txtGiaNuoc;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
