package com.example.recommender.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * SearchGroupsResponse
 * Example:
 * {
 *   "success": true,
 *   "groups": [
 *       {"group_id":1,"group_name":"Sci-Fi Fans","created_by":1},
 *       ...
 *   ]
 * }
 */
public class SearchGroupsResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("groups")
    private List<ReadingGroup> groups;

    public boolean isSuccess() {
        return success;
    }

    public List<ReadingGroup> getGroups() {
        return groups;
    }
}
