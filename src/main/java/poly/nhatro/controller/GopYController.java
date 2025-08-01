/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.nhatro.controller;
import poly.nhatro.dao.impl.GopYDAOImpl;
import poly.nhatro.entity.GopY;
import poly.nhatro.ui.GopYJdilog; // Import JDialog để tương tác trực tiếp
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.awt.event.ActionEvent; // Import for ActionEvent
import java.awt.event.ActionListener; // Import for ActionListener
/**
 * Lớp điều khiển (Controller) cho chức năng Quản lý Góp ý.
 * Xử lý các logic liên quan đến UI và tương tác trực tiếp với DAO.
 */
public class GopYController {
    private final GopYJdilog dialog; // Tham chiếu đến JDialog
    private final GopYDAOImpl gopYDAO; // Trực tiếp sử dụng DAO
    private List<GopY> listGopY; // Danh sách dữ liệu hiện tại trên bảng
    private int currentIndex = -1; // Vị trí hàng hiện tại đang được chọn/hiển thị

    public GopYController(GopYJdilog dialog) {
        this.dialog = dialog;
        this.gopYDAO = new GopYDAOImpl();
        init(); // Khởi tạo dữ liệu và trạng thái ban đầu khi Controller được tạo

        // Thêm ActionListener cho trường tìm kiếm
        dialog.txtTimKiem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search(); // Gọi phương thức tìm kiếm khi nhấn Enter
            }
        });
    }
    
    // Phương thức khởi tạo dữ liệu và trạng thái ban đầu
    private void init() {
        fillToTable(); // Tải dữ liệu ban đầu lên bảng
        currentIndex = -1; // Đảm bảo không có hàng nào được chọn mặc định
        dialog.tblGopY.clearSelection(); // Xóa chọn trên bảng
        clear(); // Xóa các trường text field khi khởi động
        updateStatus(); // Cập nhật trạng thái các nút
    }
    
    public void setForm(GopY entity) {
        if (entity != null) {
            dialog.txtIDGopY.setText(String.valueOf(entity.getIdGopY()));
            dialog.txtNoiDung.setText(entity.getNoiDung());
            dialog.txtTenNguoiDung.setText(entity.getTenNguoiDung()); // Display user name
            dialog.txtTenChiNhanh.setText(entity.getTenChiNhanh()); // Display branch name
            System.out.println("DEBUG: Đặt form với Tên Người dùng: " + entity.getTenNguoiDung() +
                               ", Tên Chi nhánh: " + entity.getTenChiNhanh());
        } else {
            clear();
        }
    }

    public GopY getForm() {
        String noiDung = dialog.txtNoiDung.getText().trim();
        if (noiDung.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Nội dung góp ý không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        String tenNguoiDung = dialog.txtTenNguoiDung.getText().trim();
        if (tenNguoiDung.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Tên người dùng không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        String tenChiNhanh = dialog.txtTenChiNhanh.getText().trim();
        if (tenChiNhanh.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Tên chi nhánh không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        Integer idNguoiDung = null;
        Integer idChiNhanh = null;

        try {
            idNguoiDung = gopYDAO.findNguoiDungIdByTen(tenNguoiDung);
            if (idNguoiDung == null) {
                JOptionPane.showMessageDialog(dialog, "Tên người dùng không tồn tại trong hệ thống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog, "Lỗi khi tìm kiếm ID người dùng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }

        try {
            idChiNhanh = gopYDAO.findChiNhanhIdByTen(tenChiNhanh);
            if (idChiNhanh == null) {
                JOptionPane.showMessageDialog(dialog, "Tên chi nhánh không tồn tại trong hệ thống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog, "Lỗi khi tìm kiếm ID chi nhánh: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }

        GopY gy = new GopY(noiDung, idNguoiDung, idChiNhanh);
        
        // Only set ID_GopY if there's a value from the form (when updating)
        if (!dialog.txtIDGopY.getText().isEmpty()) {
            try {
                int idGopY = Integer.parseInt(dialog.txtIDGopY.getText());
                gy.setIdGopY(idGopY);
            } catch (NumberFormatException e) {
                System.err.println("Lỗi chuyển đổi ID_GopY từ form: " + e.getMessage());
            }
        }
        return gy;
    }

    public void fillToTable() {
        DefaultTableModel model = (DefaultTableModel) dialog.tblGopY.getModel();
        model.setRowCount(0); // Clear existing rows
        try {
            listGopY = gopYDAO.findAll(); // Fetch all data including names
            for (GopY gy : listGopY) {
                Object[] row = {
                    gy.getIdGopY(),
                    gy.getNoiDung(),
                    gy.getTenNguoiDung(), // Display user name
                    gy.getTenChiNhanh()   // Display branch name
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog, "Lỗi tải dữ liệu lên bảng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Phương thức này được gọi khi click vào bảng
    public void edit() {
        int selectedRow = dialog.tblGopY.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < listGopY.size()) { 
            currentIndex = selectedRow;
            setForm(listGopY.get(currentIndex)); // Fill form with selected data
            updateStatus();
        } else {
            clear(); 
        }
    }

    public void update() {
        if (dialog.txtIDGopY.getText().isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Vui lòng chọn một góp ý để cập nhật.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        GopY gy = getForm(); 
        if (gy == null) { 
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(dialog, 
            "Bạn có chắc chắn muốn cập nhật góp ý ID: " + gy.getIdGopY() + " này?", 
            "Xác nhận cập nhật", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE); 
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                gopYDAO.update(gy);
                JOptionPane.showMessageDialog(dialog, "Cập nhật góp ý thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                fillToTable();
                // After update, try to re-select the updated row
                if (!listGopY.isEmpty() && currentIndex != -1 && currentIndex < listGopY.size()) { 
                    dialog.tblGopY.setRowSelectionInterval(currentIndex, currentIndex);
                    setForm(listGopY.get(currentIndex)); // Re-fill form with updated data
                } else {
                    clear();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(dialog, "Cập nhật thất bại: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    public void delete() {
        if (dialog.txtIDGopY.getText().isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Vui lòng chọn góp ý cần xóa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int idGopYToDelete;
        try {
            idGopYToDelete = Integer.parseInt(dialog.txtIDGopY.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(dialog, "Mã góp ý không hợp lệ để xóa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(dialog, 
            "Bạn có chắc chắn muốn xóa góp ý ID: " + idGopYToDelete + " này?", 
            "Xác nhận xóa", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE); 
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                gopYDAO.deleteById(idGopYToDelete);
                JOptionPane.showMessageDialog(dialog, "Xóa góp ý thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                fillToTable();
                clear();
                currentIndex = -1; 
                updateStatus();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(dialog, "Xóa thất bại: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    public void clear() {
        dialog.txtIDGopY.setText("");
        dialog.txtNoiDung.setText("");
        dialog.txtTenNguoiDung.setText(""); // Clear user name field
        dialog.txtTenChiNhanh.setText(""); // Clear branch name field
        dialog.txtTimKiem.setText(""); // Clear search field
        currentIndex = -1;
        dialog.tblGopY.clearSelection();
        fillToTable(); // Reload all data after clearing search
        updateStatus();
    }

    public void moveFirst() {
        if (!listGopY.isEmpty()) {
            currentIndex = 0;
            setForm(listGopY.get(currentIndex));
            dialog.tblGopY.setRowSelectionInterval(currentIndex, currentIndex);
            updateStatus();
        }
    }

    public void movePrevious() {
        if (!listGopY.isEmpty()) {
            if (currentIndex > 0) {
                currentIndex--;
            } else {
                currentIndex = listGopY.size() - 1; // Wrap around
            }
            setForm(listGopY.get(currentIndex));
            dialog.tblGopY.setRowSelectionInterval(currentIndex, currentIndex);
            updateStatus();
        }
    }

    public void moveNext() {
        if (!listGopY.isEmpty()) {
            if (currentIndex < listGopY.size() - 1) {
                currentIndex++;
            } else {
                currentIndex = 0; // Wrap around
            }
            setForm(listGopY.get(currentIndex));
            dialog.tblGopY.setRowSelectionInterval(currentIndex, currentIndex);
            updateStatus();
        }
    }

    public void moveLast() {
        if (!listGopY.isEmpty()) {
            currentIndex = listGopY.size() - 1;
            setForm(listGopY.get(currentIndex));
            dialog.tblGopY.setRowSelectionInterval(currentIndex, currentIndex);
            updateStatus();
        }
    }

    // Phương thức tìm kiếm theo Mã góp ý
    public void search() {
        String searchIdText = dialog.txtTimKiem.getText().trim();
        DefaultTableModel model = (DefaultTableModel) dialog.tblGopY.getModel();
        model.setRowCount(0); // Clear existing rows

        if (searchIdText.isEmpty()) {
            fillToTable(); // If search field is empty, show all
            return;
        }

        try {
            int idToSearch = Integer.parseInt(searchIdText);
            GopY foundGopY = gopYDAO.findById(idToSearch);

            if (foundGopY != null) {
                Object[] row = {
                    foundGopY.getIdGopY(),
                    foundGopY.getNoiDung(),
                    foundGopY.getTenNguoiDung(),
                    foundGopY.getTenChiNhanh()
                };
                model.addRow(row);
                setForm(foundGopY); // Fill form with found data
                currentIndex = listGopY.indexOf(foundGopY); // Update currentIndex
                dialog.tblGopY.setRowSelectionInterval(0, 0); // Select the first (and only) row
            } else {
                JOptionPane.showMessageDialog(dialog, "Không tìm thấy góp ý với Mã: " + idToSearch, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                clear(); // Clear form if not found
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(dialog, "Mã góp ý phải là một số nguyên hợp lệ.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            clear(); // Clear form if invalid input
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog, "Lỗi khi tìm kiếm góp ý: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            clear();
        } finally {
            updateStatus(); // Update button status after search
        }
    }


    // Phương thức helper để cập nhật trạng thái các nút và trường nhập liệu
    private void updateStatus() {
        boolean first = currentIndex == 0;
        boolean last = currentIndex == listGopY.size() - 1;
        boolean empty = listGopY.isEmpty();
        
        
        dialog.btnXoa.setEnabled(currentIndex != -1 && !empty);
  
        
        // Trạng thái editability của các trường
        dialog.txtIDGopY.setEditable(false); // ID luôn không sửa được
        dialog.txtNoiDung.setEditable(false); // Đã sửa thành false
        dialog.txtTenNguoiDung.setEditable(false); // Đã sửa thành false
        dialog.txtTenChiNhanh.setEditable(false); // Đã sửa thành false
    }
    
    // Các phương thức checkAll, uncheckAll, deleteCheckedItems chưa được triển khai
    // vì chúng không có giao diện cụ thể trên JDialog này (ví dụ: checkbox trên bảng)
    // Nếu cần, bạn có thể thêm logic cho chúng.
    public void checkAll() {
        JOptionPane.showMessageDialog(dialog, "Chức năng 'Tích chọn tất cả' chưa được triển khai cho bảng này.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    public void uncheckAll() {
        JOptionPane.showMessageDialog(dialog, "Chức năng 'Bỏ tích chọn tất cả' chưa được triển khai cho bảng này.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    public void deleteCheckedItems() {
        JOptionPane.showMessageDialog(dialog, "Chức năng 'Xóa các mục được tích chọn' chưa được triển khai cho bảng này.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // setEditable có thể được gọi từ bên ngoài nếu cần thay đổi toàn bộ trạng thái form
    public void setEditable(boolean editable) {
        dialog.txtNoiDung.setEditable(editable);
        dialog.txtTenNguoiDung.setEditable(editable);
        dialog.txtTenChiNhanh.setEditable(editable);
    }
}
