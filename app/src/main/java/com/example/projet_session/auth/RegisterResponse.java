package com.example.projet_session.auth;

public class RegisterResponse {
    private String token;


    private String userId;


    public RegisterResponse() {
    }


    public RegisterResponse(String token) {
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
