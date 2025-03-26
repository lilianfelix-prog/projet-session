package com.example.projet_session.auth;

public class LoginResponse {
    private int response_code;
    private String token;
    private String user;

    public LoginResponse(int response_code, String token, String user) {
        this.response_code = response_code;
        this.token = token;
        this.user = user;
    }
}
