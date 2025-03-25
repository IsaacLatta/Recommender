package com.example.recommender.model.response;

import com.example.recommender.model.entity.Book;
import com.example.recommender.model.entity.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
    private String joinedAt;

    @SerializedName("recommendations")
    private List<Book> recommendedBooks;

    @SerializedName("members")
    private List<User> members;

    // New field for admin IDs. This field should be provided by the backend JSON response.
    @SerializedName("admin_ids")
    private List<Integer> adminIds;

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

    public List<Book> getRecommendedBooks() {
        return recommendedBooks;
    }

    public void setRecommendedBooks(List<Book> books) {
        this.recommendedBooks = books;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public List<Integer> getAdminIds() {
        return adminIds;
    }

    public void setAdminIds(List<Integer> adminIds) {
        this.adminIds = adminIds;
    }

    public boolean isAdmin(int userId) {
        if (adminIds != null && !adminIds.isEmpty()) {
            return adminIds.contains(userId);
        }
        // Fallback: assume the creator is the sole admin
        return userId == createdBy;
    }
}
