package com.example.recommender.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FriendSearchResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("users")
    private List<User> users;

    public boolean isSuccess() {
        return success;
    }
    public List<User> getUsers() {
        return users;
    }
}
