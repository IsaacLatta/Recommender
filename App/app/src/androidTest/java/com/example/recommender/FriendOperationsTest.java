package com.example.recommender;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.recommender.model.entity.Store;
import com.example.recommender.model.entity.User;
import com.example.recommender.network.api.API;
import com.example.recommender.network.service.AuthService;
import com.example.recommender.network.service.FriendsService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class FriendOperationsTest {

    private Controller controller;

    @Before
    public void setup() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        API api = new API(BuildConfig.API_KEY, BuildConfig.API_STAGE);
        String username = "alice", password = "password123";

        controller = new Controller(new AuthService(api));
        controller.setFriendService(new FriendsService(api));
        Log.d("LOGIN_TEST", "Checking valid credentials: " + username + ", " + password);

        controller.login(username, password);

        latch.await(5, TimeUnit.SECONDS);
        Log.d("BOOK_OPS", "Token received from login: " + Store.getInstance().getToken());
    }

    @Test
    public void testSendFriendRequest() throws InterruptedException {
        User bob = new User(2, "bob");

        CountDownLatch latch = new CountDownLatch(1);
        Log.d("FRIEND_TEST", "Sending friend request to heidi");
        controller.sendFriendRequest(bob);

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testRemoveFriend() throws InterruptedException {
        User bob = new User(2, "bob");

        CountDownLatch latch = new CountDownLatch(1);
        Log.d("FRIEND_TEST", "Removing friend bob");
        controller.removeFriend(bob);

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testSearchFriends() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        String query = "alice";  // Expect to find "charlie" (user_id = 3) among others.
        Log.d("FRIEND_TEST", "Searching for friends with query: " + query);
        controller.searchFriends(query);

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testListFriends() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Log.d("FRIEND_TEST", "Listing friends");
        controller.listFriends();

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testListFriendRequests() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Log.d("FRIEND_TEST", "Listing friend requests");
        controller.listFriendRequests();

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testHandleFriendRequest() throws InterruptedException {
        User frank = new User(6, "frank");

        CountDownLatch latch = new CountDownLatch(1);
        Log.d("FRIEND_TEST", "Approving friend request from frank");
        controller.handleFriendRequest(frank, true);

        latch.await(5, TimeUnit.SECONDS);
    }
}
