package com.example.recommender;

import android.util.Log;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.recommender.model.Store;
import com.example.recommender.network.API;
import com.example.recommender.network.AuthService;
import com.example.recommender.network.BookService;

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

        String username = "alice", password = "password123";
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

        CountDownLatch latch =  new CountDownLatch(1);

        String search_query = "harry potter";
        Store.getInstance().setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxLCJleHAiOjE3NDE3NjI0NTV9.mCuRj4Wzat_lrCy6oC0EPNX0eUV_O5fZwgQYhshnYaA");
        controller.searchBook(search_query);

        latch.await(5, TimeUnit.SECONDS);
    }
}
