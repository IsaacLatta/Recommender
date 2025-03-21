package com.example.recommender.model.response;

import com.example.recommender.model.entity.User;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GroupMembersResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("members")
    private List<User> members;

    public boolean isSuccess() {
        return success;
    }

    public List<User> getMembers() {
        return members;
    }
}
