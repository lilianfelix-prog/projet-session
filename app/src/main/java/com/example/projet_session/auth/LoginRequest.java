package com.example.projet_session.auth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginRequest {
    @POST("/api/login")
    Call<LoginResponse> login(@Body LoginData loginData);
}
