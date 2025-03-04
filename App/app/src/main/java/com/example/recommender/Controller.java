package com.example.recommender;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;

public class Controller extends ViewModel {
    private final OkHttpClient client = new OkHttpClient();
    private API bookAPI;
    private API databaseAPI;
    public Controller(API databaseAPI, API bookAPI) {
        this.databaseAPI = databaseAPI;
        this.bookAPI = bookAPI;
        Log.d("DEBUG", "Controller created");
        Log.d("DEBUG", String.format("API ENDPOINT: %s, KEY: %s", databaseAPI.getEndpoint(), databaseAPI.getKey()));
        Log.d("DEBUG", String.format("API ENDPOINT: %s, KEY: %s", bookAPI.getEndpoint(), bookAPI.getKey()));
    }

    public Boolean login(String username, String password) {
        try {
            String url = databaseAPI.getEndpoint() + "/login";

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", username);
            jsonBody.put("password", password);

            Log.d("LOGIN_INFO", "Sending login request to: " + url);
            RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json; charset=utf-8"));
            Log.d("LOGIN_INFO", "Sending JSON body: " + jsonBody.toString());

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", databaseAPI.getKey())
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("LOGIN_ERROR", "Failed to connect to API", e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        Log.e("LOGIN_ERROR", "Response failed: " + response.code());
                        return;
                    }

                    String responseData = response.body().string();
                    Log.d("LOGIN_SUCCESS", "Response: " + responseData);
                }
            });
        }
        catch (Exception e) {
            Log.e("LOGIN_ERROR", "JSON creation failed", e);
            return false;
        }
        return true;
    }
}
