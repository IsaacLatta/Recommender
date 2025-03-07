package com.example.recommender.model;

import com.google.gson.annotations.SerializedName;

/**
 * CreateGroupRequest
 * Body: {"group_name": "..."}
 */
public class CreateGroupRequest {
    @SerializedName("group_name")
    private String groupName;

    public CreateGroupRequest(String groupName) {
        this.groupName = groupName;
    }
    public String getGroupName() {
        return groupName;
    }
}
