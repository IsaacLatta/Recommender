package com.example.recommender.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BookResponse {
    @SerializedName("totalItems")
    private int totalItems;

    @SerializedName("items")
    private List<com.example.recommender.model.Book> items;

    public int getTotalItems() {
        return totalItems;
    }
    public List<Book> getItems() {
        return items;
    }
}
