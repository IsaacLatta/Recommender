package com.example.recommender.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * CreateGroupResponse
 * Example JSON: { "success": true, "message": "Reading group created", "group_id": 123 }
 */
public class CreateGroupResponse extends BasicResponse {
    @SerializedName("group_id")
    private int groupId;

    public int getGroupId() {
        return groupId;
    }
}
