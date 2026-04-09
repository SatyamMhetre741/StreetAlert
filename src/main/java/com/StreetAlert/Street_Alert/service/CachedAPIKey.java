package com.StreetAlert.Street_Alert.service;

import org.springframework.stereotype.Service;

@Service
public class CachedAPIKey {
    private final APIService apiService;
    private final Object lock = new Object();

    private volatile String apiKey;

    public CachedAPIKey(APIService apiService) {
        this.apiService = apiService;
    }

    public String getApiKey() {
        String current = apiKey;
        if (current != null) {
            return current;
        }

        synchronized (lock) {
            if (apiKey == null) {
                apiKey = apiService.getApiKey();
            }
            return apiKey;
        }
    }

    public void refreshApiKey() {
        synchronized (lock) {
            apiKey = apiService.getApiKey();
        }
    }

    public void clearApiKey() {
        synchronized (lock) {
            apiKey = null;
        }
    }

    public void setApiKey() {
        refreshApiKey();
    }

}
