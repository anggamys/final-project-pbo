/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.final_project_pbo;

import java.util.ArrayList;

/**
 *
 * @author c0delb08
 */
public class Final_project_pbo {

    public static void main(String[] args) {
        System.out.println("=== STARTING TESTING OWNER ===");
        testOwnerFullCycle();

        System.out.println("\n=== STARTING TESTING STAFF ===");
        testStaff();

        System.out.println("\n=== STARTING TESTING GROSIR ===");
        testGrosir();

        System.out.println("\n=== STARTING TESTING RETAIL ===");
        testRetailProduct();
    }

    private static void testOwnerFullCycle() {
        Owner owner = new Owner();

        System.out.println("\n=== [1] CREATE: Add Owner User ===");
        String testUsername = "test_owner_" + System.currentTimeMillis();
        String testPassword = "initialPassword123";
        Response<User> addResponse = owner.addUser(testUsername, testPassword);
        System.out.println(addResponse);

        if (!addResponse.isSuccess()) {
            System.err.println("Failed to create test user. Abort test.");
            return;
        }

        User createdUser = addResponse.getData();
        Integer userId = createdUser.getIdUser();

        // --------------------------------------------------------------------

        System.out.println("\n=== [2] READ: Get Owner by ID ===");
        Response<User> getResponse = owner.getUser(userId);
        System.out.println(getResponse);

        System.out.println("\n=== [3] READ: Get All Owner Users ===");
        Response<ArrayList<User>> getAllResponse = owner.getAllUsers();
        System.out.println(getAllResponse);
        if (getAllResponse.isSuccess()) {
            for (User user : getAllResponse.getData()) {
                System.out.println(user);
            }
        }

        // --------------------------------------------------------------------

        System.out.println("\n=== [4] UPDATE: Update Password ===");
        String updatedPassword = "UpdatedPassword456";
        Response<User> updateResponse = owner.updateUser(userId, updatedPassword, updatedPassword);
        System.out.println(updateResponse);

        System.out.println("\n=== [5] LOGIN: Success Case ===");
        Response<User> loginSuccess = owner.loginUser(updatedPassword, updatedPassword);
        System.out.println(loginSuccess);

        System.out.println("\n=== [6] LOGIN: Failed Case (wrong password) ===");
        Response<User> loginFail = owner.loginUser(testUsername, "wrongPassword!");
        System.out.println(loginFail);

        System.out.println("\n=== [7] UPDATE: Password Mismatch Case ===");
        Response<User> mismatchUpdate = owner.updateUser(userId, "newpass", "otherpass");
        System.out.println(mismatchUpdate);

        System.out.println("\n=== [8] DELETE: Delete User ===");
        Response<Boolean> deleteResponse = owner.deleteUser(userId);
        System.out.println(deleteResponse);

        // --------------------------------------------------------------------

        System.out.println("\n=== [9] READ: Get Deleted User (should fail) ===");
        Response<User> getDeleted = owner.getUser(userId);
        System.out.println(getDeleted);

        System.out.println("\n=== [10] DELETE: Delete Non-existent User Again ===");
        Response<Boolean> deleteNonExist = owner.deleteUser(userId);
        System.out.println(deleteNonExist);
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

            System.out.println("--> Get All Staff Users:");
            Response<ArrayList<User>> getAllResponse = staff.getAllUsers();
            System.out.println(getAllResponse);
            if (getAllResponse.isSuccess()) {
                for (User user : getAllResponse.getData()) {
                    System.out.println(user);
                }
            }

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
        System.out.println("\n=== [1] CREATE: Add Grosir Product ===");
        String productName = "test_product_" + System.currentTimeMillis();
        String productCategory = "Category";
        Double productPrice = 100.0;
        Integer productStock = 10;
        Response<Product> addResponse = grosir.addProduct(productName, productCategory, productPrice, productStock);
        System.out.println(addResponse);

        if (!addResponse.isSuccess()) {
            System.err.println("Failed to create test product. Abort test.");
            return;
        }

        Product createdProduct = addResponse.getData();
        Integer productId = createdProduct.getIdProduct();

        // --------------------------------------------------------------------

        System.out.println("\n=== [2] READ: Get Grosir Product by ID ===");
        Response<Product> getResponse = grosir.getProduct(productId);
        System.out.println(getResponse);

        System.out.println("\n=== [3] READ: Get All Grosir Products ===");
        Response<ArrayList<Product>> getAllResponse = grosir.getAllProducts();
        System.out.println(getAllResponse);
        if (getAllResponse.isSuccess()) {
            for (Product product : getAllResponse.getData()) {
                System.out.println(product);
            }
        }

        // --------------------------------------------------------------------

        System.out.println("\n=== [4] UPDATE: Update Grosir Product ===");
        String updatedProductName = "Updated Product";
        String updatedCategory = "Updated Category";
        Double updatedPrice = 150.0;
        Integer updatedStock = 20;
        Response<Product> updateResponse = grosir.updateProduct(productId, updatedProductName, updatedCategory,
                updatedPrice, updatedStock);
        System.out.println(updateResponse);

        // --------------------------------------------------------------------

        System.out.println("\n=== [5] DELETE: Delete Grosir Product ===");
        Response<Boolean> deleteResponse = grosir.deleteProduct(productId);
        System.out.println(deleteResponse);

        // --------------------------------------------------------------------

        System.out.println("\n=== [6] READ: Get Deleted Product (should fail) ===");
        Response<Product> getDeleted = grosir.getProduct(productId);
        System.out.println(getDeleted);

        System.out.println("\n=== [7] DELETE: Delete Non-existent Product Again ===");
        Response<Boolean> deleteNonExist = grosir.deleteProduct(productId);
        System.out.println(deleteNonExist);
    }

    private static void testRetailProduct() {
        RetailProduct retailProduct = new RetailProduct();

        // Hasil id product yang baru ditambahkan digunakan untuk pengujian
        System.out.println("--> Add Retail Product:");
        Response<Product> addResponse = retailProduct.addProduct("Retail Product", "Category", 50.0, 5);
        System.out.println(addResponse);
        if (addResponse.isSuccess()) {
            int newProductId = addResponse.getData().getIdProduct();

            System.out.println("--> Get Retail Product:");
            Response<Product> getResponse = retailProduct.getProduct(newProductId);
            System.out.println(getResponse);

            System.out.println("--> Get All Retail Products:");
            Response<ArrayList<Product>> getAllResponse = retailProduct.getAllProducts();
            System.out.println(getAllResponse);
            if (getAllResponse.isSuccess()) {
                for (Product product : getAllResponse.getData()) {
                    System.out.println(product);
                }
            }

            System.out.println("--> Update Retail Product:");
            Response<Product> updateResponse = retailProduct.updateProduct(newProductId, "Updated Retail Product",
                    "Updated Category", 75.0, 10);
            System.out.println(updateResponse);

            System.out.println("--> Delete Retail Product:");
            Response<Boolean> deleteResponse = retailProduct.deleteProduct(newProductId);
            System.out.println(deleteResponse);
        }
    }
}
