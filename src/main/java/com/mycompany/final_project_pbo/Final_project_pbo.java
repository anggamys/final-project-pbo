/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.final_project_pbo;

import com.mycompany.final_project_pbo.models.Category;
import com.mycompany.final_project_pbo.models.DebtTransaction;
import com.mycompany.final_project_pbo.models.Product;
import com.mycompany.final_project_pbo.models.StockTransaction;
import com.mycompany.final_project_pbo.models.TransactionType;
import com.mycompany.final_project_pbo.models.User;
import com.mycompany.final_project_pbo.models.LoanStatus;
import com.mycompany.final_project_pbo.repositories.CategoryRepository;
import com.mycompany.final_project_pbo.repositories.DebtTransactionRepository;
import com.mycompany.final_project_pbo.repositories.ProductRepository;
import com.mycompany.final_project_pbo.repositories.StockTransactionRepository;
import com.mycompany.final_project_pbo.repositories.UserRepository;
import com.mycompany.final_project_pbo.services.AuthenticationService;
import com.mycompany.final_project_pbo.services.DebtTransactionService;
import com.mycompany.final_project_pbo.services.StockTransactionService;
import com.mycompany.final_project_pbo.ui.Login;
import com.mycompany.final_project_pbo.utils.Response;

import java.util.ArrayList;

/**
 *
 * @author c0delb08
 */
public class Final_project_pbo {

    public static void main(String[] args) {
        // launchProgram();
        // runTests();
        // seedDatabase();
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
        // testStockTransactionEntity();
    }

