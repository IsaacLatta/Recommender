package com.example.recommender.model.entity;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Book {

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
    public String getExternalID() {return external_id;}

    public Integer getRating() {return rating;}

    public Integer getSuggestedBy() {return rating;}

    public String getStatus() {return status;}
}
