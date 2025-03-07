package com.example.recommender;

import android.util.Log;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.recommender.model.entity.Store;
import com.example.recommender.network.api.API;
import com.example.recommender.network.service.AuthService;
import com.example.recommender.network.service.BookService;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class BackendTest {
    @Test
    public void testLogin() throws InterruptedException {
        API api = new API(BuildConfig.API_KEY, BuildConfig.API_STAGE);
        AuthService auth = new AuthService(api);
        Controller controller = new Controller(auth);

        CountDownLatch latch = new CountDownLatch(1);

        String username = "bob", password = "bobpass";
        Log.d("LOGIN_TEST", "Checking valid credentials: " + username + ", " + password);

        controller.login(username, password);

        latch.await(5, TimeUnit.SECONDS);

        password = "password1234";
        Log.d("LOGIN_TEST", "Checking invalid credentials: " + username + ", " + password);
        controller.login(username, password);

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testBookSearch() throws InterruptedException {
        API api = new API(BuildConfig.API_KEY, BuildConfig.API_STAGE);
        BookService bookService = new BookService(api);
        Controller controller = new Controller(bookService);
        Store.getInstance().setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxLCJleHAiOjE3NDE3NjI0NTV9.mCuRj4Wzat_lrCy6oC0EPNX0eUV_O5fZwgQYhshnYaA");

        CountDownLatch latch =  new CountDownLatch(1);

        String search_query = "harry potter";
        controller.searchBook(search_query);

        search_query ="Interesting books";
        controller.searchBook(search_query);

        search_query ="k,jabfjahsd";
        controller.searchBook(search_query);

        latch.await(5, TimeUnit.SECONDS);
    }
}
