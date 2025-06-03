/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.final_project_pbo;

import com.mycompany.final_project_pbo.models.Category;
import com.mycompany.final_project_pbo.models.DebtTransaction;
import com.mycompany.final_project_pbo.models.Product;
import com.mycompany.final_project_pbo.models.User;
import com.mycompany.final_project_pbo.repositories.CategoryRepository;
import com.mycompany.final_project_pbo.repositories.DebtTransactionRepository;
import com.mycompany.final_project_pbo.repositories.ProductRepository;
import com.mycompany.final_project_pbo.repositories.UserRepository;
import com.mycompany.final_project_pbo.services.AuthenticationService;
import com.mycompany.final_project_pbo.ui.Login;
import com.mycompany.final_project_pbo.utils.Response;
import com.mycompany.final_project_pbo.models.LoanStatus;

import java.util.ArrayList;

/**
 *
 * @author c0delb08
 */
public class Final_project_pbo {

    public static void main(String[] args) {
         launchProgram();
        // runTests();
//        seedDatabase();
    }

    private static void launchProgram() {
        Login loginFrame = new Login();
        loginFrame.setVisible(true);
        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null);
    }

    private static void runTests() {
        // testUserEntity();
        // testCategoryEntity();
        // testProductEntity();
        // testDebtTransactionEntity();
    }

    private static void seedDatabase() {
        User user = new User();
        Product product = new Product();
        Category category = new Category();
        DebtTransaction debtTransaction = new DebtTransaction();

        UserRepository userRepository = new UserRepository();
        CategoryRepository categoryRepository = new CategoryRepository();
        ProductRepository productRepository = new ProductRepository();
        DebtTransactionRepository debtTransactionRepository = new DebtTransactionRepository();

        // Create a user
        user.setUsername("admin");
        user.setPassword("admin123");
        user.setEmail("admin@example.com");
        user.setIsOwner(true);

        Response<User> userResponse = userRepository.save(user);
        if (userResponse.isSuccess()) {
            System.out.println("User created: " + userResponse.getData());
        } else {
            System.out.println("Failed to create user: " + userResponse.getMessage());
            return; // Exit if user creation fails
        }

        // Create a user with a different role
        User user2 = new User();
        user2.setUsername("user");
        user2.setPassword("user123");
        user2.setEmail("user@example.com");
        user2.setIsOwner(false);

        Response<User> user2Response = userRepository.save(user2);
        if (user2Response.isSuccess()) {
            System.out.println("User created: " + user2Response.getData());
        } else {
            System.out.println("Failed to create user: " + user2Response.getMessage());
            return; // Exit if user creation fails
        }

        // Create a category
        category.setName("Example Category");
        category.setDescription("This is an example category.");

        Response<Category> categoryResponse = categoryRepository.save(category);
        if (categoryResponse.isSuccess()) {
            System.out.println("Category created: " + categoryResponse.getData());
        } else {
            System.out.println("Failed to create category: " + categoryResponse.getMessage());
            return; // Exit if category creation fails
        }

        // Create a product
        product.setName("Example Product");
        product.setBarcode("1234567890123");
        product.setCategoryId(categoryResponse.getData().getId());
        product.setPrice(100.0);
        product.setStock(50);

        Response<Product> productResponse = productRepository.save(product);
        if (productResponse.isSuccess()) {
            System.out.println("Product created: " + productResponse.getData());
        } else {
            System.out.println("Failed to create product: " + productResponse.getMessage());
            return; // Exit if product creation fails
        }

        // Create a debt transaction
        debtTransaction.setDebtorName("John Doe");
        debtTransaction.setAddress("123 Main St");
        debtTransaction.setPhoneNumber("555-1234");
        debtTransaction.setLoanDate("2023-10-01");
        debtTransaction.setDueDate("2023-11-01");
        debtTransaction.setAmount(1000.0);
        debtTransaction.setStatus(LoanStatus.BELUM_LUNAS);
        debtTransaction.setCreatedBy(userResponse.getData().getId());

        Response<DebtTransaction> debtTransactionResponse = debtTransactionRepository.save(debtTransaction);
        if (debtTransactionResponse.isSuccess()) {
            System.out.println("Debt transaction created: " + debtTransactionResponse.getData());
        } else {
            System.out.println("Failed to create debt transaction: " + debtTransactionResponse.getMessage());
            return; // Exit if debt transaction creation fails
        }
    }

    private static void testUserEntity() {
        User user = new User();
        UserRepository userRepository = new UserRepository();
        AuthenticationService authService = new AuthenticationService();

        user.setUsername("exampleUser");
        user.setPassword("password123");
        user.setEmail("user@example.com");
        user.setIsOwner(false);

        // Save the user
        Response<User> saveResponse = userRepository.save(user);
        if (saveResponse.isSuccess()) {
            System.out.println("User saved successfully: " + saveResponse.getData());
        } else {
            System.out.println("Failed to save user: " + saveResponse.getMessage());
        }

        // Retrieve the user by ID
        Response<User> findByIdResponse = userRepository.findById(saveResponse.getData().getId());
        if (findByIdResponse.isSuccess()) {
            User foundUser = findByIdResponse.getData();
            System.out.println("User found by ID: " + foundUser);
        } else {
            System.out.println("Failed to find user by ID: " + findByIdResponse.getMessage());
        }

        // Retrieve the user by username
        Response<User> findResponse = userRepository.findByUsername("exampleUser");
        if (findResponse.isSuccess()) {
            User foundUser = findResponse.getData();
            System.out.println("User found: " + foundUser);
        } else {
            System.out.println("Failed to find user: " + findResponse.getMessage());
        }

        // Authenticate the user
        Response<User> authResponse = authService.authenticate("exampleUser", "password123");
        if (authResponse.isSuccess()) {
            User authenticatedUser = authResponse.getData();
            System.out.println("Authentication successful: " + authenticatedUser);
        } else {
            System.out.println("Authentication failed: " + authResponse.getMessage());
        }

        // Update the user
        user.setEmail("newemail@example.com");
        Response<User> updateResponse = userRepository.update(user);
        if (updateResponse.isSuccess()) {
            System.out.println("User updated successfully: " + updateResponse.getData());
        } else {
            System.out.println("Failed to update user: " + updateResponse.getMessage());
        }

        // Retrieve all users
        Response<ArrayList<User>> allUsersResponse = userRepository.findAll();
        if (allUsersResponse.isSuccess()) {
            ArrayList<User> allUsers = allUsersResponse.getData();
            System.out.println("All users:");
            for (User u : allUsers) {
                System.out.println(u);
            }
        } else {
            System.out.println("Failed to retrieve all users: " + allUsersResponse.getMessage());
        }

        // Delete the user
        Response<Boolean> deleteResponse = userRepository.deleteById(saveResponse.getData().getId());
        if (deleteResponse.isSuccess()) {
            System.out.println("User deleted successfully");
        } else {
            System.out.println("Failed to delete user: " + deleteResponse.getMessage());
        }
    }

    private static void testCategoryEntity() {
        Category category = new Category();
        CategoryRepository categoryRepository = new CategoryRepository();

        category.setName("Example Category");
        category.setDescription("This is an example category.");

        // Save the category
        Response<Category> saveResponse = categoryRepository.save(category);
        if (saveResponse.isSuccess()) {
            System.out.println("Category saved successfully: " + saveResponse.getData());
        } else {
            System.out.println("Failed to save category: " + saveResponse.getMessage());
        }

        // Retrieve the category by ID
        Response<Category> findByIdResponse = categoryRepository.findById(saveResponse.getData().getId());
        if (findByIdResponse.isSuccess()) {
            Category foundCategory = findByIdResponse.getData();
            System.out.println("Category found by ID: " + foundCategory);
        } else {
            System.out.println("Failed to find category by ID: " + findByIdResponse.getMessage());
        }

        // Update the category
        category.setDescription("Updated description");
        Response<Category> updateResponse = categoryRepository.update(category);
        if (updateResponse.isSuccess()) {
            System.out.println("Category updated successfully: " + updateResponse.getData());
        } else {
            System.out.println("Failed to update category: " + updateResponse.getMessage());
        }

        // Retrieve all categories
        Response<ArrayList<Category>> allCategoriesResponse = categoryRepository.findAll();
        if (allCategoriesResponse.isSuccess()) {
            ArrayList<Category> allCategories = allCategoriesResponse.getData();
            System.out.println("All categories:");
            for (Category c : allCategories) {
                System.out.println(c);
            }
        } else {
            System.out.println("Failed to retrieve all categories: " + allCategoriesResponse.getMessage());
        }

        // Delete the category
        Response<Boolean> deleteResponse = categoryRepository.deleteById(saveResponse.getData().getId());
        if (deleteResponse.isSuccess()) {
            System.out.println("Category deleted successfully");
        } else {
            System.out.println("Failed to delete category: " + deleteResponse.getMessage());
        }
    }

    private static void testProductEntity() {
        Product product = new Product();
        Category category = new Category();
        ProductRepository productRepository = new ProductRepository();
        CategoryRepository categoryRepository = new CategoryRepository();

        category.setName("Example Category");
        category.setDescription("This is an example category.");

        // Save the category
        Response<Category> saveResponse = categoryRepository.save(category);
        if (saveResponse.isSuccess()) {
            System.out.println("Category saved successfully: " + saveResponse.getData());
        } else {
            System.out.println("Failed to save category: " + saveResponse.getMessage());
        }

        product.setName("Example Product");
        product.setBarcode("1234567890123");
        product.setCategoryId(saveResponse.getData().getId());
        product.setPrice(99.99);
        product.setStock(100);

        // Save the product
        Response<Product> productSaveResponse = productRepository.save(product);
        if (productSaveResponse.isSuccess()) {
            System.out.println("Product saved successfully: " + productSaveResponse.getData());
        } else {
            System.out.println("Failed to save product: " + productSaveResponse.getMessage());
        }

        // Retrieve the product by ID
        Response<Product> productFindByIdResponse = productRepository.findById(productSaveResponse.getData().getId());
        if (productFindByIdResponse.isSuccess()) {
            Product foundProduct = productFindByIdResponse.getData();
            System.out.println("Product found by ID: " + foundProduct);
        } else {
            System.out.println("Failed to find product by ID: " + productFindByIdResponse.getMessage());
        }

        // Update the product
        product.setPrice(89.99);
        Response<Product> productUpdateResponse = productRepository.update(product);
        if (productUpdateResponse.isSuccess()) {
            System.out.println("Product updated successfully: " + productUpdateResponse.getData());
        } else {
            System.out.println("Failed to update product: " + productUpdateResponse.getMessage());
        }

        // Retrieve all products
        Response<ArrayList<Product>> allProductsResponse = productRepository.findAll();
        if (allProductsResponse.isSuccess()) {
            ArrayList<Product> allProducts = allProductsResponse.getData();
            System.out.println("All products:");
            for (Product p : allProducts) {
                System.out.println(p);
            }
        } else {
            System.out.println("Failed to retrieve all products: " + allProductsResponse.getMessage());
        }

        // Delete the product
        Response<Boolean> productDeleteResponse = productRepository.deleteById(productSaveResponse.getData().getId());
        if (productDeleteResponse.isSuccess()) {
            System.out.println("Product deleted successfully");
        } else {
            System.out.println("Failed to delete product: " + productDeleteResponse.getMessage());
        }

        // Delete the category
        Response<Boolean> categoryDeleteResponse = categoryRepository.deleteById(saveResponse.getData().getId());
        if (categoryDeleteResponse.isSuccess()) {
            System.out.println("Category deleted successfully");
        } else {
            System.out.println("Failed to delete category: " + categoryDeleteResponse.getMessage());
        }
    }

    private static void testDebtTransactionEntity() {
        User user = new User();
        DebtTransaction debtTransaction = new DebtTransaction();
        UserRepository userRepository = new UserRepository();
        DebtTransactionRepository debtTransactionRepository = new DebtTransactionRepository();

        // Create a user
        user.setUsername("john_doe");
        user.setPassword("password123");
        user.setEmail("john@example.com");
        user.setIsOwner(false);

        // Save the user
        Response<User> userSaveResponse = userRepository.save(user);
        if (userSaveResponse.isSuccess()) {
            System.out.println("User saved successfully: " + userSaveResponse.getData());
        } else {
            System.out.println("Failed to save user: " + userSaveResponse.getMessage());
            return; // Exit if user creation fails
        }

        // Create a debt transaction
        debtTransaction.setDebtorName("John Doe");
        debtTransaction.setAddress("123 Main St");
        debtTransaction.setPhoneNumber("555-1234");
        debtTransaction.setLoanDate("2023-10-01");
        debtTransaction.setDueDate("2023-11-01");
        debtTransaction.setAmount(1000.0);
        debtTransaction.setStatus(LoanStatus.BELUM_LUNAS);
        debtTransaction.setCreatedBy(userSaveResponse.getData().getId());

        // Save the debt transaction
        Response<DebtTransaction> debtTransactionSaveResponse = debtTransactionRepository.save(debtTransaction);
        if (debtTransactionSaveResponse.isSuccess()) {
            System.out.println("Debt transaction saved successfully: " + debtTransactionSaveResponse.getData());
        } else {
            System.out.println("Failed to save debt transaction: " + debtTransactionSaveResponse.getMessage());
            return; // Exit if debt transaction creation fails
        }

        // Retrieve the debt transaction by ID
        Response<DebtTransaction> debtTransactionFindByIdResponse = debtTransactionRepository
                .findById(debtTransactionSaveResponse.getData().getId());
        if (debtTransactionFindByIdResponse.isSuccess()) {
            DebtTransaction foundTransaction = debtTransactionFindByIdResponse.getData();
            System.out.println("Debt transaction found by ID: " + foundTransaction);
        } else {
            System.out
                    .println("Failed to find debt transaction by ID: " + debtTransactionFindByIdResponse.getMessage());
        }

        // Update the debt transaction
        debtTransaction.setAmount(1200.0);
        debtTransaction.setStatus(LoanStatus.TERLAMBAT);
        
        Response<DebtTransaction> debtTransactionUpdateResponse = debtTransactionRepository.update(debtTransaction);
        if (debtTransactionUpdateResponse.isSuccess()) {
            System.out.println("Debt transaction updated successfully: " + debtTransactionUpdateResponse.getData());
        } else {
            System.out.println("Failed to update debt transaction: " + debtTransactionUpdateResponse.getMessage());
        }

        // Retrieve all debt transactions
        Response<ArrayList<DebtTransaction>> allDebtTransactionsResponse = debtTransactionRepository.findAll();
        if (allDebtTransactionsResponse.isSuccess()) {
            ArrayList<DebtTransaction> allTransactions = allDebtTransactionsResponse.getData();
            System.out.println("All debt transactions:");
            for (DebtTransaction dt : allTransactions) {
                System.out.println(dt);
            }
        } else {
            System.out.println("Failed to retrieve all debt transactions: " + allDebtTransactionsResponse.getMessage());
        }

        // Delete the debt transaction
        Response<Boolean> debtTransactionDeleteResponse = debtTransactionRepository
                .deleteById(debtTransactionSaveResponse.getData().getId());
        if (debtTransactionDeleteResponse.isSuccess()) {
            System.out.println("Debt transaction deleted successfully");
        } else {
            System.out.println("Failed to delete debt transaction: " + debtTransactionDeleteResponse.getMessage());
        }

        // Delete the user
        Response<Boolean> userDeleteResponse = userRepository.deleteById(userSaveResponse.getData().getId());
        if (userDeleteResponse.isSuccess()) {
            System.out.println("User deleted successfully");
        } else {
            System.out.println("Failed to delete user: " + userDeleteResponse.getMessage());
        }
    }
}