    private static void seedDatabase() {
        clearDatabase(); // Clear existing data before seeding

        User user = new User();
        User user2 = new User();
        User user3 = new User();
        Product product = new Product();
        Product product2 = new Product();
        Product product3 = new Product();
        Category category = new Category();
        Category category2 = new Category();
        DebtTransaction debtTransaction = new DebtTransaction();
        DebtTransaction debtTransaction2 = new DebtTransaction();
        DebtTransaction debtTransaction3 = new DebtTransaction();
        StockTransaction stockTransaction = new StockTransaction();
        StockTransaction stockTransaction2 = new StockTransaction();
        StockTransaction stockTransaction3 = new StockTransaction();

        UserRepository userRepository = new UserRepository();
        CategoryRepository categoryRepository = new CategoryRepository();
        ProductRepository productRepository = new ProductRepository();
        DebtTransactionService debtTransactionService = new DebtTransactionService();

        // Create a user
        user.setUsername("Owner");
        user.setPassword("owner123");
        user.setEmail("owner@example.com");
        user.setIsOwner(true);

        Response<User> userResponse = userRepository.save(user, null);
        if (userResponse.isSuccess()) {
            System.out.println("User created: " + userResponse.getData());
        } else {
            System.out.println("Failed to create user: " + userResponse.getMessage());
            return;
        }

        // Create a user with a different role
        user2.setUsername("Staff");
        user2.setPassword("staff123");
        user2.setEmail("staff@example.com");
        user2.setIsOwner(false);

        Response<User> user2Response = userRepository.save(user2, null);
        if (user2Response.isSuccess()) {
            System.out.println("User created: " + user2Response.getData());
        } else {
            System.out.println("Failed to create user: " + user2Response.getMessage());
            return;
        }

        // Create another user
        user3.setUsername("Stafftwo");
        user3.setPassword("stafftwo123");
        user3.setEmail("stafftwo@example.com");
        user3.setIsOwner(false);

        Response<User> user3Response = userRepository.save(user3, null);
        if (user3Response.isSuccess()) {
            System.out.println("User created: " + user3Response.getData());
        } else {
            System.out.println("Failed to create user: " + user3Response.getMessage());
            return;
        }

        // Create a category
        category.setName("Bahan pokok");
        category.setDescription("Kategori untuk bahan makanan dan minuman");

        Response<Category> categoryResponse = categoryRepository.save(category, null);
        if (categoryResponse.isSuccess()) {
            System.out.println("Category created: " + categoryResponse.getData());
        } else {
            System.out.println("Failed to create category: " + categoryResponse.getMessage());
            return;
        }

        // Create another category
        category2.setName("Kecantikan");
        category2.setDescription("Kategori untuk produk kecantikan");
        Response<Category> category2Response = categoryRepository.save(category2, null);
        if (category2Response.isSuccess()) {
            System.out.println("Category created: " + category2Response.getData());
        } else {
            System.out.println("Failed to create category: " + category2Response.getMessage());
            return;
        }

        // Create a product
        product.setName("Beras");
        product.setBarcode("1234567890123");
        product.setCategoryId(categoryResponse.getData().getId());
        product.setPurchasePrice(40000.0);
        product.setSellingPrice(50000.0);
        product.setStock(50);

        Response<Product> productResponse = productRepository.save(product, null);
        if (productResponse.isSuccess()) {
            System.out.println("Product created: " + productResponse.getData());
        } else {
            System.out.println("Failed to create product: " + productResponse.getMessage());
            return;
        }

        // Create another product
        product2.setName("Sabun Mandi");
        product2.setBarcode("9876543210987");
        product2.setCategoryId(category2Response.getData().getId());
        product2.setPurchasePrice(10000.0);
        product2.setSellingPrice(15000.0);
        product2.setStock(100);

        Response<Product> product2Response = productRepository.save(product2, null);
        if (product2Response.isSuccess()) {
            System.out.println("Product created: " + product2Response.getData());
        } else {
            System.out.println("Failed to create product: " + product2Response.getMessage());
            return;
        }

        // Create another product
        product3.setName("Telur");
        product3.setBarcode("1112223334445");
        product3.setCategoryId(categoryResponse.getData().getId());
        product3.setPurchasePrice(20000.0);
        product3.setSellingPrice(25000.0);
        product3.setStock(30);

        Response<Product> product3Response = productRepository.save(product3, null);
        if (product3Response.isSuccess()) {
            System.out.println("Product created: " + product3Response.getData());
        } else {
            System.out.println("Failed to create product: " + product3Response.getMessage());
            return;
        }

        // Create a debt transaction
        debtTransaction.setDebtorName("Agus Santoso");
        debtTransaction.setAddress("Jl. Merdeka No. 10");
        debtTransaction.setPhoneNumber("08123456789");
        debtTransaction.setAmount(1000000.0);
        debtTransaction.setStatus(LoanStatus.BELUM_LUNAS);
        debtTransaction.setCreatedBy(userResponse.getData().getId());

        Response<DebtTransaction> debtTransactionResponse = debtTransactionService.save(debtTransaction, null);
        if (debtTransactionResponse.isSuccess()) {
            System.out.println("Debt transaction created: " + debtTransactionResponse.getData());
        } else {
            System.out.println("Failed to create debt transaction: " + debtTransactionResponse.getMessage());
            return; // Exit if debt transaction creation fails
        }

        // Create another debt transaction
        debtTransaction2.setDebtorName("Budi Setiawan");
        debtTransaction2.setAddress("Jl. Kebangsaan No. 20");
        debtTransaction2.setPhoneNumber("08234567890");
        debtTransaction2.setAmount(2000000.0);
        debtTransaction2.setStatus(LoanStatus.LUNAS);
        debtTransaction2.setCreatedBy(userResponse.getData().getId());

        Response<DebtTransaction> debtTransaction2Response = debtTransactionService.save(debtTransaction2, null);
        if (debtTransaction2Response.isSuccess()) {
            System.out.println("Debt transaction created: " + debtTransaction2Response.getData());
        } else {
            System.out.println("Failed to create debt transaction: " + debtTransaction2Response.getMessage());
            return; // Exit if debt transaction creation fails
        }

        // Create another debt transaction
        debtTransaction3.setDebtorName("Citra Wulandari");
        debtTransaction3.setAddress("Jl. Cinta No. 30");
        debtTransaction3.setPhoneNumber("08345678901");
        debtTransaction3.setAmount(1500000.0);
        debtTransaction3.setStatus(LoanStatus.TERLAMBAT);
        debtTransaction3.setCreatedBy(userResponse.getData().getId());

        Response<DebtTransaction> debtTransaction3Response = debtTransactionService.save(debtTransaction3, null);
        if (debtTransaction3Response.isSuccess()) {
            System.out.println("Debt transaction created: " + debtTransaction3Response.getData());
        } else {
            System.out.println("Failed to create debt transaction: " + debtTransaction3Response.getMessage());
            return; // Exit if debt transaction creation fails
        }
    }

