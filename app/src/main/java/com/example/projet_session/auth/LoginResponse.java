package com.example.projet_session.auth;

public class LoginResponse {
    private String token;


    private String userId;


    public LoginResponse() {
    }


    public LoginResponse(String token) {
        this.token = token;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
