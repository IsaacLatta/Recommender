package com.example.recommender;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.recommender.model.entity.Store;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Example instrumentation tests for reading-group endpoints
 * (create/join/recommend/promote/search/etc.)
 * via the Controller class.
 */
@RunWith(AndroidJUnit4.class)
public class ReadingOperationsTest {
    private Controller controller;

    @Before
    public void setup() {
        // Sample valid JWT for user "alice" (user_id = 1).
        // Adjust to whichever user you want to test.
        String token = "eyJhbGciOiJI..."; // truncated for example
        Store.getInstance().setToken(token);

        // Set the local "current user" info
        Store.getInstance().setUserId("1");
        Store.getInstance().setUsername("alice");

        // Create a new Controller that can handle reading-group calls
        // (the no-arg constructor in your code will auto-init some services,
        // but if you prefer, you can pass in new ReadingService(...) directly).
        controller = new Controller();
    }

    @Test
    public void testCreateGroup() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Log.d("READING_TEST", "Creating a new reading group: 'MyTestGroup'");

        // We call createReadingGroup, which is async. We'll rely on Logcat to verify success/fail.
        controller.createReadingGroup("MyTestGroup");

        // Wait a few seconds to let the async call finish.
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testJoinGroup() throws InterruptedException {
        // Suppose group_id=2 is "Mystery Lovers" from your DB.
        // We'll try to join as user_id=1 (alice).
        int groupId = 2;
        CountDownLatch latch = new CountDownLatch(1);

        Log.d("READING_TEST", "Joining group_id=" + groupId);
        controller.joinReadingGroup(groupId);

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testRecommendBook() throws InterruptedException {
        // Suppose we want to recommend a Google Books ID "GB_NEUROMANCER_ID" to group_id=1
        CountDownLatch latch = new CountDownLatch(1);
        int groupId = 1;
        String externalId = "GB_NEUROMANCER_ID";

        Log.d("READING_TEST", "Recommending book " + externalId + " to group_id=" + groupId);
        controller.recommendBookToGroup(groupId, externalId);

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testSearchGroups() throws InterruptedException {
        // We'll look for groups matching "Fans" in the name, e.g. "Sci-Fi Fans".
        CountDownLatch latch = new CountDownLatch(1);
        String query = "Fans";

        Log.d("READING_TEST", "Searching for reading groups with query=" + query);
        controller.searchReadingGroups(query);

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testPromoteMember() throws InterruptedException {
        // Suppose user_id=1 (alice) is an admin of group_id=1,
        // and we want to promote user_id=3 (charlie) to admin as well.
        CountDownLatch latch = new CountDownLatch(1);
        int groupId = 1;
        int memberId = 3;

        Log.d("READING_TEST", "Promoting user_id=" + memberId + " in group_id=" + groupId);
        controller.promoteGroupMember(groupId, memberId);

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testHandleRecommendation() throws InterruptedException {
        // Suppose there's a "pending" recommendation in group_id=1 for external_id="OL_1984_ID"
        // and user_id=1 is an admin. We'll approve it.
        CountDownLatch latch = new CountDownLatch(1);
        int groupId = 1;
        String externalId = "OL_1984_ID";

        Log.d("READING_TEST", "Approving recommendation for " + externalId + " in group_id=" + groupId);
        controller.handleBookRecommendation(groupId, externalId, true /*approve*/);

        latch.await(5, TimeUnit.SECONDS);
    }
}
