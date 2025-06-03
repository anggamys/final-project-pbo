/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.utils;

import io.github.cdimascio.dotenv.Dotenv;

/**
 *
 * @author c0delb08
 */
public class EnvConfig {
    private static final Dotenv dotenv = Dotenv.configure()
                                               .directory("./") // atau path lain
                                               .ignoreIfMissing()
                                               .load();

    public static String get(String key) {
        return dotenv.get(key);
    }
}
