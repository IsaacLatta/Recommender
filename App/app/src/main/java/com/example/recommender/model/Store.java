package com.example.recommender.model;

import java.util.List;

public class Store {
    private static Store instance;
    private String userId;
    private String username;
    private String token;
    private List<Book> searchedBooks;
    private List<User> searchedUsers;
    private List<User> friends;
    private List<User> friendRequests;

    private Store() {
        userId = "";
    }

    public static synchronized Store getInstance() {
        if (instance == null) {
            instance = new Store();
        }
        return instance;
    }

    public String getUserId() {return userId;}
    public void setUserId(String userId) {this.userId = userId;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}
    public void setSearchedBooks(List<Book> books) {this.searchedBooks = books;}
    public List<Book> getSearchedBooks() {return searchedBooks;}
    public List<User> getSearchedUsers() {return searchedUsers;}
    public void setSearchedUsers(List<User> users) {this.searchedUsers = users;}
    public List<User> getFriends() {return friends;}
    public void setFriends(List<User> friends) {this.friends = friends;}
    public List<User> getFriendRequests() {return friendRequests;}
    public void setFriendRequests(List<User> friend_requests) {this.friendRequests = friend_requests;}
}
