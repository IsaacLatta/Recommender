package com.example.recommender.network.api;

import com.example.recommender.model.request.BookActionRequest;
import com.example.recommender.model.response.BookResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BookApi {
    @GET("search_book")
    Call<BookResponse> searchBooks(
            @Header("Authorization") String auth,
            @Query("q") String query,
            @Header("x-api-key") String apiKey
    );

    @POST("book")
    Call<com.example.recommender.model.response.BasicResponse> manageBook(
            @Header("Authorization") String auth,
            @Body BookActionRequest request,
            @Header("x-api-key") String apiKey
    );

    @GET("book")
    Call<BookResponse> getUserBooks(
            @Header("Authorization") String auth,
            @Header("x-api-key") String apiKey
    );

}
