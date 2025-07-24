package poly.nhatro.util;

import java.awt.Desktop;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import javax.swing.JTable;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Lớp tiện ích để xuất dữ liệu từ JTable ra file Excel.
 */
public class ExcelOutput {

    private static final String DEFAULT_FILE_NAME = "BaoCaoTongHop.xlsx";
    private static final String DEFAULT_FILE_PATH = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + DEFAULT_FILE_NAME;

    private static Workbook workbook = null;
    private static File excelFile = null;

    /**
     * Xuất dữ liệu vào file Excel (nhiều sheet trong cùng 1 file)
     */
    public static void exportJTableToExcel(JTable table, String sheetTitle, String sheetName) {
        try {
            // Kiểm tra và khởi tạo workbook
            initializeWorkbook();

            // Kiểm tra sheet đã tồn tại chưa
            Sheet existingSheet = workbook.getSheet(sheetName);
            if (existingSheet != null) {
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Sheet '" + sheetName + "' đã tồn tại. Bạn có muốn ghi đè?",
                        "Xác nhận", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.NO_OPTION) {
                    // Thêm số vào tên sheet nếu không muốn ghi đè
                    int i = 1;
                    while (workbook.getSheet(sheetName + "_" + i) != null) {
                        i++;
                    }
                    sheetName = sheetName + "_" + i;
                } else {
                    // Xóa sheet cũ nếu muốn ghi đè
                    int sheetIndex = workbook.getSheetIndex(sheetName);
                    workbook.removeSheetAt(sheetIndex);
                }
            }

            // Tạo sheet mới
            Sheet sheet = workbook.createSheet(sheetName);
            createSheetContent(sheet, table, sheetTitle);

            // Lưu file
            saveWorkbook();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Lỗi khi xuất Excel: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Khởi tạo workbook (tạo mới hoặc load từ file có sẵn)
     */
    
    private static void initializeWorkbook() throws IOException {
        if (workbook == null) {
            excelFile = new File(DEFAULT_FILE_PATH);

            if (excelFile.exists()) {
                // Nếu file đã tồn tại, load từ file
                try (FileInputStream fis = new FileInputStream(excelFile)) {
                    workbook = new XSSFWorkbook(fis);
                }
            } else {
                // Nếu file chưa tồn tại, tạo mới
                workbook = new XSSFWorkbook();
            }
        }
    }
    

    /**
     * Tạo nội dung cho sheet
     */
    private static void createSheetContent(Sheet sheet, JTable table, String sheetTitle) {
        TableModel model = table.getModel();

        // Style cho tiêu đề
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);

        // 1. Tạo tiêu đề sheet
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(sheetTitle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, model.getColumnCount() - 1));
        titleCell.setCellStyle(titleStyle);

        // 2. Thêm ngày xuất báo cáo
        Row dateRow = sheet.createRow(1);
        Cell dateCell = dateRow.createCell(0);
        dateCell.setCellValue("Ngày xuất: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, model.getColumnCount() - 1));

        // 3. Tạo header các cột
        Row headerRow = sheet.createRow(2);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        for (int i = 0; i < model.getColumnCount(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(model.getColumnName(i));
            cell.setCellStyle(headerStyle);
        }

        // 4. Điền dữ liệu
        for (int r = 0; r < model.getRowCount(); r++) {
            Row dataRow = sheet.createRow(r + 3);
            for (int c = 0; c < model.getColumnCount(); c++) {
                Cell cell = dataRow.createCell(c);
                Object value = model.getValueAt(r, c);

                if (value != null) {
                    if (value instanceof String) {
                        cell.setCellValue((String) value);
                    } else if (value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                    } else if (value instanceof Boolean) {
                        cell.setCellValue((Boolean) value);
                    } else if (value instanceof Date) {
                        CellStyle dateStyle = workbook.createCellStyle();
                        CreationHelper createHelper = workbook.getCreationHelper();
                        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
                        cell.setCellValue((Date) value);
                        cell.setCellStyle(dateStyle);
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
            }
        }

        // 5. Tự động điều chỉnh độ rộng cột
        for (int i = 0; i < model.getColumnCount(); i++) {
            sheet.autoSizeColumn(i);

            // Bổ sung: Đặt độ rộng tối thiểu và tối đa nếu cần
            int currentWidth = sheet.getColumnWidth(i);
            int maxWidth = 100 * 256; // ~100 characters
            int minWidth = 10 * 256;  // ~10 characters

            if (currentWidth > maxWidth) {
                sheet.setColumnWidth(i, maxWidth);
            } else if (currentWidth < minWidth) {
                sheet.setColumnWidth(i, minWidth);
            }
        }
    }

    /**
     * Lưu workbook vào file
     */
    private static void saveWorkbook() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(excelFile)) {
            workbook.write(fos);

            JOptionPane.showMessageDialog(null,
                    "Dữ liệu đã được lưu vào file: " + excelFile.getAbsolutePath(),
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);

            // Hỏi có muốn mở file không
            int option = JOptionPane.showConfirmDialog(null,
                    "Bạn có muốn mở file Excel ngay bây giờ không?",
                    "Mở file", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(excelFile);
            }
        }
    }

    /**
     * Đóng workbook khi không cần dùng nữa
     */
    public static void closeWorkbook() {
        if (workbook != null) {
            try {
                workbook.close();
                workbook = null;
                excelFile = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

