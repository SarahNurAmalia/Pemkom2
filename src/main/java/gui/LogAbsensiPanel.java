/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import dao.GenericDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import object.Karyawan;
import object.LogAbsensi;
import service.KaryawanService;

public class LogAbsensiPanel extends javax.swing.JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    public LogAbsensiPanel() {
        setupUI();
        loadData();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(94, 70, 21));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(94, 70, 21));
        JLabel lblTitle = new JLabel("Log Absensi");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18));
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.PAGE_START);

        // Table
        String[] columns = {"No", "UID RFID", "Nama Karyawan", "Waktu Tap", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new java.awt.Font("Segoe UI", 0, 12));
        table.getTableHeader().setFont(new java.awt.Font("Segoe UI", 1, 12));
        table.getTableHeader().setBackground(new Color(94, 70, 21));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        GenericDAO<LogAbsensi> logDAO = new GenericDAO<>("log_absensi", LogAbsensi.class);
        KaryawanService krService = new KaryawanService();
        List<LogAbsensi> logs = logDAO.findAll();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        int no = 1;
        for (LogAbsensi log : logs) {
            Karyawan k = krService.findByUid(log.getUidRfid());
            String namaKaryawan = (k != null) ? k.getNamaLengkap() : "(tidak dikenal)";
            String waktu = (log.getWaktuTap() != null) ? log.getWaktuTap().format(formatter) : "-";

            tableModel.addRow(new Object[]{
                no++,
                log.getUidRfid(),
                namaKaryawan,
                waktu,
                log.getStatus()
            });
        }
    }
}