package service;

import dao.GenericDAO;
import gui.KaryawanPanel;
import object.Karyawan;
import com.mongodb.client.model.Filters;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.bson.conversions.Bson;
import utill.EncryptionUtils;
import service.I18nService;

public class KaryawanService {
    private final GenericDAO<Karyawan> DAO;

    public KaryawanService() {
        this.DAO = new GenericDAO<>("karyawan", Karyawan.class);
    }

    public void tambahKaryawan(Karyawan karyawanBaru) {
        DAO.save(karyawanBaru);
    }

    public void tambahKaryawan(String uidRfid, String idKaryawan, String namaLengkap, String jabatan) {
        Karyawan karyawanBaru = new Karyawan(uidRfid, idKaryawan, namaLengkap, jabatan);
        DAO.save(karyawanBaru);
    }

    public void tampilkanDaftarKaryawan() {
        List<Karyawan> daftar = DAO.findAll();
        System.out.println("--- Daftar Karyawan ---");
        for (Karyawan k : daftar) {
            System.out.println(k.toString());
        }
    }

    public void tampilKaryawan(JPanel panelTarget, String key) {
        List<Karyawan> daftarKaryawan;
        if (key.isEmpty()) {
            daftarKaryawan = DAO.findAll();
        } else {
            daftarKaryawan = cariKaryawan(key);
        }

        panelTarget.removeAll();
        panelTarget.setLayout(new BorderLayout());
        panelTarget.setBackground(new Color(30, 18, 8));

        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        gridPanel.setOpaque(true);
        gridPanel.setBackground(new Color(30,18,8));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        for (Karyawan k : daftarKaryawan) {
            try {
            
                JPanel cardPanel = new JPanel(new GridLayout(4, 1, 0, 0));
                cardPanel.setBackground(new Color(62,38,18)); // coklat tua
                cardPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(255,183,77), 2, true),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));

                JLabel lblNama = new JLabel(I18nService.get("karyawan.nama") + ": " + k.getNamaLengkap());
               lblNama.setForeground(Color.WHITE);
               lblNama.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));

                // FIX: handle null dari decrypt
                String rawId = k.getIdKaryawan();
                String idKaryawan;
                if (rawId == null || rawId.isEmpty()) {
                    idKaryawan = "(belum diisi)";
                } else {
                    idKaryawan = EncryptionUtils.decrypt(rawId);
                    if (idKaryawan == null) idKaryawan = "(gagal decrypt)";
                }
                JLabel lblIDK = new JLabel(I18nService.get("karyawan.id") + ": " + idKaryawan);
                lblIDK.setForeground(Color.WHITE);
                lblIDK.setForeground(new Color(245,245,245));
                lblIDK.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                
               JLabel lblJabt = new JLabel(I18nService.get("karyawan.jabatan") + ": " + I18nService.get("jabatan." + k.getJabatan().toLowerCase()));
                lblJabt.setForeground(Color.WHITE);
                lblJabt.setForeground(new Color(245,245,245));
                lblJabt.setFont(new Font("Segoe UI", Font.PLAIN, 14));

                JPanel controlPanel = new JPanel(new GridLayout(1, 2, 20, 15));
                controlPanel.setBackground(new Color(74,44,18));

                JButton tombolEdit = new JButton(I18nService.get("karyawan.edit"));
                tombolEdit.setBackground(new Color(212,175,55));
                tombolEdit.setForeground(Color.BLACK);
                tombolEdit.setFocusPainted(false);
                tombolEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
                tombolEdit.addActionListener((ActionEvent e) -> {
                    KaryawanPanel.txtUID.setText(k.getUidRfid());
                    KaryawanPanel.txtKRID.setText(k.getIdKaryawan());
                    KaryawanPanel.txtKRID.setEnabled(false);
                    KaryawanPanel.txtKRName.setText(k.getNamaLengkap());
                    KaryawanPanel.txtKRJabt.setSelectedItem(k.getJabatan());
                    KaryawanPanel.btnUpdate.setEnabled(true);
                    KaryawanPanel.btnSave.setEnabled(false);
                });

                JButton tombolDelete = new JButton(I18nService.get("karyawan.hapus"));
                tombolDelete.setBackground(new Color(153,0,0));
                tombolDelete.setForeground(Color.WHITE);
                tombolDelete.setFocusPainted(false);
                tombolDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
                tombolDelete.addActionListener((ActionEvent e) -> {
                    Object[] options = {"Ya, Hapus", "Batal"};
                    int choice = JOptionPane.showOptionDialog(
                            null,
                            "Apakah Anda ingin menghapus data " + k.getNamaLengkap() + "?",
                            "Konfirmasi Pengelolaan",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]
                    );
                    switch (choice) {
                        case JOptionPane.YES_OPTION -> hapusKaryawan(k.getIdKaryawan());
                        case JOptionPane.NO_OPTION -> System.out.println("User memilih: Batal");
                        default -> {}
                    }
                });

                controlPanel.add(tombolEdit);
                controlPanel.add(tombolDelete);

                cardPanel.add(lblNama);
                cardPanel.add(lblIDK);
                cardPanel.add(lblJabt);
                cardPanel.add(controlPanel);

                gridPanel.add(cardPanel);

            } catch (Exception e) {
               
                e.printStackTrace();
                
            }
        }

        panelTarget.add(gridPanel, BorderLayout.NORTH);
        panelTarget.revalidate();
        panelTarget.repaint();
    }

    public List<Karyawan> cariKaryawan(String key) {
        List<Bson> filters = new ArrayList<>();
        for (Field field : Karyawan.class.getDeclaredFields()) {
            if (field.getName().equals("uidRfid")) {
                continue;
            }
            filters.add(Filters.regex(field.getName(), key, "i"));
        }
        return DAO.findMany(Filters.or(filters));
    }

    public void updateKaryawan(Karyawan newK) {
        Bson filter = Filters.eq("idKaryawan", newK.getIdKaryawan());
        Karyawan k = DAO.findOne(filter);
        if (k != null) {
            DAO.update(filter, newK);
            KaryawanPanel.showData("");
            JOptionPane.showMessageDialog(null, "Data berhasil diperbarui!");
        }
    }

    public void hapusKaryawan(String idK) {
        Bson filter = Filters.eq("idKaryawan", idK);
        DAO.delete(filter);
        KaryawanPanel.showData("");
        JOptionPane.showMessageDialog(null, "Data karyawan berhasil dihapus.");
    }

    public Karyawan findByUid(String uid) {
        Bson filter = Filters.eq("uidRfid", uid);
        return DAO.findOne(filter);
    }
    
    public int getTotalKaryawan() {
    return (int) DAO.findAll().size();
}
    
}