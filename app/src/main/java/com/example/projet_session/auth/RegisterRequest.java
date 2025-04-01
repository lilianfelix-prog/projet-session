package com.example.projet_session.auth;

import retrofit2.http.POST;
import retrofit2.Call;

public interface RegisterRequest {
    @POST("register")
    Call<RegisterResponse> register();
}

