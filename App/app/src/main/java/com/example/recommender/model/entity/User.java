package com.example.recommender.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }


}
