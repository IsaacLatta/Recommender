package com.example.recommender.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * JoinGroupRequest
 * Body: {"group_id": 123}
 */
public class JoinGroupRequest {
    @SerializedName("group_id")
    private int groupId;

    public JoinGroupRequest(int groupId) {
        this.groupId = groupId;
    }
    public int getGroupId() {
        return groupId;
    }
}
