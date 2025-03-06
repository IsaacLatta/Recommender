package com.example.recommender.model;

import java.util.List;

public class Store {
    private static Store instance;
    private String userId;
    private String username;
    private String token;
    private List<Book> searchedBooks;

    private Store() {
        userId = "";
    }

    public static synchronized Store getInstance() {
        if (instance == null) {
            instance = new Store();
        }
        return instance;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}

    public void setSearchedBooks(List<Book> books) {this.searchedBooks = books;}
    public List<Book> getSearchedBooks() {return searchedBooks;}
}
