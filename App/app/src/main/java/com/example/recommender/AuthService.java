package com.example.recommender.data.network;

import android.util.Log;

import com.example.recommender.API;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuthService {
    private OkHttpClient client;
    private API api; // This will be your database API (for auth)

    public AuthService(API api, OkHttpClient client) {
        this.api = api;
        this.client = client;
    }

    public void login(String username, String password, final AuthCallback callback) {
        try {
            String url = api.getEndpoint() + "/login";

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", username);
            jsonBody.put("password", password);

            RequestBody body = RequestBody.create(jsonBody.toString(),
                    MediaType.get("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", api.getKey())
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("AUTH_SERVICE", "Login request failed", e);
                    callback.onFailure(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        Log.e("AUTH_SERVICE", "Login response not successful: " + response.code());
                        callback.onFailure(new IOException("Response not successful: " + response.code()));
                        return;
                    }
                    String responseData = response.body().string();
                    callback.onSuccess(responseData);
                }
            });
        } catch (Exception e) {
            Log.e("AUTH_SERVICE", "Exception during login", e);
            callback.onFailure(e);
        }
    }

    public interface AuthCallback {
        void onSuccess(String responseData);
        void onFailure(Exception e);
    }
}
