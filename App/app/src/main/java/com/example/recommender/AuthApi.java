package com.example.recommender.data.network;

import com.example.recommender.data.model.LoginRequest;
import com.example.recommender.data.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request, @Header("x-api-key") String apiKey);
}
