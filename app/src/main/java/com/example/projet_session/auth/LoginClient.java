package com.example.projet_session.auth;
import retrofit2.Call;

public class LoginClient implements LoginCallback.LoginListener {

    private void login(LoginData loginData) {
        LoginRequest loginRequest = ServiceGenerator.createService(LoginRequest.class, loginData.getEmail(), loginData.getPassword());
        Call<LoginResponse> call = loginRequest.login(loginData);
        call.enqueue(new LoginCallback(this));
    }
    @Override
    public void onLoginSuccess(LoginResponse loginResponse) {

    }

    @Override
    public void onLoginFailure(String errorMessage) {

    }

    @Override
    public void onNetworkError(Throwable t) {

    }
}
