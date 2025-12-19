package com.hustleborn.service.model.auth;

public class RegisterRequest {
    private String username;
    private String password;
    private String displayName;
    private String email;

    public RegisterRequest() {}

    public RegisterRequest(String username, String password, String displayName, String email) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}