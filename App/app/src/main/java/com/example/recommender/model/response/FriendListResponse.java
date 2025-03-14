package com.example.recommender.model.response;

import com.example.recommender.model.entity.User;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FriendListResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("friends")
    private List<User> friends;

    public boolean isSuccess() {
        return success;
    }
    public List<User> getFriends() {
        return friends;
    }
}
