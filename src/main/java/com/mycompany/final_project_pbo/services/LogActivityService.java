/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.services;

import com.mycompany.final_project_pbo.models.LogActivity;
import com.mycompany.final_project_pbo.models.LogLevel;
import com.mycompany.final_project_pbo.repositories.LogActivityRepository;

/**
 *
 * @author c0delb08
 */
public class LogActivityService {
    LogActivityRepository logActivityRepository = new LogActivityRepository();

    public void logAction(Integer userId, String action, String module, LogLevel logLevel) {
        var logActivity = new LogActivity();
        logActivity.setUserId(userId);
        logActivity.setAction(action);
        logActivity.setModule(module);
        logActivity.setLogLevel(logLevel);

        var response = logActivityRepository.save(logActivity);
        if (response.isSuccess()) {
            System.out.println("Log activity saved successfully: " + response.getData());
        } else {
            System.err.println("Failed to save log activity: " + response.getMessage());
        }
    }
}
