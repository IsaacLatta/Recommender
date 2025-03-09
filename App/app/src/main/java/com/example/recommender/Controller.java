package com.example.recommender;

import android.util.Log;
import androidx.lifecycle.ViewModel;

import com.example.recommender.model.response.BasicResponse;
import com.example.recommender.model.response.BookResponse;
import com.example.recommender.model.response.GroupListResponse;
import com.example.recommender.model.response.LoginResponse;
import com.example.recommender.model.entity.Store;
import com.example.recommender.model.entity.User;
import com.example.recommender.model.response.CreateGroupResponse;
import com.example.recommender.model.response.FriendListResponse;
import com.example.recommender.model.response.FriendRequestsResponse;
import com.example.recommender.model.response.FriendSearchResponse;
import com.example.recommender.model.response.SearchGroupsResponse;
import com.example.recommender.network.api.API;
import com.example.recommender.network.service.AuthService;
import com.example.recommender.network.service.BookService;
import com.example.recommender.network.service.FriendsService;
import com.example.recommender.network.service.FriendsService.FriendCallback;
import com.example.recommender.network.service.FriendsService.FriendListCallback;
import com.example.recommender.network.service.FriendsService.FriendRequestsCallback;
import com.example.recommender.network.service.FriendsService.FriendSearchCallback;
import com.example.recommender.network.service.ReadingService;

