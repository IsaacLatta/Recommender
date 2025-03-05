package com.example.recommender.network;

import com.example.recommender.model.BookResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookService {
    private BookApi bookApi;
    private API api;

    public BookService(API api) {
        this.api = api;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api.getEndpoint())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        bookApi = retrofit.create(BookApi.class);
    }

    public void searchBook(String jwtToken, String query, final BookCallback callback) {
        String authHeader = "Bearer " + jwtToken;
        Call<BookResponse> call = bookApi.searchBooks(authHeader, query, api.getKey());
        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Search failed with code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }

    public interface BookCallback {
        void onSuccess(BookResponse response);
        void onFailure(Exception e);
    }
}
