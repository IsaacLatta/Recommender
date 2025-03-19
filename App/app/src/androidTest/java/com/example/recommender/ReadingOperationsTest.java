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
        controller = Controller.getInstance();
        controller.login("alice", "password123");
    }

    @Test
    public void testCreateGroup() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Log.d("READING_TEST", "Creating a new reading group: 'MyTestGroup'");

        controller.createReadingGroup("MyTestGroup");

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testJoinGroup() throws InterruptedException {
        int groupId = 3;
        CountDownLatch latch = new CountDownLatch(1);

        Log.d("READING_TEST", "Joining group_id=" + groupId);
        controller.joinReadingGroup(groupId);

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testRecommendBook() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        int groupId = 1;
        String externalId = "GB_NEUROMANCER_ID";

        Log.d("READING_TEST", "Recommending book " + externalId + " to group_id=" + groupId);
        controller.recommendBookToGroup(groupId, externalId);

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testSearchGroups() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        String query = "Fans";

        Log.d("READING_TEST", "Searching for reading groups with query=" + query);
        controller.searchReadingGroups(query);

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testPromoteMember() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        int groupId = 1;
        int memberId = 3;

        Log.d("READING_TEST", "Promoting user_id=" + memberId + " in group_id=" + groupId);
        controller.promoteGroupMember(groupId, memberId);

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testHandleRecommendation() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        int groupId = 1;
        String externalId = "OL_1984_ID";

        Log.d("READING_TEST", "Approving recommendation for " + externalId + " in group_id=" + groupId);
        controller.handleBookRecommendation(groupId, externalId, true /*approve*/);

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testListUserGroups() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Log.d("READING_TEST", "Listing user groups for current user");
        controller.listUserGroups();

        latch.await(5, TimeUnit.SECONDS);
    }
    @Test
    public void testListGroupRecommendations() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        int groupId = 1; // some known group that has recommended books

        Log.d("READING_TEST", "Listing recommended books for group_id=" + groupId);
        controller.listGroupRecommendations(groupId);

        latch.await(5, TimeUnit.SECONDS);
    }
}
