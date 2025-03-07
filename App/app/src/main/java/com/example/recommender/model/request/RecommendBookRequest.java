package com.example.recommender.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * RecommendBookRequest
 * Body: {"group_id": 123, "external_id": "GB_SOME_BOOK_ID"}
 */
public class RecommendBookRequest {
    @SerializedName("group_id")
    private int groupId;

    @SerializedName("external_id")
    private String externalId;

    public RecommendBookRequest(int groupId, String externalId) {
        this.groupId = groupId;
        this.externalId = externalId;
    }
    public int getGroupId() {
        return groupId;
    }
    public String getExternalId() {
        return externalId;
    }
}
