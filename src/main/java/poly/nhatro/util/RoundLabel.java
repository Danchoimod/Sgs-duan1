/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.nhatro.util;

/**
 *
 * @author admin
 */
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class RoundLabel extends JLabel {

    public RoundLabel() {
        setOpaque(false);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ hình tròn với màu nền
        if (getBackground() != null) {
            g2.setColor(getBackground());
            g2.fillOval(0, 0, getWidth(), getHeight());
        }

        // Vẽ text
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        int max = Math.max(d.width, d.height);
        return new Dimension(max, max);
    }
}