    private static void clearDatabase() {
        UserRepository userRepository = new UserRepository();
        CategoryRepository categoryRepository = new CategoryRepository();
        ProductRepository productRepository = new ProductRepository();
        DebtTransactionRepository debtTransactionRepository = new DebtTransactionRepository();
        StockTransactionRepository stockTransactionRepository = new StockTransactionRepository();

        // Clear all entities
        stockTransactionRepository.deleteAll(null);
        debtTransactionRepository.deleteAll(null);
        productRepository.deleteAll(null);
        categoryRepository.deleteAll(null);
        userRepository.deleteAll(null);

        System.out.println("Database cleared successfully.");
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
        Response<User> saveResponse = userRepository.save(user, null);
        if (saveResponse.isSuccess()) {
            System.out.println("User saved successfully: " + saveResponse.getData());
        } else {
            System.out.println("Failed to save user: " + saveResponse.getMessage());
        }

        // Retrieve the user by ID
        Response<User> findByIdResponse = userRepository.findById(saveResponse.getData().getId(),
                saveResponse.getData().getId());
        if (findByIdResponse.isSuccess()) {
            User foundUser = findByIdResponse.getData();
            System.out.println("User found by ID: " + foundUser);
        } else {
            System.out.println("Failed to find user by ID: " + findByIdResponse.getMessage());
        }

        // Retrieve the user by username
        Response<User> findResponse = userRepository.findByUsername("exampleUser", saveResponse.getData().getId());
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
        Response<User> updateResponse = userRepository.update(user, saveResponse.getData().getId());
        if (updateResponse.isSuccess()) {
            System.out.println("User updated successfully: " + updateResponse.getData());
        } else {
            System.out.println("Failed to update user: " + updateResponse.getMessage());
        }

        // Retrieve all users
        Response<ArrayList<User>> allUsersResponse = userRepository.findAll(saveResponse.getData().getId());
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
        Response<Boolean> deleteResponse = userRepository.deleteById(saveResponse.getData().getId(),
                saveResponse.getData().getId());
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
        Response<Category> saveResponse = categoryRepository.save(category, null);
        if (saveResponse.isSuccess()) {
            System.out.println("Category saved successfully: " + saveResponse.getData());
        } else {
            System.out.println("Failed to save category: " + saveResponse.getMessage());
        }

        // Retrieve the category by ID
        Response<Category> findByIdResponse = categoryRepository.findById(saveResponse.getData().getId(), null);
        if (findByIdResponse.isSuccess()) {
            Category foundCategory = findByIdResponse.getData();
            System.out.println("Category found by ID: " + foundCategory);
        } else {
            System.out.println("Failed to find category by ID: " + findByIdResponse.getMessage());
        }

        // Update the category
        category.setDescription("Updated description");
        Response<Category> updateResponse = categoryRepository.update(category, null);
        if (updateResponse.isSuccess()) {
            System.out.println("Category updated successfully: " + updateResponse.getData());
        } else {
            System.out.println("Failed to update category: " + updateResponse.getMessage());
        }

        // Retrieve all categories
        Response<ArrayList<Category>> allCategoriesResponse = categoryRepository.findAll(null);
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
        Response<Boolean> deleteResponse = categoryRepository.deleteById(saveResponse.getData().getId(), null);
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
        Response<Category> saveResponse = categoryRepository.save(category, null);
        if (saveResponse.isSuccess()) {
            System.out.println("Category saved successfully: " + saveResponse.getData());
        } else {
            System.out.println("Failed to save category: " + saveResponse.getMessage());
        }

        product.setName("Example Product");
        product.setBarcode("1234567890123");
        product.setCategoryId(saveResponse.getData().getId());
        product.setPurchasePrice(50.0);
        product.setSellingPrice(75.0);
        product.setStock(100);

        // Save the product
        Response<Product> productSaveResponse = productRepository.save(product, null);
        if (productSaveResponse.isSuccess()) {
            System.out.println("Product saved successfully: " + productSaveResponse.getData());
        } else {
            System.out.println("Failed to save product: " + productSaveResponse.getMessage());
        }

        // Retrieve the product by ID
        Response<Product> productFindByIdResponse = productRepository.findById(productSaveResponse.getData().getId(),
                null);
        if (productFindByIdResponse.isSuccess()) {
            Product foundProduct = productFindByIdResponse.getData();
            System.out.println("Product found by ID: " + foundProduct);
        } else {
            System.out.println("Failed to find product by ID: " + productFindByIdResponse.getMessage());
        }

        // Update the product
        product.setName("Updated Product Name");
        product.setPurchasePrice(60.0);
        product.setSellingPrice(90.0);
        product.setStock(120);

        Response<Product> productUpdateResponse = productRepository.update(product, null);
        if (productUpdateResponse.isSuccess()) {
            System.out.println("Product updated successfully: " + productUpdateResponse.getData());
        } else {
            System.out.println("Failed to update product: " + productUpdateResponse.getMessage());
        }

        // Retrieve all products
        Response<ArrayList<Product>> allProductsResponse = productRepository.findAll(null);
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
        Response<Boolean> productDeleteResponse = productRepository.deleteById(productSaveResponse.getData().getId(),
                null);
        if (productDeleteResponse.isSuccess()) {
            System.out.println("Product deleted successfully");
        } else {
            System.out.println("Failed to delete product: " + productDeleteResponse.getMessage());
        }

        // Delete the category
        Response<Boolean> categoryDeleteResponse = categoryRepository.deleteById(saveResponse.getData().getId(), null);
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
        Response<User> userSaveResponse = userRepository.save(user, null);
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
        debtTransaction.setAmount(1000.0);
        debtTransaction.setStatus(LoanStatus.BELUM_LUNAS);
        debtTransaction.setCreatedBy(userSaveResponse.getData().getId());

        // Save the debt transaction
        Response<DebtTransaction> debtTransactionSaveResponse = debtTransactionRepository.save(debtTransaction,
                userSaveResponse.getData().getId());
        if (debtTransactionSaveResponse.isSuccess()) {
            System.out.println("Debt transaction saved successfully: " + debtTransactionSaveResponse.getData());
        } else {
            System.out.println("Failed to save debt transaction: " + debtTransactionSaveResponse.getMessage());
            return; // Exit if debt transaction creation fails
        }

        // Retrieve the debt transaction by ID
        Response<DebtTransaction> debtTransactionFindByIdResponse = debtTransactionRepository
                .findById(debtTransactionSaveResponse.getData().getId(), userSaveResponse.getData().getId());
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

        Response<DebtTransaction> debtTransactionUpdateResponse = debtTransactionRepository.update(debtTransaction,
                userSaveResponse.getData().getId());
        if (debtTransactionUpdateResponse.isSuccess()) {
            System.out.println("Debt transaction updated successfully: " + debtTransactionUpdateResponse.getData());
        } else {
            System.out.println("Failed to update debt transaction: " + debtTransactionUpdateResponse.getMessage());
        }

        // Retrieve all debt transactions
        Response<ArrayList<DebtTransaction>> allDebtTransactionsResponse = debtTransactionRepository
                .findAll(userSaveResponse.getData().getId());
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
                .deleteById(debtTransactionSaveResponse.getData().getId(), userSaveResponse.getData().getId());
        if (debtTransactionDeleteResponse.isSuccess()) {
            System.out.println("Debt transaction deleted successfully");
        } else {
            System.out.println("Failed to delete debt transaction: " + debtTransactionDeleteResponse.getMessage());
        }

        // Delete the user
        Response<Boolean> userDeleteResponse = userRepository.deleteById(userSaveResponse.getData().getId(), null);
        if (userDeleteResponse.isSuccess()) {
            System.out.println("User deleted successfully");
        } else {
            System.out.println("Failed to delete user: " + userDeleteResponse.getMessage());
        }
    }

