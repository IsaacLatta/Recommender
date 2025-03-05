package com.example.recommender.model;

public class LoginResponse {
    private boolean success;
    private String user_id;
    private String username;

    public boolean isSuccess() {
        return success;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }
}
