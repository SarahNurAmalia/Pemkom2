/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utill;

import service.AuthService;

/**
 *
 * @author ASUS
 */
public class UserInjector {
    public static void main(String[] args) {
        

        System.out.println("Mulai inject user...");

        AuthService userService = new AuthService();
        userService.registerUser("Administrator", "admin", "123");

        System.out.println("Selesai inject user...");
    }
    
}
