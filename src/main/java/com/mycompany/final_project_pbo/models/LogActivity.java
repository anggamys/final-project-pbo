/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.models;

/**
 *
 * @author c0delb08
 */
public class LogActivity extends BaseEntity {
    private Integer userId;
    private String action;
    private String module;
    private LogLevel logLevel;

    public LogActivity() {
        super();
    }

    public LogActivity(Integer userId, String action, String module, LogLevel logLevel) {
        super();
        this.userId = userId;
        this.action = action;
        this.module = module;
        this.logLevel = logLevel;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    @Override
    public String toString() {
        return "LogActivity{" + "userId=" + userId + ", action=" + action + ", module=" + module + ", logLevel="
                + logLevel + ", timestamp=" + getCreatedAt() + '}';
    }
}
