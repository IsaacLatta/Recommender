package com.example.recommender;

import android.util.Log;

import androidx.lifecycle.ViewModel;

public class Controller extends ViewModel {
    private API bookAPI;
    private API databaseAPI;
    public Controller(API databaseAPI, API bookAPI) {
        this.databaseAPI = databaseAPI;
        this.bookAPI = bookAPI;
        Log.d("DEBUG", "Controller created");
        Log.d("DEBUG", String.format("API ENDPOINT: %s, KEY: %s", databaseAPI.getEndpoint(), databaseAPI.getKey()));
        Log.d("DEBUG", String.format("API ENDPOINT: %s, KEY: %s", bookAPI.getEndpoint(), bookAPI.getKey()));
    }

    public Boolean login(String username, String password) {



        return true;
    }
}
