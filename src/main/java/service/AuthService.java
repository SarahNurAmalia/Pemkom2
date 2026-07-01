/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import object.User;
import dao.GenericDAO;
import gui.KaryawanPanel; // Halaman tujuan
import gui.LoginPage;
import utill.SecurityUtils;
import com.mongodb.client.model.Filters;
import gui.AdminPage;
import java.awt.Frame;
import javax.swing.JOptionPane;
import java.time.LocalDateTime;
import javax.swing.SwingUtilities;

/**
 *
 * @author ASUS
 */
public class AuthService {
    // Inisialisasi DAO untuk koleksi "users" [8]
    private final GenericDAO<User> userDAO = new GenericDAO<>("users", User.class);

    /**
     * Melakukan proses login dengan memvalidasi kredensial (Sub-CPMK 4) [5].
     *
     * @param username
     * @param plainPassword
     * @param loginPage
     */
    public void login(String username, String plainPassword, LoginPage loginPage) {
        // 1. Mengubah password input menjadi hash SHA-256 untuk keamanan [2]
        String hashedInput = SecurityUtils.getHash(plainPassword, SecurityUtils.SHA_256);

        // 2. Mencari user di database berdasarkan username DAN password hash [7, 9]
        User user = userDAO.findOne(Filters.and(
                Filters.eq("username", username),
                Filters.eq("password", hashedInput)
        ));

        // 3. Validasi hasil pencarian
        if (user != null) {
            // Update waktu login terakhir
            user.setLastLogin(LocalDateTime.now());
            userDAO.update(Filters.eq("username", username), user);

            // Berhasil: Masuk ke Halaman Admin
            // Berhasil login
            JOptionPane.showMessageDialog(null, "Selamat Datang, " + user.getFullname());

            // Buka AdminPage (yang ada sidebar)
            AdminPage admin = new AdminPage();
            admin.setExtendedState(Frame.MAXIMIZED_BOTH);
            admin.setLocationRelativeTo(null);
            admin.setVisible(true);

            // Tutup halaman login
            SwingUtilities.getWindowAncestor(loginPage).dispose();
        } else {
            // Gagal: Notifikasi Error
            JOptionPane.showMessageDialog(null,
                    "Username atau Password Salah!",
                    "Login Gagal",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Metode untuk menambahkan user/admin baru ke database. Implementasi sesuai
     * target SPRINT 3 untuk pengamanan kredensial [2].
     *
     * @param fullname Nama lengkap user
     * @param username Username untuk login
     * @param plainPassword Password mentah (akan di-hash otomatis)
     */
    public void registerUser(String fullname, String username, String plainPassword) {

    String hashedPassword = SecurityUtils.getHash(
            plainPassword,
            SecurityUtils.SHA_256
    );

    User newUser = new User(
            fullname,
            username,
            hashedPassword,
            null
    );

    try {

        userDAO.save(newUser);

        System.out.println("BERHASIL SIMPAN USER");

    } catch (Exception e) {
    System.out.println("ERROR = " + e.getMessage());

    }
}
}
