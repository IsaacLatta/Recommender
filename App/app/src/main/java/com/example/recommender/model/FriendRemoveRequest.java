package com.example.recommender.model;

import com.google.gson.annotations.SerializedName;

public class FriendRemoveRequest {
    @SerializedName("friend_id")
    private int friendId;

    public FriendRemoveRequest(int friendId) {
        this.friendId = friendId;
    }
}
