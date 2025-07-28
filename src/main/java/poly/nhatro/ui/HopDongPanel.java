/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package poly.nhatro.ui;

import poly.nhatro.entity.HopDong;
import poly.nhatro.dao.HopDongDAO;
import poly.nhatro.dao.impl.HopDongImpl;
import poly.nhatro.util.XDate;
import poly.nhatro.util.XDialog;
import javax.swing.table.DefaultTableModel;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Phu Pham
 */
public class HopDongPanel extends javax.swing.JPanel {

    private HopDongDAO hopDongDAO;
    private DefaultTableModel tblModel;

    private final String DATE_FORMAT = "dd/MM/yyyy";

    /**
     * Creates new form HopDongPanel
     */
    public HopDongPanel() {
        initComponents();
        init();
   
    }

    private void init() {
        hopDongDAO = new HopDongImpl();
        tblModel = (DefaultTableModel) jTable2.getModel();
        String[] columnNames = {"Mã hợp đồng", "Ngày bắt đầu", "Ngày kết thúc", "Tiền cọc", "Mã người dùng", "Phòng", "Chi nhánh"};
        tblModel.setColumnIdentifiers(columnNames);

        fillTable();
        updateStatus();
        clearForm();
    }

    private void fillTable() {
        tblModel.setRowCount(0);
        try {
            List<HopDong> list = hopDongDAO.getAll();
            for (HopDong hd : list) {
                Object[] row = {
                    hd.getID_HopDong(),
                    XDate.format(hd.getNgayBatDau(), DATE_FORMAT),
                    XDate.format(hd.getNgayKetThuc(), DATE_FORMAT),
                    hd.getSoTienCoc(),
                    hd.getID_NguoiDung(),
                    hd.getID_Phong(),
                    hd.getID_ChiNhanh()
                };
                tblModel.addRow(row);
            }
        } catch (RuntimeException e) {
            XDialog.alert("Lỗi tải dữ liệu hợp đồng: ");
            e.printStackTrace();
        }
    }

    private void clearForm() {
        jTextField1.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
        jTextField7.setText("");

        Date currentDate = XDate.now();
        jTextField2.setText(XDate.format(currentDate, DATE_FORMAT));

        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.MONTH, 6);
        Date endDate = cal.getTime();
        jTextField3.setText(XDate.format(endDate, DATE_FORMAT));

