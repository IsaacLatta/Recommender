package com.example.recommender.model.response;

import com.example.recommender.model.entity.User;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FriendRequestsResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("requests")
    private List<User> requests;

    public boolean isSuccess() {
        return success;
    }
    public List<User> getRequests() {
        return requests;
    }
}
