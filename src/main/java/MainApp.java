/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ASUS
 */
public class MainApp {
       public static void main(String[] args) {
        Karyawan k = new Karyawan();
        k.setUidRfid("12345678");
        k.setIdKaryawan("3656");
        k.setNamaLengkap("Julian"); 
        k.setDepartemen("Barista"); 
        
//        String data = k.toString();
        System.err.println(k.toString());
        
              
        
    }
}

