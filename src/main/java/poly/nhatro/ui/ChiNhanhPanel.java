package poly.nhatro.ui;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import poly.nhatro.entity.ChiNhanh;
import poly.nhatro.service.ChiNhanhService;

public class ChiNhanhPanel extends javax.swing.JPanel {

    private ChiNhanhService chiNhanhService;
    private DefaultTableModel model;

    public ChiNhanhPanel() {
        initComponents();
        initData();
        loadDataToTable();
    }

    private void initData() {
        chiNhanhService = new ChiNhanhService();
        model = (DefaultTableModel) tblChiNhanh.getModel();
        model.setColumnIdentifiers(new String[]{"ID", "Tên chi nhánh", "Địa chỉ", "Giá điện", "Giá nước"});
    }

    private void loadDataToTable() {
        try {
            model.setRowCount(0);
            List<ChiNhanh> list = chiNhanhService.findAll();
            for (ChiNhanh cn : list) {
                model.addRow(new Object[]{
                    cn.getId_ChiNhanh(),
                    cn.getTenChiNhanh(),
                    cn.getDiaChi(),
                    cn.getGiaDien(),
                    cn.getGiaNuoc()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    private void clearForm() {
        txtTenChiNhanh.setText("");
        txtDiaChi.setText("");
        txtGiaDien.setText("");
        txtGiaNuoc.setText("");
    }

    private void fillFormFromSelectedRow() {
        int selectedRow = tblChiNhanh.getSelectedRow();
        if (selectedRow >= 0) {
            txtTenChiNhanh.setText(tblChiNhanh.getValueAt(selectedRow, 1).toString());
            txtDiaChi.setText(tblChiNhanh.getValueAt(selectedRow, 2).toString());
            txtGiaDien.setText(tblChiNhanh.getValueAt(selectedRow, 3).toString());
            txtGiaNuoc.setText(tblChiNhanh.getValueAt(selectedRow, 4).toString());
        }
    }

    private ChiNhanh getFormData() {
        ChiNhanh cn = new ChiNhanh();
        cn.setTenChiNhanh(txtTenChiNhanh.getText());
        cn.setDiaChi(txtDiaChi.getText());
        cn.setGiaDien(Double.parseDouble(txtGiaDien.getText()));
        cn.setGiaNuoc(Double.parseDouble(txtGiaNuoc.getText()));
        return cn;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtTenChiNhanh = new javax.swing.JTextField();
        txtGiaNuoc = new javax.swing.JTextField();
        txtGiaDien = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnUpdate = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtDiaChi = new javax.swing.JTextField();
        btnClear = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblChiNhanh = new javax.swing.JTable();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Quản lý chi nhánh");

        txtTenChiNhanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenChiNhanhActionPerformed(evt);
            }
        });

        jLabel2.setText("Giá điện");

        jLabel3.setText("Giá nước");

        jLabel4.setText("Tên chi nhánh");

        btnUpdate.setText("Sửa");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnAdd.setText("Thêm");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnDelete.setText("Xóa");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        jLabel5.setText("Tìm kiếm ");

        btnClear.setText("Làm mới");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnSearch.setText("tìm");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTenChiNhanh, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                            .addComponent(jLabel4)
                            .addComponent(txtSearch))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearch)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnAdd)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnUpdate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDelete)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnClear))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtGiaDien, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtGiaNuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTenChiNhanh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGiaDien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGiaNuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdate)
                    .addComponent(btnDelete)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd)
                    .addComponent(btnClear)
                    .addComponent(btnSearch))
                .addContainerGap())
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
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tblChiNhanhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblChiNhanhMouseClicked
        fillFormFromSelectedRow();
    }//GEN-LAST:event_tblChiNhanhMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        try {
            if (validateForm()) {
                ChiNhanh cn = getFormData();
                chiNhanhService.insert(cn);
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                loadDataToTable();
                clearForm();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm dữ liệu: " + e.getMessage());
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        try {
            int selectedRow = tblChiNhanh.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một chi nhánh để cập nhật");
                return;
            }
            
            if (validateForm()) {
                int id = (int) tblChiNhanh.getValueAt(selectedRow, 0);
                ChiNhanh cn = getFormData();
                cn.setId_ChiNhanh(id);
                
                chiNhanhService.update(cn);
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadDataToTable();
                clearForm();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật dữ liệu: " + e.getMessage());
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        try {
            int selectedRow = tblChiNhanh.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một chi nhánh để xóa");
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa chi nhánh này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            
            int id = (int) tblChiNhanh.getValueAt(selectedRow, 0);
            chiNhanhService.delete(id);
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
            loadDataToTable();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa dữ liệu: " + e.getMessage());
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearForm();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        String keyword = txtSearch.getText();
        if (keyword.isEmpty()) {
            loadDataToTable();
        } else {
            try {
                model.setRowCount(0);
                List<ChiNhanh> list = chiNhanhService.findByKeyword(keyword);
                for (ChiNhanh cn : list) {
                    model.addRow(new Object[]{
                        cn.getId_ChiNhanh(),
                        cn.getTenChiNhanh(),
                        cn.getDiaChi(),
                        cn.getGiaDien(),
                        cn.getGiaNuoc()
                    });
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void txtTenChiNhanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenChiNhanhActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenChiNhanhActionPerformed

    private boolean validateForm() {
        if (txtTenChiNhanh.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên chi nhánh");
            return false;
        }
        if (txtDiaChi.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập địa chỉ");
            return false;
        }
        try {
            Double.parseDouble(txtGiaDien.getText());
            Double.parseDouble(txtGiaNuoc.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá điện và giá nước phải là số");
            return false;
        }
        return true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblChiNhanh;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtGiaDien;
    private javax.swing.JTextField txtGiaNuoc;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTenChiNhanh;
    // End of variables declaration//GEN-END:variables
}