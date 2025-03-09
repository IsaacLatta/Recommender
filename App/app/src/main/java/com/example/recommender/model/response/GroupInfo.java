package com.example.recommender.model.response;

import com.google.gson.annotations.SerializedName;

public class GroupInfo {

    @SerializedName("group_id")
    private int groupId;

    @SerializedName("group_name")
    private String groupName;

    @SerializedName("created_by")
    private int createdBy;

    @SerializedName("role")
    private String role;

    @SerializedName("joined_at")
    private String joinedAt; // or use a Date/DateTime type if you prefer parsing

    public int getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public String getRole() {
        return role;
    }

    public String getJoinedAt() {
        return joinedAt;
    }
}
