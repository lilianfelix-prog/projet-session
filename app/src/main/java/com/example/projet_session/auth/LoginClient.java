package com.example.projet_session.auth;
import retrofit2.Call;

public class LoginClient implements LoginCallback.LoginListener {

    private void login(LoginData loginData) {
        LoginRequest loginRequest = ServiceGenerator.createService(LoginRequest.class, loginData.getEmail(), loginData.getPassword());
        Call<LoginResponse> call = loginRequest.login();
        call.enqueue(new LoginCallback(this));
    }
    @Override
    public void onLoginSuccess(LoginResponse loginResponse) {
        System.out.println("login successful");
    }

    @Override
    public void onLoginFailure(String errorMessage) {
        System.out.println("login failed: " + errorMessage );
    }

    @Override
    public void onNetworkError(Throwable t) {
        System.out.println("network error: " + t.getMessage());
    }

}
