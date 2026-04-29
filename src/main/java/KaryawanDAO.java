/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ASUS
 */
import java.util.ArrayList;
import java.util.List;

public class KaryawanDAO implements DataAccessObject<Karyawan> {

    private final List<Karyawan> database = new ArrayList<>();

    /**
     *
     * @param data
     */
    @Override
    public void create(Karyawan data) {
        database.add(data);
        System.out.println("Data berhasil ditambahkan");
    }

    @Override
    public Karyawan read(String id) {
        for (Karyawan k : database) {
            if (k.getIdKaryawan().equals(id)) {
                return k;
            }
        }
        return null;
    }

    @Override
    public List<Karyawan> readAll() {
        return database;
    }

    @Override
    public void update(String id, Karyawan data) {
        for (int i = 0; i < database.size(); i++) {
            if (database.get(i).getIdKaryawan().equals(id)) {
                database.set(i, data);
                System.out.println("Data berhasil diupdate");
                return;
            }
        }
    }

    @Override
    public void delete(String id) {
        database.removeIf(k -> k.getIdKaryawan().equals(id));
        System.out.println("Data berhasil dihapus");
    }
}