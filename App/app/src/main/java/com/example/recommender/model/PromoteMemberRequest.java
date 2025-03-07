package com.example.recommender.model;

import com.google.gson.annotations.SerializedName;

/**
 * PromoteMemberRequest
 * Body: {"group_id": 123, "member_id": 456}
 */
public class PromoteMemberRequest {
    @SerializedName("group_id")
    private int groupId;

    @SerializedName("member_id")
    private int memberId;

    public PromoteMemberRequest(int groupId, int memberId) {
        this.groupId = groupId;
        this.memberId = memberId;
    }
}
