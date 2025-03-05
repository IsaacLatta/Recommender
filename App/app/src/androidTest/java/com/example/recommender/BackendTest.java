package com.example.recommender;

import android.util.Log;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.recommender.network.API;
import com.example.recommender.network.AuthService;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)  // Ensures this runs on a real/emulated Android device
public class BackendTest {
    @Test
    public void testLogin() throws InterruptedException {
        API api = new API(BuildConfig.API_KEY, BuildConfig.API_STAGE);
        AuthService auth = new AuthService(api);
        Controller controller = new Controller(auth);

        CountDownLatch latch = new CountDownLatch(1);  // Ensures we wait for the API response

        String username = "alice", password = "password123";
        Log.d("LOGIN_TEST", "Checking valid credentials: " + username + ", " + password);

        controller.login(username, password);

        latch.await(5, TimeUnit.SECONDS);

        password = "password1234";
        Log.d("LOGIN_TEST", "Checking invalid credentials: " + username + ", " + password);
        controller.login(username, password);

        latch.await(5, TimeUnit.SECONDS);
    }
}
