package com.example.recommender.network.api;

import com.example.recommender.model.response.BasicResponse;
import com.example.recommender.model.response.FriendListResponse;
import com.example.recommender.model.response.FriendRequestsResponse;
import com.example.recommender.model.request.FriendRemoveRequest;
import com.example.recommender.model.request.FriendRequestActionRequest;
import com.example.recommender.model.response.FriendSearchResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FriendsApi {
    @POST("friend/remove")
    Call<BasicResponse> removeFriend(
            @Header("Authorization") String auth,
            @Body FriendRemoveRequest request,
            @Header("x-api-key") String apiKey
    );

    @GET("friend/search")
    Call<FriendSearchResponse> searchFriends(
            @Header("Authorization") String auth,
            @Query("q") String query,
            @Header("x-api-key") String apiKey
    );

    @GET("friend/list")
    Call<FriendListResponse> listFriends(
            @Header("Authorization") String auth,
            @Header("x-api-key") String apiKey
    );

    @GET("friend/requests")
    Call<FriendRequestsResponse> listFriendRequests(
            @Header("Authorization") String auth,
            @Header("x-api-key") String apiKey
    );

//    @POST("friend/request/send")
//    Call<BasicResponse> sendFriendRequest(
//            @Header("Authorization") String auth,
//            @Body FriendRequestSendRequest request,
//            @Header("x-api-key") String apiKey
//    );

    @POST("friend/request")
    Call<BasicResponse> handleFriendRequest(
            @Header("Authorization") String auth,
            @Body FriendRequestActionRequest request,
            @Header("x-api-key") String apiKey
    );
}
