package com.example.recommender;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.recommender.model.entity.Store;
import com.example.recommender.model.entity.User;
import com.example.recommender.network.api.API;
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
    public void setup() {
        // Set a valid token for alice (user_id = 1)
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxLCJleHAiOjE3NDE3NjI0NTV9.mCuRj4Wzat_lrCy6oC0EPNX0eUV_O5fZwgQYhshnYaA";
        Store.getInstance().setToken(token);
        // Set current user info for alice
        Store.getInstance().setUserId("1");
        Store.getInstance().setUsername("alice");

        // Initialize Controller (which creates the FriendsService internally)
        API api = new API(BuildConfig.API_KEY, BuildConfig.API_STAGE);
        controller = new Controller(new FriendsService(api));
    }

    @Test
    public void testSendFriendRequest() throws InterruptedException {
        // Send a friend request from alice to heidi (user_id = 8).
        // (Assuming alice is not already connected with heidi.)
        User heidi = new User(8, "heidi");

        CountDownLatch latch = new CountDownLatch(1);
        Log.d("FRIEND_TEST", "Sending friend request to heidi");
        controller.sendFriendRequest(heidi);

        // Wait for asynchronous response (inspect Logcat for "Friend request sent:" output).
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testRemoveFriend() throws InterruptedException {
        // Remove an existing friend from alice's list. For example, bob (user_id = 2) is a friend.
        User bob = new User(2, "bob");

        CountDownLatch latch = new CountDownLatch(1);
        Log.d("FRIEND_TEST", "Removing friend bob");
        controller.removeFriend(bob);

        // Wait for asynchronous response (check Logcat for "Friend removed:" output).
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testSearchFriends() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        String query = "alice";  // Expect to find "charlie" (user_id = 3) among others.
        Log.d("FRIEND_TEST", "Searching for friends with query: " + query);
        controller.searchFriends(query);

        // Wait for asynchronous response (inspect Logcat for "Found X users." message).
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testListFriends() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Log.d("FRIEND_TEST", "Listing friends");
        controller.listFriends();

        // Wait for asynchronous response (check Logcat for "Total friends:" output).
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testListFriendRequests() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Log.d("FRIEND_TEST", "Listing friend requests");
        controller.listFriendRequests();

        // Wait for asynchronous response (inspect Logcat for "Pending requests:" output).
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testHandleFriendRequest() throws InterruptedException {
        // Assume that alice (user 1) has a pending friend request from frank (user_id = 6).
        User frank = new User(6, "frank");

        CountDownLatch latch = new CountDownLatch(1);
        Log.d("FRIEND_TEST", "Approving friend request from frank");
        controller.handleFriendRequest(frank, true);

        // Wait for asynchronous response (check Logcat for "Request handled:" output).
        latch.await(5, TimeUnit.SECONDS);
    }
}
