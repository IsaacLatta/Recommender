package com.example.recommender.network;

import com.example.recommender.model.BasicResponse;
import com.example.recommender.model.CreateGroupRequest;
import com.example.recommender.model.CreateGroupResponse;
import com.example.recommender.model.HandleRecommendationRequest;
import com.example.recommender.model.JoinGroupRequest;
import com.example.recommender.model.PromoteMemberRequest;
import com.example.recommender.model.RecommendBookRequest;
import com.example.recommender.model.SearchGroupsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ReadingApi {

    @POST("reading/group/join")
    Call<BasicResponse> joinGroup(
            @Header("Authorization") String auth,
            @Header("x-api-key") String apiKey,
            @Body JoinGroupRequest request
    );

    @POST("reading/group/recommend")
    Call<BasicResponse> recommendBook(
            @Header("Authorization") String auth,
            @Header("x-api-key") String apiKey,
            @Body RecommendBookRequest request
    );

    @POST("reading/group/handle_recommendation")
    Call<BasicResponse> handleRecommendation(
            @Header("Authorization") String auth,
            @Header("x-api-key") String apiKey,
            @Body HandleRecommendationRequest request
    );

    @GET("reading/group/search")
    Call<SearchGroupsResponse> searchGroups(
            @Header("Authorization") String auth,
            @Header("x-api-key") String apiKey,
            @Query("q") String query
    );

    @POST("reading/group/promote")
    Call<BasicResponse> promoteMember(
            @Header("Authorization") String auth,
            @Header("x-api-key") String apiKey,
            @Body PromoteMemberRequest request
    );

    @POST("reading/group/create")
    Call<CreateGroupResponse> createGroup(
            @Header("Authorization") String auth,
            @Header("x-api-key") String apiKey,
            @Body CreateGroupRequest request
    );
}
