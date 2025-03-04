package com.example.recommender;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.recommender.data.network.AuthService;

import okhttp3.OkHttpClient;

import org.json.JSONObject;

public class Controller extends ViewModel {
    private AuthService authService;
    private API bookAPI;
    private API databaseAPI;

    public Controller(API databaseAPI, API bookAPI) {
        this.databaseAPI = databaseAPI;
        this.bookAPI = bookAPI;
        Log.d("DEBUG", "Controller created");
        Log.d("DEBUG", String.format("Database API ENDPOINT: %s, KEY: %s",
                databaseAPI.getEndpoint(), databaseAPI.getKey()));
        Log.d("DEBUG", String.format("Book API ENDPOINT: %s, KEY: %s",
                bookAPI.getEndpoint(), bookAPI.getKey()));

        OkHttpClient client = new OkHttpClient();
        authService = new AuthService(databaseAPI, client);
    }

    public void login(String username, String password) {
        authService.login(username, password, new AuthService.AuthCallback() {
            @Override
            public void onSuccess(String responseData) {
                try {
                    JSONObject jsonResponse = new JSONObject(responseData);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        String user_id = jsonResponse.getString("user_id");
                        String userName = jsonResponse.getString("username");

                        Store.getInstance().setUserId(user_id);
                        Store.getInstance().setUsername(userName);
                        Log.d("LOGIN_SUCCESS", "User logged in: " + userName +" " + user_id);
                    } else {
                        Log.e("LOGIN_FAILED", "Invalid credentials");
                    }
                } catch (Exception e) {
                    Log.e("LOGIN_ERROR", "Error parsing login response", e);
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("LOGIN_ERROR", "Login failed", e);
            }
        });
    }
}
