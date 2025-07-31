package poly.nhatro.controller;

import poly.nhatro.dao.impl.GopYDAOImpl;
import poly.nhatro.entity.GopY;
import poly.nhatro.ui.GopYJdilog; // Đảm bảo import đúng đường dẫn của JDialog
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * Lớp điều khiển (Controller) cho chức năng Quản lý Góp ý.
 * Xử lý các logic liên quan đến UI và tương tác trực tiếp với DAO.
 * Phiên bản này chỉ hỗ trợ Sửa và Xóa, không có Thêm.
 */
public class GopYController {
  private final GopYJdilog dialog;
    private final GopYDAOImpl gopYDAO;
    private List<GopY> listGopY;
    private int currentIndex = -1; // Mặc định không có hàng nào được chọn

    public GopYController(GopYJdilog dialog) {
        this.dialog = dialog;
        this.gopYDAO = new GopYDAOImpl();
        init();
    }

    private void init() {
        fillToTable(); // Vẫn điền dữ liệu vào bảng
        
        currentIndex = -1; // Đảm bảo không có hàng nào được chọn mặc định
        dialog.tblGopY.clearSelection(); // Xóa chọn trên bảng
        clear(); // Xóa các trường text field khi khởi động
        updateStatus();
    }

    public void setForm(GopY entity) {
        dialog.txtIDGopY.setText(String.valueOf(entity.getIdGopY()));
        dialog.txtNoiDung.setText(entity.getNoiDung());
        dialog.txtIDNguoiDung.setText(String.valueOf(entity.getIdNguoiDung()));
        dialog.txtIDChiNhanh.setText(String.valueOf(entity.getIdChiNhanh()));

        System.out.println("DEBUG: Đặt form với ID Người dùng: " + entity.getIdNguoiDung() +
                           ", ID Chi nhánh: " + entity.getIdChiNhanh());
    }

