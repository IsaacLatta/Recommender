package com.example.recommender.model.request;

import com.google.gson.annotations.SerializedName;

public class BookActionRequest {
    @SerializedName("external_id")
    private String externalId;

    @SerializedName("action")
    private String action;

    @SerializedName("rating")
    private Integer rating;

    public BookActionRequest(String externalId, String action, Integer rating) {
        this.externalId = externalId;
        this.action = action;
        this.rating = rating;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getAction() {
        return action;
    }

    public Integer getRating() {
        return rating;
    }
}
