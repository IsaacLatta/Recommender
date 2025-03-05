// File: BookApi.java
package com.example.recommender.network;

import com.example.recommender.model.BookResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BookApi {
    @GET("search_book") // Ensure this path matches your backend's endpoint
    Call<BookResponse> searchBooks(@Query("q") String query, @Query("key") String apiKey);
}