    public GopY getForm() {
        // ID Góp ý sẽ được tạo tự động hoặc lấy từ bảng, không cho phép sửa
        // nên không cần validate ở đây khi lấy form cho mục đích update/delete
        // Khi thêm mới thì IDGopY sẽ là 0 hoặc null, DAO sẽ xử lý tự tăng.
        
        String noiDung = dialog.txtNoiDung.getText().trim(); // Lấy và loại bỏ khoảng trắng thừa
        if (noiDung.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Nội dung góp ý không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        int idNguoiDung;
        try {
            idNguoiDung = Integer.parseInt(dialog.txtIDNguoiDung.getText().trim()); // Trim để loại bỏ khoảng trắng
            if (idNguoiDung <= 0) {
                 JOptionPane.showMessageDialog(dialog, "Mã người dùng phải là số nguyên dương.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                 return null;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(dialog, "Mã người dùng phải là một số nguyên hợp lệ.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        int idChiNhanh;
        try {
            idChiNhanh = Integer.parseInt(dialog.txtIDChiNhanh.getText().trim()); // Trim để loại bỏ khoảng trắng
            if (idChiNhanh <= 0) {
                 JOptionPane.showMessageDialog(dialog, "Mã chi nhánh phải là số nguyên dương.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                 return null;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(dialog, "Mã chi nhánh phải là một số nguyên hợp lệ.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        GopY gy = new GopY(noiDung, idNguoiDung, idChiNhanh);
        
        // Chỉ set ID_GopY nếu có giá trị từ form (khi đang sửa)
        if (!dialog.txtIDGopY.getText().isEmpty()) {
            try {
                int idGopY = Integer.parseInt(dialog.txtIDGopY.getText());
                gy.setIdGopY(idGopY);
            } catch (NumberFormatException e) {
                // Should not happen if txtIDGopY is always filled from table
                System.err.println("Lỗi chuyển đổi ID_GopY từ form: " + e.getMessage());
            }
        }
        return gy;
    }

    public void fillToTable() {
        DefaultTableModel model = (DefaultTableModel) dialog.tblGopY.getModel();
        model.setRowCount(0);

        try {
            listGopY = gopYDAO.findAll();
            for (GopY gy : listGopY) {
                Object[] row = {
                    gy.getIdGopY(),
                    gy.getNoiDung(),
                    gy.getIdNguoiDung(), 
                    gy.getIdChiNhanh()   
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
        if (selectedRow >= 0 && selectedRow < listGopY.size()) { // Thêm kiểm tra index hợp lệ
            currentIndex = selectedRow;
            setForm(listGopY.get(currentIndex)); // Chỉ điền form khi click
            updateStatus();
        } else {
            // Trường hợp không chọn hàng hợp lệ (ví dụ: bảng trống)
            clear(); // Xóa form nếu không có hàng nào được chọn hợp lệ
        }
    }

    public void update() {
        if (dialog.txtIDGopY.getText().isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Vui lòng chọn một góp ý để cập nhật.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        GopY gy = getForm(); // Lấy dữ liệu từ form và thực hiện validate ban đầu
        if (gy == null) { // Nếu getForm() trả về null, có lỗi validate
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(dialog, 
            "Bạn có chắc chắn muốn cập nhật góp ý ID: " + gy.getIdGopY() + " này?", 
            "Xác nhận cập nhật", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE); // Sử dụng icon câu hỏi

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                gopYDAO.update(gy);
                JOptionPane.showMessageDialog(dialog, "Cập nhật góp ý thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                fillToTable();
                // Sau khi update, chọn lại hàng hiện tại trên bảng và điền lại form
                if (!listGopY.isEmpty() && currentIndex != -1 && currentIndex < listGopY.size()) { // Kiểm tra currentIndex có hợp lệ sau khi fill lại
                    setForm(listGopY.get(currentIndex));
                    dialog.tblGopY.setRowSelectionInterval(currentIndex, currentIndex);
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
        
        // Đảm bảo ID góp ý là số hợp lệ trước khi hỏi xóa
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
            JOptionPane.QUESTION_MESSAGE); // Sử dụng icon câu hỏi

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                gopYDAO.deleteById(idGopYToDelete);
                JOptionPane.showMessageDialog(dialog, "Xóa góp ý thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                fillToTable();
                clear();
                currentIndex = -1; // Đảm bảo không còn hàng nào được chọn
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
        dialog.txtIDNguoiDung.setText("");
        dialog.txtIDChiNhanh.setText("");
        currentIndex = -1;
        dialog.tblGopY.clearSelection();
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
                currentIndex = listGopY.size() - 1; // Vòng lặp
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
                currentIndex = 0; // Vòng lặp
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

    private void updateStatus() {
        boolean first = currentIndex == 0;
        boolean last = currentIndex == listGopY.size() - 1;
        boolean empty = listGopY.isEmpty();

        dialog.btnSua.setEnabled(currentIndex != -1 && !empty);
        dialog.btnXoa.setEnabled(currentIndex != -1 && !empty);
        dialog.btnMoi.setEnabled(true);

        dialog.txtIDGopY.setEditable(false);
        dialog.txtNoiDung.setEditable(true);
        dialog.txtIDNguoiDung.setEditable(false);
        dialog.txtIDChiNhanh.setEditable(false); 
    }

    public void checkAll() {
        JOptionPane.showMessageDialog(dialog, "Chức năng 'Tích chọn tất cả' chưa được triển khai cho bảng này.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public void uncheckAll() {
        JOptionPane.showMessageDialog(dialog, "Chức năng 'Bỏ tích chọn tất cả' chưa được triển khai cho bảng này.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public void deleteCheckedItems() {
        JOptionPane.showMessageDialog(dialog, "Chức năng 'Xóa các mục được tích chọn' chưa được triển khai cho bảng này.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setEditable(boolean editable) {
        dialog.txtNoiDung.setEditable(editable);
        dialog.txtIDNguoiDung.setEditable(editable);
        // Mã chi nhánh không được sửa, nên loại bỏ khỏi đây
        // dialog.txtIDChiNhanh.setEditable(editable); 
    }
}