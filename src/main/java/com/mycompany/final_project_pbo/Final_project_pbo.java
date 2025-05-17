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
        System.out.println("=== STARTING TESTING OWNER ===");
        testOwner();

        System.out.println("\n=== STARTING TESTING STAFF ===");
        testStaff();

        System.out.println("\n=== STARTING TESTING GROSIR ===");
        testGrosir();
    }

    private static void testOwner() {
        Owner owner = new Owner();
        
        System.out.println("--> Add Owner User:");
        Response<User> addResponse = owner.addUser("owner", "owner");
        System.out.println(addResponse);
        if (addResponse.isSuccess()) {
            Integer newUserId = addResponse.getData().getIdUser();

            System.out.println("--> Login with correct password:");
            Response<User> loginResponse = owner.loginUser("owner", "owner");
            System.out.println(loginResponse);

            System.out.println("--> Get Owner User:");
            Response<User> getResponse = owner.getUser(newUserId);
            System.out.println(getResponse);

            System.out.println("--> Update Owner User Password:");
            Response<User> updateResponse = owner.updateUser(newUserId, "newownerpassword", "newownerpassword");
            System.out.println(updateResponse);

            System.out.println("--> Delete Owner User:");
            Response<Boolean> deleteResponse = owner.deleteUser(newUserId);
            System.out.println(deleteResponse);
        }
    }

    private static void testStaff() {
        Staff staff = new Staff();

        System.out.println("--> Add Staff User:");
        Response<User> addResponse = staff.addUser("staff", "staff");
        System.out.println(addResponse);
        if (addResponse.isSuccess()) {
            Integer newUserId = addResponse.getData().getIdUser();

            System.out.println("--> Login with correct password:");
            Response<User> loginResponse = staff.loginUser("staff", "staff");
            System.out.println(loginResponse);

            System.out.println("--> Get Staff User:");
            Response<User> getResponse = staff.getUser(newUserId);
            System.out.println(getResponse);

            System.out.println("--> Update Staff User Password:");
            Response<User> updateResponse = staff.updateUser(newUserId, "newstaffpassword", "newstaffpassword");
            System.out.println(updateResponse);

            System.out.println("--> Delete Staff User:");
            Response<Boolean> deleteResponse = staff.deleteUser(newUserId);
            System.out.println(deleteResponse);
        }
    }

    private static void testGrosir() {
        Grosir grosir = new Grosir();

        // Hasil id product yang baru ditambahkan digunakan untuk pengujian
        System.out.println("--> Add Grosir Product:");
        Response<Product> addResponse = grosir.addProduct("Grosir Product", "Category", 100.0, 10);
        System.out.println(addResponse);
        if (addResponse.isSuccess()) {
            int newProductId = addResponse.getData().getIdProduct();

            System.out.println("--> Get Grosir Product:");
            Response<Product> getResponse = grosir.getProduct(newProductId);
            System.out.println(getResponse);

            System.out.println("--> Update Grosir Product:");
            Response<Product> updateResponse = grosir.updateProduct(newProductId, "Updated Product", "Updated Category", 150.0, 20);
            System.out.println(updateResponse);

            System.out.println("--> Delete Grosir Product:");
            Response<Boolean> deleteResponse = grosir.deleteProduct(newProductId);
            System.out.println(deleteResponse);
        }
    }
}
