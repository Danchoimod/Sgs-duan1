package poly.nhatro.util;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.basic.BasicComboBoxUI;

/**
 * A custom JComboBox with rounded corners.
 */
public class RoundedComboBox<E> extends JComboBox<E> {
    private int cornerRadius = 15;

    public RoundedComboBox() {
        super();
        setOpaque(false);
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setUI(new BasicComboBoxUI()); // sử dụng giao diện cơ bản để custom dễ hơn
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // padding
    }

    public RoundedComboBox(E[] items) {
        super(items);
        setOpaque(false);
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setUI(new BasicComboBoxUI());
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (isEditable()) {
            super.paintComponent(g); // Nếu editable thì dùng mặc định
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Nền bo góc
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(getForeground().darker());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        g2.dispose();
    }
}
