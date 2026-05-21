/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serial;

/**
 *
 * @author ASUS
 * @param <T>
 */


public interface SerialDataHandler<T> {
    void onDataReceived(T data);
}