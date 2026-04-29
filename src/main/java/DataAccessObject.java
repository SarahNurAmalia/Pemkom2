/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ASUS
 */
import java.util.List;

public interface DataAccessObject<T> {

    void create(T data);

    T read(String id);

    List<T> readAll();

    void update(String id, T data);

    void delete(String id);
}