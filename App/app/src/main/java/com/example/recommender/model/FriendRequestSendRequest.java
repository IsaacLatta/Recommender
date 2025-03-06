package com.example.recommender.model;

import com.google.gson.annotations.SerializedName;

public class FriendRequestSendRequest {
    @SerializedName("receiver_id")
    private int receiverId;

    public FriendRequestSendRequest(int receiverId) {
        this.receiverId = receiverId;
    }
}
