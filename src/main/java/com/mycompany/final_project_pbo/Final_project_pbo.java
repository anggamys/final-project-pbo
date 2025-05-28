/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.final_project_pbo;

import com.mycompany.final_project_pbo.ui.*;

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
        System.out.println("=== STARTING TESTING USER ===");
        testUserFullCycle();

        System.out.println("=== STARTING TESTING PRODUCT ===");
        testProductCycle();
    }
    
    private static void testUserFullCycle() {
        User userService = new User();
        
        System.out.println("\n=== [1] CREATE: Add User ===");
        String testUsername = "example";
        String testPassword = "example";
        String testRole = "OWNER";
        
        String testChangeUsername = "test_user_" + System.currentTimeMillis();
        String testChangePassword = "UpdatedPassword456";
        
        Response<User> addResponse = userService.addUser(testUsername, testPassword, testRole);
        System.out.println(addResponse);

        if (!addResponse.isSuccess()) {
            System.err.println("Failed to create test user. Abort test.");
            return;
        }
        
//        test commit

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

    private static void testProductCycle() {
        System.out.println("\n=== [1] CREATE: Add Product ===");
        Product productService = new Product();
        productService.setName("Test Product");
        productService.setCategory("Testing");
        productService.setPrice(99.99);
        productService.setStock(10);

        Response<Product> saveResponse = productService.save();
        System.out.println(saveResponse);

        if (!saveResponse.isSuccess()) {
            System.err.println("Failed to create test product. Abort test.");
            return;
        }

//        Product createdProduct = saveResponse.getData();
//        int productId = createdProduct.getIdProduct();
//
//        System.out.println("\n=== [2] READ: Get Product by ID ===");
//        Response<Product> readResponse;
//        readResponse = productService.findById(productId);
//        System.out.println(readResponse);
//
//        System.out.println("\n=== [3] READ: Get All Products ===");
//        Response<ArrayList<Product>> allResponse = productService.findAll();
//        if (allResponse.isSuccess()) {
//            for (Product p : allResponse.getData()) {
//                System.out.println(p);
//            }
//        } else {
//            System.err.println("Failed to fetch product list.");
//        }
//
//        System.out.println("\n=== [4] UPDATE: Update Product Info ===");
//        createdProduct.setName("Updated Product Name");
//        createdProduct.setPrice(129.99);
//        createdProduct.setStock(25);
//        Response<Product> updateResponse = createdProduct.update();
//        System.out.println(updateResponse);
//
//        System.out.println("\n=== [5] DELETE: Delete Product by ID ===");
//        Response<Boolean> deleteResponse = productService.deleteById(productId);
//        System.out.println(deleteResponse);
//
//        System.out.println("\n=== [6] READ: Try Get Deleted Product ===");
//        Response<Product> readAfterDelete = productService.findById(productId);
//        System.out.println(readAfterDelete);
    }
}
