// File: Controller.java
package com.example.recommender;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.example.recommender.model.Book;
import com.example.recommender.model.BookResponse;
import com.example.recommender.model.LoginResponse;
import com.example.recommender.model.Store;
import com.example.recommender.network.API;
import com.example.recommender.network.AuthService;
import com.example.recommender.network.BookService;

import java.util.List;

public class Controller extends ViewModel {
    private AuthService authService;
    private BookService bookService;

    public Controller(AuthService authService) {
        this.authService = authService;
    }

    public Controller(BookService bookService) {
        this.bookService = bookService;
    }

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    public void searchBook(String query) {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("BOOK_SEARCH", "No JWT token available.");
            return;
        }

        if (bookService == null) {
            API bookAPI = new API(BuildConfig.API_KEY, BuildConfig.API_STAGE);
            bookService = new BookService(bookAPI);
        }

        bookService.searchBook(token, query, new BookService.BookCallback() {
            @Override
            public void onSuccess(BookResponse response) {
                Log.d("BOOK_SEARCH", "Found " + response.getTotalItems() + " items.");
                if (response.getItems() != null) {
                    Store.getInstance().setSearchedBooks(response.getItems());
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("BOOK_SEARCH", "Search failed", e);
            }
        });
    }

    public void login(String username, String password) {
        authService.login(username, password, new AuthService.AuthCallback() {
            @Override
            public void onSuccess(LoginResponse response) {
                if(response.isSuccess()){
                    String userId = response.getUser_id();
                    String userName = response.getUsername();
                    String token =  response.getToken();
                    Store.getInstance().setUserId(userId);
                    Store.getInstance().setUsername(userName);
                    Store.getInstance().setToken(token);
                    Log.d("LOGIN_SUCCESS", "User logged in: " + userName + " " + userId);
                    Log.d("LOGIN_TOKEN", "Token Received: " + token);
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
