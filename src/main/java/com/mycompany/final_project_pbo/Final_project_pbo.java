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
        // System.out.println("=== STARTING TESTING OWNER ===");
        // testOwner();

        // System.out.println("\n=== STARTING TESTING STAFF ===");
        // testStaff();

        System.out.println("\n=== STARTING TESTING GROSIR ===");
        testGrosir();
    }

    private static void testOwner() {
        Owner owner = new Owner("", "");

        System.out.println("--> Add Owner User:");
        System.out.println(owner.addUser("owner", "owner"));

        System.out.println("--> Login with wrong password:");
        System.out.println(owner.loginUser("owner", "wrongpassword"));

        System.out.println("--> Login with correct password:");
        System.out.println(owner.loginUser("owner", "owner"));

        System.out.println("--> Get Owner User:");
        System.out.println(owner.getUser("owner"));

        System.out.println("--> Update Owner User Password:");
        System.out.println(owner.updateUser("owner", "newownerpassword"));

        System.out.println("--> Delete Owner User:");
        System.out.println(owner.deleteUser("owner"));
    }

    private static void testStaff() {
        Staff staff = new Staff("", "");

        System.out.println("--> Add Staff User:");
        System.out.println(staff.addUser("staff", "staff"));

        System.out.println("--> Login with correct password:");
        System.out.println(staff.loginUser("staff", "staff"));

        System.out.println("--> Get Staff User:");
        System.out.println(staff.getUser("staff"));

        System.out.println("--> Update Staff User Password:");
        System.out.println(staff.updateUser("staff", "newstaffpassword"));

        System.out.println("--> Delete Staff User:");
        System.out.println(staff.deleteUser("staff"));
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
