package com.example.recommender;

/* com.example.recommender.Store holds all the data for the users application */
public class Store {
    private static Store instance;
    private String ID;
    private String username;

    private Store() {}

    public String getUsername() {
        return username;
    }

}
