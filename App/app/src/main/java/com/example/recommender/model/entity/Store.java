package com.example.recommender.model.entity;

import com.example.recommender.model.response.GroupInfo;

import java.util.ArrayList;
import java.util.List;

public class Store {
    private static Store instance;
    private String userId;
    private String username;
    private String token;

    private List<Book> savedBooks;
    private List<Book> searchedBooks;
    private List<User> searchedUsers;
    private List<User> friends;
    private List<User> friendRequests;
    private List<GroupInfo> joinedGroups;

    private List<StoreListener> listeners;
    private List<ReadingGroup> searchedReadingGroups;
    private Store() {
        userId = "";
        listeners = new ArrayList<>();
    }

    public void addListener(StoreListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(StoreListener listener) {
        listeners.remove(listener);
    }

    public void notifyListeners() {
        for (StoreListener listener : listeners) {
            listener.onStoreUpdated();
        }
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

    public void setSavedBooks(List<Book> books) {this.savedBooks = books;}
    public void setSearchedReadingGroups(List<ReadingGroup> groups) {this.searchedReadingGroups = groups;}
    public List<ReadingGroup> getSearchedReadingGroups() {return searchedReadingGroups;}
    public List<Book> getSavedBooks() {return savedBooks;}
    public List<Book> getSearchedBooks() {return searchedBooks;}
    public List<User> getSearchedUsers() {return searchedUsers;}
    public void setSearchedUsers(List<User> users) {this.searchedUsers = users;}
    public List<User> getFriends() {return friends;}
    public void setFriends(List<User> friends) {this.friends = friends;}
    public List<User> getFriendRequests() {return friendRequests;}
    public void setFriendRequests(List<User> friend_requests) {this.friendRequests = friend_requests;}

    public void setJoinedGroups(List<GroupInfo> groups) {this.joinedGroups = groups;}

    public List<GroupInfo> getJoinedGroups() {return joinedGroups;}

    public void setRecommendedBooks(List<Book> books, int groupId) {
        for (GroupInfo group: joinedGroups) {
            if(group.getGroupId() == groupId) {
                group.setRecommendedBooks(books);
            }
        }
    }
}

