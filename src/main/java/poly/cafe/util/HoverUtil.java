package poly.cafe.util;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;

public class HoverUtil {
    
    private static final Color HOVER_COLOR = new Color(220, 220, 220); // Light Gray
    private static final Color DEFAULT_COLOR = new Color(249,249,249);

    public static void setHoverEffect(JLabel label) {
        label.setOpaque(true); // Đảm bảo label có background
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setBackground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setBackground(DEFAULT_COLOR);
            }
        });
    }

    public static void setHoverEffect(JLabel label, Color hoverColor, Color defaultColor) {
        label.setOpaque(true);
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setBackground(defaultColor);
            }
        });
    }
}
