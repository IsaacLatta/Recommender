package com.example.recommender.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Book {
    @SerializedName("id")
    private String id;

    @SerializedName("volumeInfo")
    private VolumeInfo volumeInfo;

    public String getId() {
        return id;
    }

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    public static class VolumeInfo {
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
}
