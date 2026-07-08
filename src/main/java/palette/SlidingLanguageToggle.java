/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package palette;

import gui.Settings;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
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
public class SlidingLanguageToggle extends JToggleButton {
     // Tema warna cafe
    private final Color COLOR_BG = new Color(44, 28, 14);              // coklat tua
    private final Color COLOR_SLIDER_ACTIVE = new Color(255, 143, 0);  // orange cafe

    private final int cornerRadius = 24;

    private int selectedLanguageIndex = 0;

    private final String[] languages = {"Indonesia", "English", "Espanol"};
    private final String[] langcodes = {"id", "en", "es"};

    public SlidingLanguageToggle() {
        super();
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(new Font("Segoe UI", Font.BOLD, 13));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int width = getWidth();
                int sectionWidth = width / 3;
                int clickX = e.getX();

                if (clickX < sectionWidth) {
                    setSelectedLanguageIndex(0);
                } else if (clickX < sectionWidth * 2) {
                    setSelectedLanguageIndex(1);
                } else {
                    setSelectedLanguageIndex(2);
                }

                Settings.prefs.put("LANGUAGE", langcodes[selectedLanguageIndex]);
                service.I18nService.setLocale(java.util.Locale.of(langcodes[selectedLanguageIndex]));
                repaint();
            }
        });
    }

    public int getSelectedLanguageIndex() {
        return selectedLanguageIndex;
    }

    public void setSelectedLanguageIndex(int index) {
        if (index >= 0 && index <= 2) {
            this.selectedLanguageIndex = index;
            repaint();
        }
    }

    public void setSelectedLanguageIndexByString(String lang) {
        switch (lang) {
            case "id" -> this.selectedLanguageIndex = 0;
            case "en" -> this.selectedLanguageIndex = 1;
            case "es" -> this.selectedLanguageIndex = 2;
        }
    }

    public String getSelectedLanguageString() {
        return switch (selectedLanguageIndex) {
            case 0 -> "id";
            case 1 -> "en";
            case 2 -> "es";
            default -> "id";
        };
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int margin = 5;

        int sliderWidth = (w / 3) - (margin + (margin / 3));
        int sliderHeight = h - (margin * 2);

        // Background
        g2.setColor(COLOR_BG);
        g2.fillRoundRect(0, 0, w, h, cornerRadius, cornerRadius);

        // Slider
        int sliderX = margin + (selectedLanguageIndex * (w / 3));
        if (selectedLanguageIndex == 1) sliderX += 2;
        if (selectedLanguageIndex == 2) sliderX = w - sliderWidth - margin;

        g2.setColor(COLOR_SLIDER_ACTIVE);
        g2.fillRoundRect(sliderX, margin, sliderWidth, sliderHeight, cornerRadius - 6, cornerRadius - 6);

        // Teks
        FontMetrics fm = g2.getFontMetrics();
        int textY = (h / 2) + (fm.getAscent() / 2) - 2;

        for (int i = 0; i < languages.length; i++) {
            String text = languages[i];
            int targetCenterX = (w / 6) + (i * (w / 3));
            int textX = targetCenterX - (fm.stringWidth(text) / 2);

            if (i == selectedLanguageIndex) {
                g2.setColor(Color.WHITE);
            } else {
                g2.setColor(new Color(255, 220, 150)); // krem
            }

            g2.drawString(text, textX, textY);
        }

        g2.dispose();
    }
    
}
