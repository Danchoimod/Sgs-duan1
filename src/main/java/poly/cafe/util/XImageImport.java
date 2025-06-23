package poly.cafe.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.ImageIcon;

public class XImageImport {
    public static void save(File src){
        File dst = new File("photos", src.getName());
        //Nếu chưa có thư mục hinhanh thì tạo
        if(!dst.getParentFile().exists()){
            dst.getParentFile().mkdirs();
        }
        Path from = Paths.get(src.getAbsolutePath());
        Path to = Paths.get(dst.getAbsolutePath());
        try {
            Files.copy(
                from,
                to,
                StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static ImageIcon read(String fileName){
        File path = new File("photos", fileName);
        return new ImageIcon(path.getAbsolutePath());
    }
}
