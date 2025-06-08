/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.repositories;

import com.mycompany.final_project_pbo.models.LogLevel;
import com.mycompany.final_project_pbo.models.StockTransaction;
import com.mycompany.final_project_pbo.models.TransactionType;
import com.mycompany.final_project_pbo.services.LogActivityService;
import com.mycompany.final_project_pbo.utils.CrudRepository;
import com.mycompany.final_project_pbo.utils.DatabaseUtil;
import com.mycompany.final_project_pbo.utils.Response;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author c0delb08
 */
public class StockTransactionRepository implements CrudRepository<StockTransaction> {
    LogActivityService logActivityService = new LogActivityService();
    private static final String MODULE_NAME = "StockTransactionRepository";

    @Override
    public Response<StockTransaction> save(StockTransaction entity, Integer userId) {
        String query = "INSERT INTO stock_transactions (product_id, quantity, transaction_type, reference_note, performed_by) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, entity.getProductId());
            preparedStatement.setInt(2, entity.getQuantity());
            preparedStatement.setString(3, entity.getTransactionType().name());
            preparedStatement.setString(4, entity.getDescription());
            preparedStatement.setInt(5, entity.getUserId() != null ? entity.getUserId() : userId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                var generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                    logActivityService.logAction(userId, "Saved StockTransaction with ID: " + entity.getId(),
                            MODULE_NAME,
                            LogLevel.INFO);
                    return Response.success("Transaction saved successfully", entity);
                } else {
                    logActivityService.logAction(userId, "Failed to retrieve generated key for StockTransaction",
                            MODULE_NAME,
                            LogLevel.ERROR);
                    return Response.failure("Failed to retrieve generated key");
                }
            } else {
                logActivityService.logAction(userId, "Failed to save StockTransaction", MODULE_NAME, LogLevel.ERROR);
                return Response.failure("Failed to save transaction");
            }

        } catch (Exception e) {
            e.printStackTrace();
            logActivityService.logAction(userId, "Error saving StockTransaction: " + e.getMessage(),
                    MODULE_NAME,
                    LogLevel.ERROR);
            return Response.failure("Error saving transaction: " + e.getMessage());
        }
    }

    @Override
    public Response<StockTransaction> update(StockTransaction entity, Integer userId) {
        String query = "UPDATE stock_transactions SET product_id = ?, quantity = ?, transaction_type = ?, "
                + "reference_note = ?, performed_by = ? WHERE transaction_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);

            preparedStatement.setInt(1, entity.getProductId());
            preparedStatement.setInt(2, entity.getQuantity());
            preparedStatement.setString(3, entity.getTransactionType().name());
            preparedStatement.setString(4, entity.getDescription());
            preparedStatement.setInt(5, entity.getUserId() != null ? entity.getUserId() : userId);
            preparedStatement.setInt(6, entity.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                logActivityService.logAction(userId, "Updated StockTransaction with ID: " + entity.getId(),
                        MODULE_NAME,
                        LogLevel.INFO);
                return Response.success("Transaction updated successfully", entity);
            } else {
                logActivityService.logAction(userId, "Failed to update StockTransaction with ID: " + entity.getId(),
                        MODULE_NAME,
                        LogLevel.ERROR);
                return Response.failure("Failed to update transaction");
            }

        } catch (Exception e) {
            e.printStackTrace();
            logActivityService.logAction(userId, "Error updating StockTransaction: " + e.getMessage(),
                    MODULE_NAME,
                    LogLevel.ERROR);
            return Response.failure("Error updating transaction: " + e.getMessage());
        }
    }

    @Override
    public Response<StockTransaction> findById(Integer id, Integer userId) {
        String query = "SELECT * FROM stock_transactions WHERE transaction_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);

            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                StockTransaction transaction = new StockTransaction();

                transaction.setId(resultSet.getInt("transaction_id"));
                transaction.setProductId(resultSet.getInt("product_id"));
                transaction.setQuantity(resultSet.getInt("quantity"));
                transaction.setTransactionType(TransactionType.valueOf(resultSet.getString("transaction_type")));
                transaction.setDescription(resultSet.getString("reference_note"));
                transaction.setUserId(resultSet.getInt("performed_by"));
                transaction.setCreatedAt(resultSet.getTimestamp("transaction_date"));

                logActivityService.logAction(userId, "Found StockTransaction with ID: " + id,
                        MODULE_NAME,
                        LogLevel.INFO);
                return Response.success("Transaction found", transaction);
            } else {
                logActivityService.logAction(userId, "StockTransaction with ID: " + id + " not found",
                        MODULE_NAME,
                        LogLevel.WARNING);
                return Response.failure("Transaction not found");
            }

        } catch (Exception e) {
            e.printStackTrace();
            logActivityService.logAction(userId, "Error finding StockTransaction: " + e.getMessage(),
                    MODULE_NAME,
                    LogLevel.ERROR);
            return Response.failure("Error finding transaction: " + e.getMessage());
        }
    }

    @Override
    public Response<Boolean> deleteById(Integer id, Integer userId) {
        String query = "DELETE FROM stock_transactions WHERE transaction_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                logActivityService.logAction(userId, "Deleted StockTransaction with ID: " + id,
                        MODULE_NAME,
                        LogLevel.INFO);
                return Response.success("Transaction deleted successfully", true);
            } else {
                logActivityService.logAction(userId, "Failed to delete StockTransaction with ID: " + id,
                        MODULE_NAME,
                        LogLevel.ERROR);
                return Response.failure("Failed to delete transaction");
            }

        } catch (Exception e) {
            e.printStackTrace();
            logActivityService.logAction(userId, "Error deleting StockTransaction: " + e.getMessage(),
                    MODULE_NAME,
                    LogLevel.ERROR);
            return Response.failure("Error deleting transaction: " + e.getMessage());
        }
    }

    @Override
    public Response<ArrayList<StockTransaction>> findAll(Integer userId) {
        String query = "SELECT * FROM stock_transactions";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);

            var resultSet = preparedStatement.executeQuery();
            ArrayList<StockTransaction> transactions = new ArrayList<>();
            while (resultSet.next()) {
                StockTransaction transaction = new StockTransaction();
                transaction.setId(resultSet.getInt("transaction_id"));
                transaction.setProductId(resultSet.getInt("product_id"));
                transaction.setQuantity(resultSet.getInt("quantity"));
                transaction.setTransactionType(TransactionType.valueOf(resultSet.getString("transaction_type")));
                transaction.setDescription(resultSet.getString("reference_note"));
                transaction.setUserId(resultSet.getInt("performed_by"));
                transaction.setCreatedAt(resultSet.getTimestamp("transaction_date"));

                transactions.add(transaction);
            }

            logActivityService.logAction(userId, "Found " + transactions.size() + " StockTransactions",
                    MODULE_NAME,
                    LogLevel.INFO);
            return Response.success("Transactions found", transactions);
        } catch (Exception e) {
            e.printStackTrace();
            logActivityService.logAction(userId, "Error finding StockTransactions: " + e.getMessage(),
                    MODULE_NAME,
                    LogLevel.ERROR);
            return Response.failure("Error finding transactions: " + e.getMessage());
        }
    }

}
