package com.example.recommender.network.api;

import com.example.recommender.model.response.BookResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface BookApi {
    @GET("search_book")
    Call<BookResponse> searchBooks(
            @Header("Authorization") String auth,
            @Query("q") String query,
            @Header("x-api-key") String apiKey
    );
}
