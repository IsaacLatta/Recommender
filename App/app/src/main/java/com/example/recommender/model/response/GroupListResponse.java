package com.example.recommender.model.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GroupListResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("groups")
    private List<GroupInfo> groups;

    public boolean isSuccess() {
        return success;
    }

    public List<GroupInfo> getGroups() {
        return groups;
    }
}
