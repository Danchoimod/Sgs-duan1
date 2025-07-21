package poly.nhatro.util;

import javax.swing.*;
import java.awt.*;

/**
 * A custom JPanel with rounded corners.
 */
public class RoundedPanel extends JPanel {
    private int cornerRadius = 20;

    public RoundedPanel() {
        super();
        setOpaque(false); // để vẽ bo góc
    }

    public RoundedPanel(int radius) {
        super();
        this.cornerRadius = radius;
        setOpaque(false);
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Kích hoạt anti-aliasing để nét mượt hơn
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ nền bo góc
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        g2.dispose();

        super.paintComponent(g); // vẽ phần bên trong như JLabel, Button...
    }
}
