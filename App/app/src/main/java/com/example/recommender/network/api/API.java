package com.example.recommender.network.api;

public class API {
    private String key;
    private String endpoint;
    public API() {
        key = "";
        endpoint = "";
    }
    
    public API(String key, String endpoint) {
        this.key = key;
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getKey() {
        return key;
    }
}
