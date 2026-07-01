package service;

import dao.GenericDAO;
import gui.KaryawanPanel;
import object.Karyawan;
import com.mongodb.client.model.Filters;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
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
        panelTarget.setBackground(new Color(94, 70, 21));

        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        for (Karyawan k : daftarKaryawan) {
            try {
            
                JPanel cardPanel = new JPanel(new GridLayout(4, 1, 0, 0));
                cardPanel.setBackground(new Color(139, 90, 43));
                cardPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.MAGENTA, 1, true),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));

                JLabel lblNama = new JLabel("Nama: " + k.getNamaLengkap());
                lblNama.setForeground(Color.WHITE);

                // FIX: handle null dari decrypt
                String rawId = k.getIdKaryawan();
                String idKaryawan;
                if (rawId == null || rawId.isEmpty()) {
                    idKaryawan = "(belum diisi)";
                } else {
                    idKaryawan = EncryptionUtils.decrypt(rawId);
                    if (idKaryawan == null) idKaryawan = "(gagal decrypt)";
                }
                JLabel lblIDK = new JLabel("ID Karyawan: " + idKaryawan);
                lblIDK.setForeground(Color.WHITE);

                JLabel lblJabt = new JLabel("Jabatan: " + k.getJabatan());
                lblJabt.setForeground(Color.WHITE);

                JPanel controlPanel = new JPanel(new GridLayout(1, 2, 20, 15));
                controlPanel.setBackground(new Color(237, 125, 49));

                JButton tombolEdit = new JButton("Edit");
                tombolEdit.setBackground(Color.ORANGE);
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

                JButton tombolDelete = new JButton("Delete");
                tombolDelete.setBackground(Color.RED);
                tombolDelete.setForeground(Color.WHITE);
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
    
    
}