public class Controller extends ViewModel {
    private FriendsService friendService;
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
        this.friendService = friendsService;
    }

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }
    public void setAuthService(AuthService authService) {this.authService = authService;}

    public void setReadingService(ReadingService readingService) {this.readingService = readingService;}
    public void setFriendService(FriendsService friendService) {this.friendService = friendService;}
    public Controller() {
        API api = new API(BuildConfig.API_KEY, BuildConfig.API_STAGE);
        if (friendService == null) {
            friendService = new FriendsService(api);
        }
        if(readingService == null) {
            readingService = new ReadingService(api);
        }
        if(bookService == null) {
            bookService  = new BookService(api);
        }
        if(authService == null) {
            authService = new AuthService(api);
        }
    }
    public void listFriends() {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("FRIEND_LIST", "No JWT token available.");
            return;
        }
        friendService.listFriends(token, new FriendListCallback() {
            @Override
            public void onSuccess(FriendListResponse response) {
                if (response.getFriends() != null) {
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
    public void sendFriendRequest(User user) {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("FRIEND", "No JWT token available.");
            return;
        }
        friendService.sendFriendRequest(token, user.getUserId(), new FriendCallback() {
            @Override
            public void onSuccess(com.example.recommender.model.response.BasicResponse response) {
                Log.d("FRIEND", "Friend request sent: " + response.getMessage());
                listFriends(); // Update the friend list
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("FRIEND", "Send friend request failed", e);
            }
        });
    }

    public void removeFriend(User user) {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("FRIEND", "No JWT token available.");
            return;
        }
        friendService.removeFriend(token, user.getUserId(), new FriendCallback() {
            @Override
            public void onSuccess(BasicResponse response) {
                Log.d("FRIEND", "Friend removed: " + response.getMessage());
                listFriends(); // Update the friend list
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
        friendService.searchFriends(token, query, new FriendSearchCallback() {
            @Override
            public void onSuccess(FriendSearchResponse response) {
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

    public void listFriendRequests() {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("FRIEND_REQ", "No JWT token available.");
            return;
        }
        friendService.listFriendRequests(token, new FriendRequestsCallback() {
            @Override
            public void onSuccess(FriendRequestsResponse response) {
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
        friendService.handleFriendRequest(token, sender.getUserId(), approve, new FriendCallback() {
            @Override
            public void onSuccess(BasicResponse response) {
                Log.d("FRIEND_HANDLE", "Request handled: " + response.getMessage());
                listFriendRequests(); // update the friend requests list
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

    public void saveOrRateBook(String externalId, String action, Integer rating) {
        String token = Store.getInstance().getToken();
        Log.d("BOOK_OPS", "Using token: " + token);
        if (token == null || token.isEmpty()) {
            Log.e("BOOK_OPS", "No JWT token available.");
            return;
        }

        if (bookService == null) {
            API api = new API(BuildConfig.API_KEY, BuildConfig.API_STAGE);
            bookService = new BookService(api);
        }

        bookService.saveOrRateBook(token, externalId, action, rating, new BookService.SaveOrRateCallback() {
            @Override
            public void onSuccess(BasicResponse response) {
                Log.d("BOOK_OPS", "Save/Rate success: " + response.getMessage());
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("BOOK_OPS", "Save/Rate failed", e);
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

                    listFriends();
                    listFriendRequests();
                    listSavedBooks();
                    listUserGroups();
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
    public void joinReadingGroup(int groupId) {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("READING_GROUP", "No JWT token available.");
            return;
        }
//        ensureReadingService();
        readingService.joinGroup(token, groupId, new ReadingService.ReadingCallback<BasicResponse>() {
            @Override
            public void onSuccess(BasicResponse response) {
                Log.d("READING_GROUP_JOIN", "Joined group: " + response.getMessage());
                listUserGroups();
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("READING_GROUP_JOIN", "Join group failed", e);
            }
        });
    }
    public void recommendBookToGroup(int groupId, String externalId) {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("READING_GROUP", "No JWT token available.");
            return;
        }
        ensureReadingService();
        readingService.recommendBook(token, groupId, externalId, new ReadingService.ReadingCallback<BasicResponse>() {
            @Override
            public void onSuccess(BasicResponse response) {
                Log.d("READING_GROUP_RECOMMEND", "Recommendation: " + response.getMessage());
                listGroupRecommendations(groupId);
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("READING_GROUP_RECOMMEND", "Recommend failed", e);
            }
        });
    }
    public void handleBookRecommendation(int groupId, String externalId, boolean approve) {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("READING_GROUP", "No JWT token available.");
            return;
        }
        ensureReadingService();

        String action = approve ? "approve" : "deny";
        readingService.handleRecommendation(token, groupId, externalId, action, new ReadingService.ReadingCallback<BasicResponse>() {
            @Override
            public void onSuccess(BasicResponse response) {
                Log.d("READING_GROUP_RECOMMEND", "Handle rec: " + response.getMessage());
                listGroupRecommendations(groupId);
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("READING_GROUP_RECOMMEND", "Handle rec failed", e);
            }
        });
    }
    public void searchReadingGroups(String query) {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("READING_GROUP_SEARCH", "No JWT token available.");
            return;
        }
        ensureReadingService();
        readingService.searchGroups(token, query, new ReadingService.ReadingCallback<SearchGroupsResponse>() {
            @Override
            public void onSuccess(SearchGroupsResponse response) {
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
    public void promoteGroupMember(int groupId, int memberId) {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("READING_GROUP_PROMOTE", "No JWT token available.");
            return;
        }
        ensureReadingService();
        readingService.promoteMember(token, groupId, memberId, new ReadingService.ReadingCallback<BasicResponse>() {
            @Override
            public void onSuccess(BasicResponse response) {
                Log.d("READING_GROUP_PROMOTE", "Promoted member: " + response.getMessage());
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("READING_GROUP_PROMOTE", "Promote failed", e);
            }
        });
    }
    public void createReadingGroup(String groupName) {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("READING_GROUP_CREATE", "No JWT token available.");
            return;
        }
        ensureReadingService();
        readingService.createGroup(token, groupName, new ReadingService.ReadingCallback<CreateGroupResponse>() {
            @Override
            public void onSuccess(CreateGroupResponse response) {
                if (response.isSuccess()) {
                    Log.d("READING_GROUP_CREATE", "Created group " + response.getGroupId() + ": " + response.getMessage());
                    listUserGroups();
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

    public void listSavedBooks() {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("BOOK_LIST", "No JWT token available.");
            return;
        }
        if (bookService == null) {
            API api = new API(BuildConfig.API_KEY, BuildConfig.API_STAGE);
            bookService = new BookService(api);
        }
        bookService.listUserSavedBooks(token, new BookService.BookCallback() {
            @Override
            public void onSuccess(BookResponse response) {
                Log.d("BOOK_LIST", "Found " + response.getTotalItems() + " saved books.");
                // e.g. Store them somewhere:
//                Store.getInstance().setSavedBooks(response.getItems());
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("BOOK_LIST", "Failed to load saved books", e);
            }
        });
    }

    public void listGroupRecommendations(Integer groupId) {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("GROUP_REC", "No JWT token available.");
            return;
        }
        if (bookService == null) {
            API api = new API(BuildConfig.API_KEY, BuildConfig.API_STAGE);
            bookService = new BookService(api);
        }
        readingService.listGroupRecommendations(token, groupId, new BookService.BookCallback() {
            @Override
            public void onSuccess(BookResponse response) {
                Log.d("GROUP_REC", "Found " + response.getTotalItems() + " recommended books.");
//                Store.getInstance().setRecommendedBooks(response.getItems());
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("GROUP_REC", "Failed to load recommendations", e);
            }
        });
    }
    public void listUserGroups() {
        String token = Store.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            Log.e("GROUP_LIST", "No JWT token available.");
            return;
        }
        ensureReadingService();
        readingService.listUserGroups(token, new ReadingService.ReadingCallback<GroupListResponse>() {
            @Override
            public void onSuccess(GroupListResponse response) {
                if (response != null && response.getGroups() != null) {
                    Log.d("GROUP_LIST", "Found " + response.getGroups().size() + " groups.");
                    // Store.getInstance().setJoinedGroups(response.getGroups());
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("GROUP_LIST", "Failed to list user groups", e);
            }
        });
    }

}
