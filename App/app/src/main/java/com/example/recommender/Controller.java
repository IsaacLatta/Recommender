package com.example.recommender;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.example.recommender.data.network.AuthService;
import com.example.recommender.data.model.LoginResponse;

public class Controller extends ViewModel {
    private AuthService authService;

    public Controller(AuthService authService) {
        this.authService = authService;
    }

    public void login(String username, String password) {
        authService.login(username, password, new AuthService.AuthCallback() {
            @Override
            public void onSuccess(LoginResponse response) {
                if(response.isSuccess()){
                    String userId = response.getUser_id();
                    String userName = response.getUsername();
                    Store.getInstance().setUserId(userId);
                    Store.getInstance().setUsername(userName);
                    Log.d("LOGIN_SUCCESS", "User logged in: " + userName + " " + userId);
                } else {
                    Log.e("LOGIN_FAILED", "Invalid credentials");
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("LOGIN_ERROR", "Login failed", e);
            }
        });
    }
}