        jTable2.clearSelection();
        updateStatus();
    }

    private HopDong readForm() {
        try {
            HopDong hd = new HopDong();

            if (!jTextField1.getText().isEmpty()) {
                try {
                    hd.setID_HopDong(Integer.parseInt(jTextField1.getText()));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Mã hợp đồng phải là một số nguyên hợp lệ.");
                }
            } else {
                hd.setID_HopDong(0);
            }


            Date ngayBatDau = XDate.parse(jTextField2.getText(), DATE_FORMAT);
            Date ngayKetThuc = XDate.parse(jTextField3.getText(), DATE_FORMAT);

            if (ngayBatDau == null) {
                throw new IllegalArgumentException("Ngày bắt đầu không đúng định dạng " + DATE_FORMAT);
            }
            if (ngayKetThuc == null) {
                throw new IllegalArgumentException("Ngày kết thúc không đúng định dạng " + DATE_FORMAT);
            }

            hd.setNgayBatDau(ngayBatDau);
            hd.setNgayKetThuc(ngayKetThuc);

            hd.setSoTienCoc(Double.parseDouble(jTextField4.getText()));

            try {
                hd.setID_NguoiDung(Integer.parseInt(jTextField5.getText()));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Mã người dùng phải là một số nguyên hợp lệ.");
            }
            try {
                hd.setID_Phong(Integer.parseInt(jTextField6.getText()));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Mã phòng phải là một số nguyên hợp lệ.");
            }
            try {
                hd.setID_ChiNhanh(Integer.parseInt(jTextField7.getText()));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Mã chi nhánh phải là một số nguyên hợp lệ.");
            }

            return hd;
        } catch (NumberFormatException e) {
            XDialog.alert("Vui lòng nhập số hợp lệ cho Tiền cọc, Mã người dùng, Mã phòng hoặc Mã chi nhánh.");
            throw new RuntimeException("Dữ liệu nhập không hợp lệ.", e);
        } catch (IllegalArgumentException e) {
            XDialog.alert("Lỗi định dạng ngày hoặc dữ liệu: ");
            throw new RuntimeException("Dữ liệu nhập không hợp lệ.", e);
        }
    }

    private void writeForm(HopDong hd) {
        jTextField1.setText(String.valueOf(hd.getID_HopDong()));
        jTextField2.setText(XDate.format(hd.getNgayBatDau(), DATE_FORMAT));
        jTextField3.setText(XDate.format(hd.getNgayKetThuc(), DATE_FORMAT));
        jTextField4.setText(String.valueOf(hd.getSoTienCoc()));
        jTextField5.setText(String.valueOf(hd.getID_NguoiDung()));
        jTextField6.setText(String.valueOf(hd.getID_Phong()));
        jTextField7.setText(String.valueOf(hd.getID_ChiNhanh()));
        jTextField1.setEditable(false);
    }

    private void updateStatus() {
        int selectedRow = jTable2.getSelectedRow();
        boolean edit = (selectedRow >= 0);

        jTextField1.setEditable(false);
        jButton1.setEnabled(!edit);
        jButton3.setEnabled(edit);
        jButton2.setEnabled(edit);
    }

    private void edit() {
        int selectedRow = jTable2.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }
        try {
            String maHD = jTable2.getValueAt(selectedRow, 0).toString();
            HopDong hd = hopDongDAO.getById(maHD);
            if (hd != null) {
                writeForm(hd);
                updateStatus();
            }
        } catch (RuntimeException e) {
            XDialog.alert("Lỗi khi tải thông tin hợp đồng để chỉnh sửa: ");
            e.printStackTrace();
        }
    }

    private void insert() {
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

            hopDongDAO.add(hd);
            fillTable();
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
            } else {
                XDialog.alert("Cập nhật thất bại: Đã xảy ra lỗi không xác định.");
            }
            e.printStackTrace();
        }
    }

    private void delete() {
        if (!XDialog.confirm("Bạn có chắc chắn muốn xóa hợp đồng này không?")) {
            return;
        }
        try {
            String maHD = jTextField1.getText();
            hopDongDAO.delete(maHD);
            fillTable();
            clearForm();
            XDialog.alert("Xóa thành công!");
        } catch (RuntimeException e) {
            String errorMessage = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
            if (errorMessage != null && errorMessage.contains("REFERENCE constraint") && errorMessage.contains("HOA_DON")) {
                XDialog.alert("Xóa thất bại: Hợp đồng này đang có hóa đơn liên quan. Vui lòng xóa các hóa đơn của hợp đồng này trước.");
            } else if (errorMessage != null) {
                XDialog.alert("Xóa thất bại: ");
            } else {
                XDialog.alert("Xóa thất bại: Đã xảy ra lỗi không xác định.");
            }
            e.printStackTrace();
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

        jCalendar1 = new com.toedter.calendar.JCalendar();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã hợp đồng", "Ngày bắt đầu", "Ngày kết thúc", "Tiền cọc", "Mã người dùng", "Phòng", "Chi nhánh"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
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

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("Ngày bắt đầu");

        jLabel2.setText("Mã hợp đồng");

        jLabel3.setText("Ngày kết thúc");

        jLabel4.setText("Tiền cọc");

        jLabel5.setText("Mã người dùng");

        jLabel6.setText("Chi nhánh");

        jLabel7.setText("Phòng");

        jButton1.setText("Thêm");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Xóa");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Sửa");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Làm mới");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1)
                    .addComponent(jTextField2)
                    .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField7)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3))
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 727, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.insert();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 1) {
            edit();
        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        this.update();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.delete();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        this.clearForm();
    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    // End of variables declaration//GEN-END:variables
}
