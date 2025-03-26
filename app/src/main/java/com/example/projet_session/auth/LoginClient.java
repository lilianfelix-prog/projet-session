package com.example.projet_session.auth;

import retrofit2.Call;

public class LoginClient {
    LoginRequest loginRequest = ServiceGenerator.createService(LoginRequest.class);
    LoginData loginData = new LoginData("james.monroe@examplepetstore.com", "password");
    Call<LoginResponse> call = loginRequest.login(loginData);
}
