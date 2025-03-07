package com.example.recommender.network.service;

import com.example.recommender.model.response.BasicResponse;
import com.example.recommender.model.response.FriendListResponse;
import com.example.recommender.model.response.FriendRequestsResponse;
import com.example.recommender.model.request.FriendRemoveRequest;
import com.example.recommender.model.request.FriendRequestActionRequest;
import com.example.recommender.model.response.FriendSearchResponse;
import com.example.recommender.network.api.API;
import com.example.recommender.network.api.FriendsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FriendsService {
    private FriendsApi friendsApi;
    private API api;

    public FriendsService(API api) {
        this.api = api;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api.getEndpoint())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        friendsApi = retrofit.create(FriendsApi.class);
    }

    public interface FriendCallback {
        void onSuccess(BasicResponse response);
        void onFailure(Exception e);
    }

    public interface FriendSearchCallback {
        void onSuccess(FriendSearchResponse response);
        void onFailure(Exception e);
    }

    public interface FriendListCallback {
        void onSuccess(FriendListResponse response);
        void onFailure(Exception e);
    }

    public interface FriendRequestsCallback {
        void onSuccess(FriendRequestsResponse response);
        void onFailure(Exception e);
    }

//    public void sendFriendRequest(String jwtToken, int receiverId, final FriendCallback callback) {
//        String authHeader = "Bearer " + jwtToken;
//        FriendRequestSendRequest request = new FriendRequestSendRequest(receiverId);
//        friendsApi.sendFriendRequest(authHeader, request, api.getKey()).enqueue(new Callback<BasicResponse>() {
//            @Override
//            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    callback.onSuccess(response.body());
//                } else {
//                    callback.onFailure(new Exception("Send friend request failed with code: " + response.code()));
//                }
//            }
//            @Override
//            public void onFailure(Call<BasicResponse> call, Throwable t) {
//                callback.onFailure(new Exception(t));
//            }
//        });
//    }

    public void removeFriend(String jwtToken, int friendId, final FriendCallback callback) {
        String authHeader = "Bearer " + jwtToken;
        FriendRemoveRequest request = new FriendRemoveRequest(friendId);
        friendsApi.removeFriend(authHeader, request, api.getKey()).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Remove friend failed with code: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }

    public void searchFriends(String jwtToken, String query, final FriendSearchCallback callback) {
        String authHeader = "Bearer " + jwtToken;
        friendsApi.searchFriends(authHeader, query, api.getKey()).enqueue(new Callback<FriendSearchResponse>() {
            @Override
            public void onResponse(Call<FriendSearchResponse> call, Response<FriendSearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Search friends failed with code: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<FriendSearchResponse> call, Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }

    public void listFriends(String jwtToken, final FriendListCallback callback) {
        String authHeader = "Bearer " + jwtToken;
        friendsApi.listFriends(authHeader, api.getKey()).enqueue(new Callback<FriendListResponse>() {
            @Override
            public void onResponse(Call<FriendListResponse> call, Response<FriendListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("List friends failed with code: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<FriendListResponse> call, Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }

    public void listFriendRequests(String jwtToken, final FriendRequestsCallback callback) {
        String authHeader = "Bearer " + jwtToken;
        friendsApi.listFriendRequests(authHeader, api.getKey()).enqueue(new Callback<FriendRequestsResponse>() {
            @Override
            public void onResponse(Call<FriendRequestsResponse> call, Response<FriendRequestsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("List friend requests failed with code: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<FriendRequestsResponse> call, Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }

    public void handleFriendRequest(String jwtToken, int senderId, boolean approve, final FriendCallback callback) {
        String authHeader = "Bearer " + jwtToken;
        String action = approve ? "approve" : "deny";
        FriendRequestActionRequest request = new FriendRequestActionRequest(action, senderId);
        friendsApi.handleFriendRequest(authHeader, request, api.getKey()).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Handle friend request failed with code: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }
}
