/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.services;

import com.mycompany.final_project_pbo.models.LogLevel;
import com.mycompany.final_project_pbo.models.User;
import com.mycompany.final_project_pbo.repositories.UserRepository;
import com.mycompany.final_project_pbo.utils.Response;

/**
 *
 * @author c0delb08
 */
public class AuthenticationService {
    UserRepository userRepository = new UserRepository();
    LogActivityService logActivityService = new LogActivityService();
    private static final String MODULE_NAME = "AuthenticationService";

    public Response<User> authenticate(String username, String password) {
        if (username == null || password == null) {
            logActivityService.logAction(null, "Authentication failed: Username or password is null", MODULE_NAME, LogLevel.ERROR);
            return Response.failure("Username and password must not be null");
        }

        Response<User> userResponse = userRepository.findByUsername(username, null);
        if (userResponse.isSuccess()) {
            User user = userResponse.getData();
            if (user.getPassword().equals(password)) {
                return Response.success("Authentication successful", user);
            } else {
                logActivityService.logAction(user.getId(), "Authentication failed: Invalid password", MODULE_NAME, LogLevel.ERROR);
                return Response.failure("Invalid password");
            }
        } else {
            logActivityService.logAction(null, "Authentication failed: User not found", MODULE_NAME, LogLevel.WARNING);
            return Response.failure("User not found");
        }
    }
}