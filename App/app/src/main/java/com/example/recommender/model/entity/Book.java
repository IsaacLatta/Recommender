package com.example.recommender.model.entity;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Book implements Serializable {

    private static final long serialVersionUID = 1L; // recommended for Serializable classes

    @SerializedName("external_id")
    private String external_id;

    @SerializedName("title")
    private String title;

    @SerializedName("authors")
    private List<String> authors;

    @SerializedName("publisher")
    private String publisher;

    @SerializedName("publishedDate")
    private String publishedDate;

    @SerializedName("description")
    private String description;

    @SerializedName("rating")
    private Integer rating;

    @SerializedName("suggested_by")
    private Integer suggestedBy;

    @SerializedName("status")
    private String status;

    @SerializedName("thumbnail")
    private String thumbnail;

    public String getTitle() {
        return title;
    }
    public List<String> getAuthors() {
        return authors;
    }
    public String getPublisher() {
        return publisher;
    }
    public String getPublishedDate() {
        return publishedDate;
    }
    public String getDescription() {
        return description;
    }
    public String getExternalId() {return external_id;}

    public Integer getRating() {return rating;}

    public Integer getSuggestedBy() {return suggestedBy;}

    public String getStatus() {return status;}

    public String getThumbnail() {
        return thumbnail;
    }
}
