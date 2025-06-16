/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.mycompany.final_project_pbo.models.DebtTransaction;
import com.mycompany.final_project_pbo.models.LoanStatus;
import com.mycompany.final_project_pbo.models.LogLevel;
import com.mycompany.final_project_pbo.repositories.DebtTransactionRepository;
import com.mycompany.final_project_pbo.utils.Response;

/**
 *
 * @author c0delb08
 */
public class DebtTransactionService {

    private final DebtTransactionRepository debtTransactionRepository;
    private final LogActivityService logActivityService;

    private static final String MODULE_NAME = "DebtTransactionService";

    public DebtTransactionService() {
        this.debtTransactionRepository = new DebtTransactionRepository();
        this.logActivityService = new LogActivityService();
    }

    public Response<DebtTransaction> save(DebtTransaction entity, Integer userId) {
        // Hitung tanggal sekarang dan 7 hari kemudian
        LocalDate currentDate = LocalDate.now();
        LocalDate sevenDaysFromNow = currentDate.plusDays(7);

        // Set nilai ke entity
        entity.setLoanDate(currentDate);
        entity.setDueDate(sevenDaysFromNow);
        entity.setStatus(LoanStatus.BELUM_LUNAS);
        entity.setCreatedBy(userId);

        // Simpan transaksi ke repository
        Response<DebtTransaction> response = debtTransactionRepository.save(entity, userId);

        // Logging aktivitas jika perlu
        if (response.isSuccess()) {
            logActivityService.logAction(userId, "Create debt transaction", MODULE_NAME, LogLevel.INFO);
        }

        return response;
    }

    public Response<DebtTransaction> update(DebtTransaction entity, Integer userId) {
        // Update transaksi di repository
        Response<DebtTransaction> response = debtTransactionRepository.update(entity, userId);

        // Logging aktivitas jika perlu
        if (response.isSuccess()) {
            logActivityService.logAction(userId, "Update debt transaction with ID: " + entity.getId(), MODULE_NAME,
                    LogLevel.INFO);
        }

        return response;
    }
}
