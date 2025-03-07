package com.example.recommender.model;

import com.google.gson.annotations.SerializedName;

/**
 * HandleRecommendationRequest
 * Body: {"group_id": 123, "external_id": "GB_BOOK", "action": "approve"|"deny"}
 */
public class HandleRecommendationRequest {
    @SerializedName("group_id")
    private int groupId;

    @SerializedName("external_id")
    private String externalId;

    @SerializedName("action")
    private String action;

    public HandleRecommendationRequest(int groupId, String externalId, String action) {
        this.groupId = groupId;
        this.externalId = externalId;
        this.action = action;
    }
}
