package poly.nhatro.util;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class XIcon {
    /**
     * Đọc icon từ resource hoặc file
     * @param path đường dẫn file, đường dẫn resource hoặc tên resource
     * @return ImageIcon
     */
    public static ImageIcon getIcon(String path) {
        if(!path.contains("/") && !path.contains("\\")){ // resource name
            // Thử tìm trong thư mục images trước
            try {
                java.net.URL imageUrl = XIcon.class.getResource("/images/" + path);
                if (imageUrl != null) {
                    return new ImageIcon(imageUrl);
                }
            } catch (Exception e) {
                // Ignore và thử fallback
            }
            
            try {
                // Fallback về thư mục icons cũ
                java.net.URL iconUrl = XIcon.class.getResource("/poly/nhatro/icons/" + path);
                if (iconUrl != null) {
                    return new ImageIcon(iconUrl);
                }
            } catch (Exception e) {
                // Ignore và thử đường dẫn file trực tiếp
            }
            
            // Nếu không tìm thấy trong resources, thử đường dẫn file trực tiếp
            return new ImageIcon(path);
        }
        
        if(path.startsWith("/")){ // resource path
            try {
                java.net.URL resourceUrl = XIcon.class.getResource(path);
                if (resourceUrl != null) {
                    return new ImageIcon(resourceUrl);
                }
            } catch (Exception e) {
                // Ignore và thử đường dẫn file
            }
            return new ImageIcon(path);
        }
        
        // Xử lý đường dẫn relative bắt đầu bằng "images/"
        if(path.startsWith("images/")){
            try {
                java.net.URL imageUrl = XIcon.class.getResource("/" + path);
                if (imageUrl != null) {
                    return new ImageIcon(imageUrl);
                }
            } catch (Exception e) {
                // Ignore
            }
        }
        
        // Fallback: sử dụng đường dẫn file trực tiếp
        return new ImageIcon(path);
    }
    /**
     * Đọc icon theo kích thước
     * @param path đường dẫn file hoặc tài nguyên
     * @param width chiều rộng
     * @param height chiều cao
     * @return Icon
     */
    public static ImageIcon getIcon(String path, int width, int height) {
        Image image = getIcon(path).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }
    /**
     * Thay đổi icon của JLabel
     * @param label JLabel cần thay đổi
     * @param path đường dẫn file hoặc tài nguyên
     */
    public static void setIcon(JLabel label, String path) {
        // Lấy kích thước JLabel, nếu chưa có thì dùng kích thước mặc định
        int width = label.getWidth() > 0 ? label.getWidth() : 150;
        int height = label.getHeight() > 0 ? label.getHeight() : 100;
        
        try {
            ImageIcon icon = XIcon.getIcon(path, width, height);
            label.setIcon(icon);
        } catch (Exception e) {
            System.err.println("Lỗi khi load ảnh: " + path + " - " + e.getMessage());
            // Đặt text thay thế khi không load được ảnh
            label.setText("Không tải được ảnh");
        }
    }
    /**
     * Thay đổi icon của JLabel
     * @param label JLabel cần thay đổi
     * @param file file icon
     */
    public static void setIcon(JLabel label, File file) {
        XIcon.setIcon(label, file.getAbsolutePath());
    }
    /**
     * Sao chép file vào thư mục với tên file mới là duy nhất
     * @param fromFile file cần sao chép
     * @param folder thư mục đích
     * @return File đã sao chép
     */
    public static File copyTo(File fromFile, String folder) {
        // Tự động tạo thư mục đích nếu chưa tồn tại
        File folderFile = new File(folder);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
            System.out.println("Đã tạo thư mục: " + folder);
        }
        
        String fileExt = fromFile.getName().substring(fromFile.getName().lastIndexOf("."));
        File toFile = new File(folder, XStr.getKey() + fileExt);
        
        try {
            Files.copy(fromFile.toPath(), toFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Đã sao chép file: " + fromFile.getName() + " → " + toFile.getName());
            return toFile;
        } catch (IOException ex) {
            System.err.println("Lỗi khi sao chép file: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
    public static File copyTo(File fromFile) {
        return copyTo(fromFile, "files");
    }
}
