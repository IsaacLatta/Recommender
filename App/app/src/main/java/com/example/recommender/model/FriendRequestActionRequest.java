package com.example.recommender.model;

import com.google.gson.annotations.SerializedName;

public class FriendRequestActionRequest {
    @SerializedName("action")
    private String action; // "approve" or "deny"

    @SerializedName("sender_id")
    private int senderId;

    public FriendRequestActionRequest(String action, int senderId) {
        this.action = action;
        this.senderId = senderId;
    }
}
