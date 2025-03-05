package com.example.recommender;

public class Store {
    private static Store instance;
    private String userId;
    private String username;

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
}
