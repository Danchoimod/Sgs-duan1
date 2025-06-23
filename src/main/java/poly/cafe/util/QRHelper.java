package poly.cafe.util;
import java.awt.Image;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class QRHelper {

    public static void setQRCodeToLabel(JLabel label, String data) {
        try {
            String qrUrl = "https://api.qrserver.com/v1/create-qr-code/?data=" + 
                           java.net.URLEncoder.encode(data, "UTF-8") + 
                           "&size=150x150";
            URL url = new URL(qrUrl);

            InputStream input = url.openStream();
            Image qrImage = ImageIO.read(input);

            // Resize (nếu cần)
            ImageIcon icon = new ImageIcon(qrImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH));
            label.setIcon(icon);

        } catch (Exception e) {
            label.setText("Không thể tải QR");
            e.printStackTrace();
        }
    }
}
