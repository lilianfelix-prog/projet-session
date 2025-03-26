package com.example.projet_session.auth;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginCallback implements Callback<LoginResponse> {

    private static final String TAG = "LoginCallback";

    private final LoginListener loginListener;

    public interface LoginListener {
        void onLoginSuccess(LoginResponse loginResponse);

        void onLoginFailure(String errorMessage);

        void onNetworkError(Throwable t);
    }

    public LoginCallback(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    @Override
    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
        if (response.isSuccessful()) {
            LoginResponse loginResponse = response.body();
            if (loginResponse != null) {
                loginListener.onLoginSuccess(loginResponse);
            } else {
                loginListener.onLoginFailure("Response body is null");
                Log.e(TAG, "Response body is null");
            }
        } else {
            String errorMessage = "Login failed: " + response.code() + " " + response.message();
            loginListener.onLoginFailure(errorMessage);
            Log.e(TAG, errorMessage);
            try {
                // Attempt to get error body if available
                String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                Log.e(TAG, "Error Body: " + errorBody);
            } catch (Exception e) {
                Log.e(TAG, "Error reading error body", e);
            }
        }
    }

    @Override
    public void onFailure(Call<LoginResponse> call, Throwable t) {
        loginListener.onNetworkError(t);
        Log.e(TAG, "Network error: " + t.getMessage(), t);
    }
}