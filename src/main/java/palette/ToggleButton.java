/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package palette;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JToggleButton;

/**
 *
 * @author ASUS
 */




public class ToggleButton extends JToggleButton {

    // Tema cafe
    private final Color COLOR_MASUK = new Color(62, 38, 18);          // coklat tua
    private final Color COLOR_MASUK_HOVER = new Color(90, 55, 25);    // coklat hover
    private final Color COLOR_PULANG = new Color(255, 143, 0);        // orange
    private final Color COLOR_PULANG_HOVER = new Color(230, 120, 0);  // orange hover
    
    private boolean isHovered = false;
    private final int cornerRadius = 8;

    public ToggleButton() {
        super("Masuk");
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });

        addActionListener(e -> {
            if (isSelected()) {
                setText("Pulang");
            } else {
                setText("Masuk");
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isSelected()) {
            g2.setColor(isHovered ? COLOR_PULANG_HOVER : COLOR_PULANG);
        } else {
            g2.setColor(isHovered ? COLOR_MASUK_HOVER : COLOR_MASUK);
        }

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        setForeground(Color.WHITE);
        g2.dispose();
        super.paintComponent(g);
    }
}
