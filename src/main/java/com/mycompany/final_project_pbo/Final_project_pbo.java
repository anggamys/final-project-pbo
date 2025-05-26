/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.final_project_pbo;

import com.mycompany.final_project_pbo.ui.Login;
import java.util.ArrayList;

/**
 *
 * @author c0delb08
 */
public class Final_project_pbo {

    public static void main(String[] args) {
        launchProgram();
        runAllTests();
    }
    
    private static void launchProgram() {
        Login loginFrame = new Login();
        loginFrame.setVisible(true);
        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null);
    }
    
    private static void runAllTests() {
        System.out.println("=== STARTING TESTING OWNER ===");
        testUserFullCycle();

        System.out.println("\n=== STARTING TESTING GROSIR ===");
        testGrosir();

        System.out.println("\n=== STARTING TESTING RETAIL ===");
        testRetailProduct();
    }
    
    private static void testUserFullCycle() {
        User userService = new User();
        
        System.out.println("\n=== [1] CREATE: Add User ===");
        String testUsername = "test_user_" + System.currentTimeMillis();
        String testPassword = "initialPassword123";
        String testRole = "OWNER";
        
        String testChangeUsername = "test_user_" + System.currentTimeMillis();
        String testChangePassword = "UpdatedPassword456";
        
        Response<User> addResponse = userService.addUser(testUsername, testPassword, testRole);
        System.out.println(addResponse);

        if (!addResponse.isSuccess()) {
            System.err.println("Failed to create test user. Abort test.");
            return;
        }

//        User createdUser = addResponse.getData();
//        Integer userId = createdUser.getIdUser();
//
//        System.out.println("\n=== [2] READ: Get User by ID ===");
//        System.out.println(userService.getUser(userId));
//
//        System.out.println("\n=== [3] READ: Get All Users ===");
//        Response<ArrayList<User>> getAllResponse = userService.getAllUsers("OWNER");
//        System.out.println(getAllResponse);
//        if (getAllResponse.isSuccess()) {
//            for (User user : getAllResponse.getData()) {
//                System.out.println(user);
//            }
//        }
//
//        System.out.println("\n=== [4] UPDATE: Update Password ===");
//        System.out.println(userService.updateUser(userId, testChangeUsername, testChangePassword));
//
//        System.out.println("\n=== [5] LOGIN: Success Case ===");
//        System.out.println(userService.loginUser(testChangeUsername, testChangePassword));
//
//        System.out.println("\n=== [6] LOGIN: Failed Case (wrong password) ===");
//        System.out.println(userService.loginUser(testUsername, "wrongPassword"));
//
//        System.out.println("\n=== [7] UPDATE: Password Mismatch Case ===");
//        System.out.println(userService.updateUser(userId, "newpass1", "newpass2"));
//
//        System.out.println("\n=== [8] DELETE: Delete User ===");
//        System.out.println(userService.deleteUser(userId));
//
//        System.out.println("\n=== [9] READ: Get Deleted User (should fail) ===");
//        System.out.println(userService.getUser(userId));
//
//        System.out.println("\n=== [10] DELETE: Delete Non-existent User Again ===");
//        System.out.println(userService.deleteUser(userId));
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
