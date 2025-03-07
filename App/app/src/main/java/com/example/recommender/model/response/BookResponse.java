package com.example.recommender.model.response;

import com.example.recommender.model.entity.Book;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BookResponse {
    @SerializedName("totalItems")
    private int totalItems;

    @SerializedName("items")
    private List<Book> items;

    public int getTotalItems() {
        return totalItems;
    }
    public List<Book> getItems() {
        return items;
    }
}
