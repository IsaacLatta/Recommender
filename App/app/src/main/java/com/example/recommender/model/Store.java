package com.example.recommender.model;

public class Store {
    private static Store instance;
    private String userId;
    private String username;
    private String token;

    private Store() {
        userId = "";
    }

    public static synchronized Store getInstance() {
        if (instance == null) {
            instance = new Store();
        }
        return instance;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}
}
