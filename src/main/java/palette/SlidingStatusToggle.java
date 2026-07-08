/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package palette;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JToggleButton;

/**
 *
 * @author ASUS
 */

public class SlidingStatusToggle extends JToggleButton implements service.I18nService.I18nChangeListener {

    private final Color COLOR_BG = new Color(39, 45, 54);
    private final Color COLOR_SLIDER_MASUK = new Color(25, 135, 84);
    private final Color COLOR_SLIDER_PULANG = new Color(220, 53, 69);
    private final int cornerRadius = 24;

    public SlidingStatusToggle() {
        super();
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(new Font("SansSerif", Font.BOLD, 14));
        addActionListener(e -> repaint());
        service.I18nService.registerListener(this);
    }

    public void setStatusByString(String status) {
        if ("Pulang".equalsIgnoreCase(status)) {
            this.setSelected(true);
        } else {
            this.setSelected(false);
        }
        repaint();
    }

    public String getStatusString() {
        return isSelected() ? "Pulang" : "Masuk";
    }

    @Override
    public void onLanguageChanged() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int margin = 5;
        int sliderWidth = (w / 2) - margin;
        int sliderHeight = h - (margin * 2);

        boolean isPulangActive = isSelected();

        // Background
        g2.setColor(COLOR_BG);
        g2.fillRoundRect(0, 0, w, h, cornerRadius, cornerRadius);

        // Slider
        int sliderX = isPulangActive ? (w / 2) : margin;
        g2.setColor(isPulangActive ? COLOR_SLIDER_PULANG : COLOR_SLIDER_MASUK);
        g2.fillRoundRect(sliderX, margin, sliderWidth, sliderHeight, cornerRadius - 6, cornerRadius - 6);

        // Teks
        FontMetrics fm = g2.getFontMetrics();
        int textY = (h / 2) + (fm.getAscent() / 2) - 2;

        // Teks Kiri - Masuk
        String textLeft = service.I18nService.get("status.masuk");
        int textLeftX = (w / 4) - (fm.stringWidth(textLeft) / 2);
        g2.setColor(!isPulangActive ? Color.WHITE : new Color(130, 135, 145));
        g2.drawString(textLeft, textLeftX, textY);

        // Teks Kanan - Pulang
        String textRight = service.I18nService.get("status.pulang");
        int textRightX = ((w / 4) * 3) - (fm.stringWidth(textRight) / 2);
        g2.setColor(isPulangActive ? Color.WHITE : new Color(130, 135, 145));
        g2.drawString(textRight, textRightX, textY);

        g2.dispose();
    }
}