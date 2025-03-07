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
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReadingService {
    private ReadingApi readingApi;
    private API api;

    public ReadingService(API api) {
        this.api = api;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api.getEndpoint())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        readingApi = retrofit.create(ReadingApi.class);
    }

    // Generic callback interface
    public interface ReadingCallback<T> {
        void onSuccess(T response);
        void onFailure(Exception e);
    }

    public void joinGroup(String jwtToken, int groupId, final ReadingCallback<BasicResponse> callback) {
        String authHeader = "Bearer " + jwtToken;
        JoinGroupRequest request = new JoinGroupRequest(groupId);
        readingApi.joinGroup(authHeader, api.getKey(), request).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Join group failed with code: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }

    public void recommendBook(String jwtToken, int groupId, String externalId, final ReadingCallback<BasicResponse> callback) {
        String authHeader = "Bearer " + jwtToken;
        RecommendBookRequest request = new RecommendBookRequest(groupId, externalId);
        readingApi.recommendBook(authHeader, api.getKey(), request).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Recommend book failed with code: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }

    public void handleRecommendation(String jwtToken, int groupId, String externalId, String action, final ReadingCallback<BasicResponse> callback) {
        String authHeader = "Bearer " + jwtToken;
        HandleRecommendationRequest request = new HandleRecommendationRequest(groupId, externalId, action);
        readingApi.handleRecommendation(authHeader, api.getKey(), request).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Handle recommendation failed with code: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }

    public void searchGroups(String jwtToken, String query, final ReadingCallback<SearchGroupsResponse> callback) {
        String authHeader = "Bearer " + jwtToken;
        readingApi.searchGroups(authHeader, api.getKey(), query).enqueue(new Callback<SearchGroupsResponse>() {
            @Override
            public void onResponse(Call<SearchGroupsResponse> call, Response<SearchGroupsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Search groups failed with code: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<SearchGroupsResponse> call, Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }

    public void promoteMember(String jwtToken, int groupId, int memberId, final ReadingCallback<BasicResponse> callback) {
        String authHeader = "Bearer " + jwtToken;
        PromoteMemberRequest request = new PromoteMemberRequest(groupId, memberId);
        readingApi.promoteMember(authHeader, api.getKey(), request).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Promote member failed with code: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }

    public void createGroup(String jwtToken, String groupName, final ReadingCallback<CreateGroupResponse> callback) {
        String authHeader = "Bearer " + jwtToken;
        CreateGroupRequest request = new CreateGroupRequest(groupName);
        readingApi.createGroup(authHeader, api.getKey(), request).enqueue(new Callback<CreateGroupResponse>() {
            @Override
            public void onResponse(Call<CreateGroupResponse> call, Response<CreateGroupResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Create group failed with code: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<CreateGroupResponse> call, Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }
}
