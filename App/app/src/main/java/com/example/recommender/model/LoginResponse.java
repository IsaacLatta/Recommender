package com.example.recommender.model;

public class LoginResponse {
    private boolean success;
    private String user_id;
    private String username;
    private String token;

    public boolean isSuccess() {
        return success;
    }

    public String getToken() {return token;}
    public String getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }
}
