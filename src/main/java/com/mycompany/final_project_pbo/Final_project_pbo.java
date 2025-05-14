/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.final_project_pbo;

/**
 *
 * @author c0delb08
 */
public class Final_project_pbo {

    public static void main(String[] args) {
        Owner owner = new Owner("owner", "password");
        
//        owner.accessDashboard();
        
        String newUsername = "example";
        String newPassword = "example";
        
        owner.addUser(newUsername, newPassword);
        owner.getUser(newUsername);
    }
}
