package com.example.recommender;

import android.util.Log;
import androidx.lifecycle.ViewModel;

import com.example.recommender.model.BookResponse;
import com.example.recommender.model.LoginResponse;
import com.example.recommender.model.Store;
import com.example.recommender.model.User;
import com.example.recommender.network.API;
import com.example.recommender.network.AuthService;
import com.example.recommender.network.BookService;
import com.example.recommender.network.FriendsService;
import com.example.recommender.network.FriendsService.FriendCallback;
import com.example.recommender.network.FriendsService.FriendListCallback;
import com.example.recommender.network.FriendsService.FriendRequestsCallback;
import com.example.recommender.network.FriendsService.FriendSearchCallback;
import com.example.recommender.network.ReadingService;

// ***** ORIGINAL CONTROLLER CLASS *****
public class Controller extends ViewModel {
    private FriendsService friendsService;
    private AuthService authService;
    private BookService bookService;
    private ReadingService readingService;



    public Controller(AuthService authService) {
        this.authService = authService;
    }

    public Controller(BookService bookService) {
        this.bookService = bookService;
    }

    public Controller(FriendsService friendsService) {
        this.friendsService = friendsService;
    }

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    public Controller() {
        // Initialize FriendsService if not already done.
        if (friendsService == null) {
            API api = new API(BuildConfig.API_KEY, BuildConfig.API_STAGE);
            friendsService = new FriendsService(api);
        }
    }

    //    public void sendFriendRequest(User user) {
    //        String token = Store.getInstance().getToken();
    //        if (token == null || token.isEmpty()) {
    //            Log.e("FRIEND", "No JWT token available.");
    //            return;
    //        }
    //        friendsService.sendFriendRequest(token, user.getUserId(), new FriendCallback() {
    //            @Override
    //            public void onSuccess(com.example.recommender.model.BasicResponse response) {
    //                Log.d("FRIEND", "Friend request sent: " + response.getMessage());
    //            }
    //            @Override
    //            public void onFailure(Exception e) {
    //                Log.e("FRIEND", "Send friend request failed", e);
    //            }
    //        });
    //    }

