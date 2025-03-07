package com.example.recommender.model.entity;

import com.google.gson.annotations.SerializedName;

public class User {
    public User() {
    }
    public User(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    @SerializedName("user_id")
    private int userId;

    @SerializedName("username")
    private String username;

    public int getUserId() {
        return userId;
    }
    public String getUsername() {
        return username;
    }
}
