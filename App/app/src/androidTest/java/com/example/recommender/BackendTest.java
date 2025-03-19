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
        Controller controller = Controller.getInstance();

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
        Controller controller = Controller.getInstance();

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
