/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author ASUS
 */


public class SidebarMainMenu extends JPanel implements service.I18nService.I18nChangeListener {

    private final Color SIDEBAR_BG = new Color(44, 28, 14);
    private final Color MENU_BG = new Color(62, 38, 18);
    private final Color SUBMENU_BG = new Color(30, 18, 8);
    private final Color HOVER_BG = new Color(180, 100, 20);
    private final Color ACTIVE_BG = new Color(255, 143, 0);
    private final Color TEXT_COLOR = new Color(255, 220, 150);

    private JButton activeButton = null;

    private final List<I18nButtonRef> localizedButtons = new ArrayList<>();

    private static class I18nButtonRef {
        JButton button;
        String i18nKey;
        I18nButtonRef(JButton button, String i18nKey) {
            this.button = button;
            this.i18nKey = i18nKey;
        }
    }

    public SidebarMainMenu() {
        this.setPreferredSize(new Dimension(260, 0));
        this.setBackground(new Color(30, 18, 8));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(createAccordion("sidebar.dashboard", new String[]{"sidebar.home"}));
        this.add(createAccordion("sidebar.datamaster", new String[]{"sidebar.karyawan", "sidebar.logabsensi", "sidebar.pengguna"}));
        this.add(createAccordion("sidebar.attendance", new String[]{"sidebar.kiosk", "sidebar.riwayat", "sidebar.analisis"}));
        this.add(createAccordion("sidebar.settings", new String[]{"sidebar.general"}));
        this.add(createAccordion("sidebar.report", new String[]{"sidebar.logabsensi", "sidebar.performance"}));
        this.add(Box.createVerticalGlue());

        updateMenuTexts();
        service.I18nService.registerListener(this);
    }

    private JPanel createAccordion(String headerKey, String[] menuKeys) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(30, 18, 8));

        JButton header = new JButton(headerKey);
        header.setFocusPainted(false);
        header.setBackground(MENU_BG);
        header.setForeground(TEXT_COLOR);
        header.setBorder(new EmptyBorder(15, 15, 15, 15));
        header.setHorizontalAlignment(SwingConstants.LEFT);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        header.setFont(new java.awt.Font("Segoe UI", 1, 13));

        localizedButtons.add(new I18nButtonRef(header, headerKey));

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(SIDEBAR_BG);

        for (String menuKey : menuKeys) {
            JButton btn = new JButton(menuKey);
            btn.setFocusPainted(false);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
            btn.setBackground(SUBMENU_BG);
            btn.setForeground(TEXT_COLOR);
            btn.setBorder(new EmptyBorder(10, 20, 10, 10));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setFont(new java.awt.Font("Segoe UI", 0, 12));

            localizedButtons.add(new I18nButtonRef(btn, menuKey));

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (activeButton != btn) btn.setBackground(HOVER_BG);
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (activeButton != btn) btn.setBackground(SUBMENU_BG);
                }
            });

            btn.addActionListener(e -> {
                switch (menuKey) {
                    case "sidebar.home" -> showPage(new DashboardPanel());
                    case "sidebar.karyawan" -> showPage(new KaryawanPanel());
                    case "sidebar.logabsensi" -> showPage(new LogAbsensiPanel());
                    case "sidebar.kiosk" -> showPage(new AttendancePage());
                    case "sidebar.general" -> showPage(new Settings());
                    default -> {}
                }

                if (activeButton != null) activeButton.setBackground(SUBMENU_BG);
                activeButton = btn;
                btn.setBackground(ACTIVE_BG);
            });

            body.add(btn);
        }

        body.setVisible(false);

        header.addActionListener(e -> {
            body.setVisible(!body.isVisible());
            container.revalidate();
            container.repaint();
        });

        container.add(header);
        container.add(body);
        return container;
    }

    private void updateMenuTexts() {
        for (I18nButtonRef ref : localizedButtons) {
            ref.button.setText(service.I18nService.get(ref.i18nKey));
        }
    }

    private void showPage(Component comp) {
        switch (comp) {
            case JPanel pnl -> {
                AdminPage.appContentPane.removeAll();
                AdminPage.appContentPane.add(pnl, BorderLayout.CENTER);
                AdminPage.appContentPane.revalidate();
                AdminPage.appContentPane.repaint();
            }
            case JFrame frm -> {
                JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(SidebarMainMenu.this);
                if (mainFrame != null) mainFrame.dispose();
                frm.setExtendedState(Frame.MAXIMIZED_BOTH);
                frm.setVisible(true);
            }
            default -> {}
        }
    }

    @Override
    public void onLanguageChanged() {
        SwingUtilities.invokeLater(() -> {
            updateMenuTexts();
            revalidate();
            repaint();
        });
    }
}