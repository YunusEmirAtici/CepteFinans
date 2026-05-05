package com.finanscepte.desktop.auth;

public class AuthManager {
    private static AuthManager instance;
    private String token;
    private String username;

    private AuthManager() {}

    public static synchronized AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public boolean isAuthenticated() {
        return token != null && !token.isEmpty();
    }

    public void clear() {
        this.token = null;
        this.username = null;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
