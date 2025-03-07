package com.example.recommender.network.api;

import com.example.recommender.model.response.BasicResponse;
import com.example.recommender.model.request.CreateGroupRequest;
import com.example.recommender.model.response.CreateGroupResponse;
import com.example.recommender.model.request.HandleRecommendationRequest;
import com.example.recommender.model.request.JoinGroupRequest;
import com.example.recommender.model.request.PromoteMemberRequest;
import com.example.recommender.model.request.RecommendBookRequest;
import com.example.recommender.model.response.SearchGroupsResponse;

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