    private static void testStockTransactionEntity() {
        User user = new User();
        Product product = new Product();
        Category category = new Category();
        StockTransaction stockTransaction = new StockTransaction();

        UserRepository userRepository = new UserRepository();
        ProductRepository productRepository = new ProductRepository();
        CategoryRepository categoryRepository = new CategoryRepository();
        StockTransactionService stockTransactionService = new StockTransactionService();
        StockTransactionRepository stockTransactionRepository = new StockTransactionRepository();

        // Create a user
        user.setUsername("stock_user");
        user.setPassword("stock_password");
        user.setEmail("stock_user@example.com");
        user.setIsOwner(false);

        Response<User> userResponse = userRepository.save(user, null);
        if (userResponse.isSuccess()) {
            System.out.println("User created: " + userResponse.getData());
        } else {
            System.out.println("Failed to create user: " + userResponse.getMessage());
            return;
        }

        // Create a category
        category.setName("Example Category");
        category.setDescription("This is an example category for stock transactions.");
        
        Response<Category> categoryResponse = categoryRepository.save(category, null);
        if (categoryResponse.isSuccess()) {
            System.out.println("Category created: " + categoryResponse.getData());
        } else {
            System.out.println("Failed to create category: " + categoryResponse.getMessage());
            return;
        }

        // Create a product
        product.setName("Example Product");
        product.setBarcode("1234567890123");
        product.setCategoryId(categoryResponse.getData().getId());
        product.setPurchasePrice(100.0);
        product.setSellingPrice(150.0);
        product.setStock(50);

        Response<Product> productResponse = productRepository.save(product, null);
        if (productResponse.isSuccess()) {
            System.out.println("Product created: " + productResponse.getData());
        } else {
            System.out.println("Failed to create product: " + productResponse.getMessage());
            return;
        }

        // Create a stock transaction
        stockTransaction.setQuantity(10);
        stockTransaction.setTransactionType(TransactionType.IN);
        stockTransaction.setDescription("Initial stock addition");
        stockTransaction.setUserId(userResponse.getData().getId());

        Response<StockTransaction> stockTransactionResponse = stockTransactionService.createTransaction(stockTransaction, productResponse.getData().getBarcode(), null);
        if (stockTransactionResponse.isSuccess()) {
            System.out.println("Stock transaction created: " + stockTransactionResponse.getData());
        } else {
            System.out.println("Failed to create stock transaction: " + stockTransactionResponse.getMessage());
        }

        // Retrieve the stock transaction by ID
        Response<StockTransaction> stockTransactionFindByIdResponse = stockTransactionRepository
                .findById(stockTransactionResponse.getData().getId(), null);
        if (stockTransactionFindByIdResponse.isSuccess()) {
            StockTransaction foundTransaction = stockTransactionFindByIdResponse.getData();
            System.out.println("Stock transaction found by ID: " + foundTransaction);
        } else {
            System.out.println("Failed to find stock transaction by ID: " + stockTransactionFindByIdResponse.getMessage());
        }

        // Retrieve all stock transactions
        Response<ArrayList<StockTransaction>> allStockTransactionsResponse = stockTransactionRepository
                .findAll(null);
        if (allStockTransactionsResponse.isSuccess()) {
            ArrayList<StockTransaction> allTransactions = allStockTransactionsResponse.getData();
            System.out.println("All stock transactions:");
            for (StockTransaction st : allTransactions) {
                System.out.println(st);
            }
        } else {
            System.out.println("Failed to retrieve all stock transactions: " + allStockTransactionsResponse.getMessage());
        }

        // Delete the stock transaction
        Response<Boolean> stockTransactionDeleteResponse = stockTransactionRepository
                .deleteById(stockTransactionResponse.getData().getId(), null);
        if (stockTransactionDeleteResponse.isSuccess()) {
            System.out.println("Stock transaction deleted successfully");
        } else {
            System.out.println("Failed to delete stock transaction: " + stockTransactionDeleteResponse.getMessage());
        }

        // Delete the product
        Response<Boolean> productDeleteResponse = productRepository.deleteById(productResponse.getData().getId(), null);
        if (productDeleteResponse.isSuccess()) {
            System.out.println("Product deleted successfully");
        } else {
            System.out.println("Failed to delete product: " + productDeleteResponse.getMessage());
        }

        // Delete the category
        Response<Boolean> categoryDeleteResponse = categoryRepository.deleteById(categoryResponse.getData().getId(), null);
        if (categoryDeleteResponse.isSuccess()) {
            System.out.println("Category deleted successfully");
        } else {
            System.out.println("Failed to delete category: " + categoryDeleteResponse.getMessage());
        }

        // Delete the user
        Response<Boolean> userDeleteResponse = userRepository.deleteById(userResponse.getData().getId(), null);
        if (userDeleteResponse.isSuccess()) {
            System.out.println("User deleted successfully");
        } else {
            System.out.println("Failed to delete user: " + userDeleteResponse.getMessage());
        }
    }
}
