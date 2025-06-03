/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.repositories;

import com.mycompany.final_project_pbo.models.DebtTransaction;
import com.mycompany.final_project_pbo.models.LoanStatus;
import com.mycompany.final_project_pbo.utils.CrudRepository;
import com.mycompany.final_project_pbo.utils.DatabaseUtil;
import com.mycompany.final_project_pbo.utils.Response;

import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author c0delb08
 */
public class DebtTransactionRepository implements CrudRepository<DebtTransaction> {

    @Override
    public Response<DebtTransaction> save(DebtTransaction entity) {
        String query = "INSERT INTO debt_transactions (debtor_name, address, phone_number, loan_date, due_date, amount, status, created_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, entity.getDebtorName());
            preparedStatement.setString(2, entity.getAddress());
            preparedStatement.setString(3, entity.getPhoneNumber());
            preparedStatement.setString(4, entity.getLoanDate());
            preparedStatement.setString(5, entity.getDueDate());
            preparedStatement.setDouble(6, entity.getAmount());
            preparedStatement.setString(7, entity.getStatus().name()); // ENUM as string
            preparedStatement.setInt(8, entity.getCreatedBy());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                var generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                    return Response.success("Debt transaction saved successfully", entity);
                } else {
                    return Response.failure("Failed to retrieve generated ID");
                }
            } else {
                return Response.failure("Failed to save debt transaction");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure("Error occurred while saving debt transaction");
        }
    }

    @Override
    public Response<DebtTransaction> update(DebtTransaction entity) {
        String query = "UPDATE debt_transactions SET debtor_name = ?, address = ?, phone_number = ?, loan_date = ?, due_date = ?, amount = ?, status = ?, created_by = ? WHERE transaction_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, entity.getDebtorName());
            preparedStatement.setString(2, entity.getAddress());
            preparedStatement.setString(3, entity.getPhoneNumber());
            preparedStatement.setString(4, entity.getLoanDate());
            preparedStatement.setString(5, entity.getDueDate());
            preparedStatement.setDouble(6, entity.getAmount());
            preparedStatement.setString(7, entity.getStatus().name());
            preparedStatement.setInt(8, entity.getCreatedBy()); // optional: change to updatedBy
            preparedStatement.setInt(9, entity.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return Response.success("Debt transaction updated successfully", entity);
            } else {
                return Response.failure("Failed to update debt transaction");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure("Error occurred while updating debt transaction");
        }
    }

    @Override
    public Response<DebtTransaction> findById(Integer id) {
        String query = "SELECT * FROM debt_transactions WHERE transaction_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                DebtTransaction transaction = extractTransaction(resultSet);
                return Response.success("Debt transaction found", transaction);
            } else {
                return Response.failure("Debt transaction not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure("Error occurred while finding debt transaction");
        }
    }

    @Override
    public Response<Boolean> deleteById(Integer id) {
        String query = "DELETE FROM debt_transactions WHERE transaction_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return Response.success("Debt transaction deleted successfully", true);
            } else {
                return Response.failure("Failed to delete debt transaction");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure("Error occurred while deleting debt transaction");
        }
    }

    @Override
    public Response<ArrayList<DebtTransaction>> findAll() {
        String query = "SELECT * FROM debt_transactions";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            var resultSet = preparedStatement.executeQuery();

            ArrayList<DebtTransaction> transactions = new ArrayList<>();
            while (resultSet.next()) {
                transactions.add(extractTransaction(resultSet));
            }
            return Response.success("All debt transactions retrieved successfully", transactions);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure("Error occurred while retrieving all debt transactions");
        }
    }

    private DebtTransaction extractTransaction(java.sql.ResultSet resultSet) throws Exception {
        DebtTransaction transaction = new DebtTransaction();
        transaction.setId(resultSet.getInt("transaction_id"));
        transaction.setDebtorName(resultSet.getString("debtor_name"));
        transaction.setAddress(resultSet.getString("address"));
        transaction.setPhoneNumber(resultSet.getString("phone_number"));
        transaction.setLoanDate(resultSet.getString("loan_date"));
        transaction.setDueDate(resultSet.getString("due_date"));
        transaction.setAmount(resultSet.getDouble("amount"));

        String statusStr = resultSet.getString("status");
        try {
            transaction.setStatus(LoanStatus.valueOf(statusStr.toUpperCase()));
        } catch (IllegalArgumentException | NullPointerException e) {
            transaction.setStatus(LoanStatus.BELUM_LUNAS); // fallback
        }

        transaction.setCreatedBy(resultSet.getInt("created_by"));
        return transaction;
    }
}
