package com.example.recommender.network.service;

import com.example.recommender.model.request.LoginRequest;
import com.example.recommender.model.response.LoginResponse;
import com.example.recommender.network.api.API;
import com.example.recommender.network.api.AuthApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthService {
    private AuthApi authApi;
    private API api;

    public AuthService(API api) {
        this.api = api;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api.getEndpoint())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        authApi = retrofit.create(AuthApi.class);
    }

    public void login(String username, String password, final AuthCallback callback) {
        LoginRequest request = new LoginRequest(username, password);
        Call<LoginResponse> call = authApi.login(request, api.getKey());
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Login failed with code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }

    public interface AuthCallback {
        void onSuccess(LoginResponse response);
        void onFailure(Exception e);
    }
}
