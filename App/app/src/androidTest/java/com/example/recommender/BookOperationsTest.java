package com.example.recommender;

import android.util.Log;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.recommender.model.entity.Store;
import com.example.recommender.network.api.API;
import com.example.recommender.network.service.AuthService;
import com.example.recommender.network.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class BookOperationsTest {

    private Controller controller;

    @Before
    public void setup() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        controller = new Controller();
        String username = "alice", password = "password123";
        Log.d("LOGIN_TEST", "Checking valid credentials: " + username + ", " + password);

        controller.login(username, password);

        latch.await(5, TimeUnit.SECONDS);
        Log.d("BOOK_OPS", "Token received from login: " + Store.getInstance().getToken());
    }

    @Test
    public void testSaveBook() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Log.d("BOOK_OPS_TEST", "Saving a book with external_id=GB_TEST_SAVE");
        controller.saveOrRateBook("GB_TEST_SAVE", "save", null);

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testRateBook() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Log.d("BOOK_OPS_TEST", "Rating a book with external_id=GB_TEST_RATE, rating=5");
        controller.saveOrRateBook("GB_TEST_RATE", "rate", 5);

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testSaveAndRate() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Log.d("BOOK_OPS_TEST", "Saving & rating a book with external_id=GB_TEST_BOTH, rating=3");
        controller.saveOrRateBook("GB_TEST_BOTH", "save_and_rate", 3);

        latch.await(5, TimeUnit.SECONDS);
    }
}