    public void removeFriend(User user) {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("FRIEND", "No JWT token available.");
            return;
        }
        friendsService.removeFriend(token, user.getUserId(), new FriendCallback() {
            @Override
            public void onSuccess(com.example.recommender.model.BasicResponse response) {
                Log.d("FRIEND", "Friend removed: " + response.getMessage());
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("FRIEND", "Remove friend failed", e);
            }
        });
    }

    public void searchFriends(String query) {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("FRIEND_SEARCH", "No JWT token available.");
            return;
        }
        friendsService.searchFriends(token, query, new FriendSearchCallback() {
            @Override
            public void onSuccess(com.example.recommender.model.FriendSearchResponse response) {
                if (response.getUsers() != null) {
                    Store.getInstance().setSearchedUsers(response.getUsers());
                    Log.d("FRIEND_SEARCH", "Found " + Store.getInstance().getSearchedUsers().size() + " users.");
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("FRIEND_SEARCH", "Search friends failed", e);
            }
        });
    }

    public void listFriends() {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("FRIEND_LIST", "No JWT token available.");
            return;
        }
        friendsService.listFriends(token, new FriendListCallback() {
            @Override
            public void onSuccess(com.example.recommender.model.FriendListResponse response) {
                if (response.getFriends() != null) {
                    // UPDATE STORE
                    Store.getInstance().setFriends(response.getFriends());
                    Log.d("FRIEND_LIST", "Total friends: " + Store.getInstance().getFriends().size());
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("FRIEND_LIST", "List friends failed", e);
            }
        });
    }

    public void listFriendRequests() {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("FRIEND_REQ", "No JWT token available.");
            return;
        }
        friendsService.listFriendRequests(token, new FriendRequestsCallback() {
            @Override
            public void onSuccess(com.example.recommender.model.FriendRequestsResponse response) {
                if (response.getRequests() != null) {
                    Store.getInstance().setFriendRequests(response.getRequests());
                    Log.d("FRIEND_REQ", "Pending requests: " + Store.getInstance().getFriendRequests().size());
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("FRIEND_REQ", "List friend requests failed", e);
            }
        });
    }

    public void handleFriendRequest(User sender, boolean approve) {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("FRIEND_HANDLE", "No JWT token available.");
            return;
        }
        friendsService.handleFriendRequest(token, sender.getUserId(), approve, new FriendCallback() {
            @Override
            public void onSuccess(com.example.recommender.model.BasicResponse response) {
                Log.d("FRIEND_HANDLE", "Request handled: " + response.getMessage());
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("FRIEND_HANDLE", "Handle friend request failed", e);
            }
        });
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
                    Store.getInstance().setUserId(response.getUser_id());
                    Store.getInstance().setUsername(response.getUsername());
                    Store.getInstance().setToken(response.getToken());
                    Log.d("LOGIN_SUCCESS", "User logged in: " + Store.getInstance().getUsername() + " " + Store.getInstance().getUserId());
                    Log.d("LOGIN_TOKEN", "Token Received: " + Store.getInstance().getToken());
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


    private void ensureReadingService() {
        if (readingService == null) {
            API api = new API(BuildConfig.API_KEY, BuildConfig.API_STAGE);
            readingService = new ReadingService(api);
        }
    }

    /**
     * Join a reading group.
     */
    public void joinReadingGroup(int groupId) {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("READING_GROUP", "No JWT token available.");
            return;
        }
        ensureReadingService();
        readingService.joinGroup(token, groupId, new ReadingService.ReadingCallback<com.example.recommender.model.BasicResponse>() {
            @Override
            public void onSuccess(com.example.recommender.model.BasicResponse response) {
                Log.d("READING_GROUP_JOIN", "Joined group: " + response.getMessage());
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("READING_GROUP_JOIN", "Join group failed", e);
            }
        });
    }

    /**
     * Recommend a book to the group.
     * We pass a group and book object, but under the hood we just need group_id, external_id.
     */
    public void recommendBookToGroup(int groupId, String externalId) {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("READING_GROUP", "No JWT token available.");
            return;
        }
        ensureReadingService();
        readingService.recommendBook(token, groupId, externalId, new ReadingService.ReadingCallback<com.example.recommender.model.BasicResponse>() {
            @Override
            public void onSuccess(com.example.recommender.model.BasicResponse response) {
                Log.d("READING_GROUP_RECOMMEND", "Recommendation: " + response.getMessage());
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("READING_GROUP_RECOMMEND", "Recommend failed", e);
            }
        });
    }

    /**
     * Approve or deny a book recommendation (admin only).
     */
    public void handleBookRecommendation(int groupId, String externalId, boolean approve) {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("READING_GROUP", "No JWT token available.");
            return;
        }
        ensureReadingService();

        String action = approve ? "approve" : "deny";
        readingService.handleRecommendation(token, groupId, externalId, action, new ReadingService.ReadingCallback<com.example.recommender.model.BasicResponse>() {
            @Override
            public void onSuccess(com.example.recommender.model.BasicResponse response) {
                Log.d("READING_GROUP_RECOMMEND", "Handle rec: " + response.getMessage());
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("READING_GROUP_RECOMMEND", "Handle rec failed", e);
            }
        });
    }

    /**
     * Search for reading groups by name.
     */
    public void searchReadingGroups(String query) {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("READING_GROUP_SEARCH", "No JWT token available.");
            return;
        }
        ensureReadingService();
        readingService.searchGroups(token, query, new ReadingService.ReadingCallback<com.example.recommender.model.SearchGroupsResponse>() {
            @Override
            public void onSuccess(com.example.recommender.model.SearchGroupsResponse response) {
                if (response.getGroups() != null) {
//                    Store.getInstance().setSearchedReadingGroups(response.getGroups());
                    Log.d("READING_GROUP_SEARCH", "Found " + response.getGroups().size() + " groups.");
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("READING_GROUP_SEARCH", "Search groups failed", e);
            }
        });
    }

    /**
     * Promote a member in a group (must be admin in that group).
     */
    public void promoteGroupMember(int groupId, int memberId) {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("READING_GROUP_PROMOTE", "No JWT token available.");
            return;
        }
        ensureReadingService();
        readingService.promoteMember(token, groupId, memberId, new ReadingService.ReadingCallback<com.example.recommender.model.BasicResponse>() {
            @Override
            public void onSuccess(com.example.recommender.model.BasicResponse response) {
                Log.d("READING_GROUP_PROMOTE", "Promoted member: " + response.getMessage());
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("READING_GROUP_PROMOTE", "Promote failed", e);
            }
        });
    }

    /**
     * Create a brand-new reading group.
     * The creator will be admin automatically.
     */
    public void createReadingGroup(String groupName) {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("READING_GROUP_CREATE", "No JWT token available.");
            return;
        }
        ensureReadingService();
        readingService.createGroup(token, groupName, new ReadingService.ReadingCallback<com.example.recommender.model.CreateGroupResponse>() {
            @Override
            public void onSuccess(com.example.recommender.model.CreateGroupResponse response) {
                if (response.isSuccess()) {
                    Log.d("READING_GROUP_CREATE", "Created group " + response.getGroupId() + ": " + response.getMessage());
                } else {
                    Log.e("READING_GROUP_CREATE", "Group not created. " + response.getMessage());
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("READING_GROUP_CREATE", "Create group failed", e);
            }
        });
    }
}
