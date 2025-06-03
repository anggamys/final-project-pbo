/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.services;

import com.mycompany.final_project_pbo.models.User;
import com.mycompany.final_project_pbo.repositories.UserRepository;
import com.mycompany.final_project_pbo.utils.Response;

/**
 *
 * @author c0delb08
 */
public class AuthenticationService {
    UserRepository userRepository = new UserRepository();

    public Response<User> authenticate(String username, String password) {
        if (username == null || password == null) {
            return Response.failure("Username and password must not be null");
        }

        Response<User> userResponse = userRepository.findByUsername(username);
        if (userResponse.isSuccess()) {
            User user = userResponse.getData();
            if (user.getPassword().equals(password)) {
                return Response.success("Authentication successful", user);
            } else {
                return Response.failure("Invalid password");
            }
        } else {
            return Response.failure("User not found");
        }
    }
}