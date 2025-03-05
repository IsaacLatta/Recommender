package com.example.recommender.network;

import com.example.recommender.model.LoginRequest;
import com.example.recommender.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request, @Header("x-api-key") String apiKey);
}
