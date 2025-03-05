package com.example.recommender.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Book {
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
}
