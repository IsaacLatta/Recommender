package com.example.recommender.model;

import com.google.gson.annotations.SerializedName;

/**
 * ReadingGroup
 * For each group object in "groups" array from searchGroups, etc.
 */
public class ReadingGroup {
    @SerializedName("group_id")
    private int groupId;

    @SerializedName("group_name")
    private String groupName;

    @SerializedName("created_by")
    private int createdBy;

    public int getGroupId() {
        return groupId;
    }
    public String getGroupName() {
        return groupName;
    }
    public int getCreatedBy() {
        return createdBy;
    }
}
