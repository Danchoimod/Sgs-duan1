package poly.nhatro.util;

import javax.swing.*;
import java.awt.*;

/**
 * A custom JScrollPane with rounded corners for tables.
 */
public class RoundedScrollPane extends JScrollPane {
    private int cornerRadius = 20;

    public RoundedScrollPane() {
        super();
        setupRounding();
    }

    public RoundedScrollPane(Component view) {
        super(view);
        setupRounding();
    }

    public RoundedScrollPane(Component view, int radius) {
        super(view);
        this.cornerRadius = radius;
        setupRounding();
    }

    public RoundedScrollPane(int radius) {
        super();
        this.cornerRadius = radius;
        setupRounding();
    }

    private void setupRounding() {
        setOpaque(false); // để vẽ bo góc
        getViewport().setOpaque(false); // cho view bên trong trong suốt
        setBorder(null); // loại bỏ border mặc định
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ nền bo góc
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Vẽ border nếu muốn
        g2.setColor(Color.LIGHT_GRAY);
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Không vẽ border mặc định để tránh xung đột với rounded border
    }
}
