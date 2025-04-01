package com.example.projet_session.auth;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterCallback implements Callback<RegisterResponse> {
    private static final String TAG = "RegisterCallback";

    private final RegisterCallback.RegisterListener registerListener;

    public interface RegisterListener {
        void onRegisterSuccess(RegisterResponse registerResponse);

        void onRegisterFailure(String errorMessage);

        void onNetworkError(Throwable t);
    }

    public RegisterCallback(RegisterListener registerListener) {
        this.registerListener = registerListener;
    }

    @Override
    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
        if (response.isSuccessful()) {
            RegisterResponse registerResponse = response.body();
            if (registerResponse != null) {

                registerListener.onRegisterSuccess(registerResponse);
            } else {
                registerListener.onRegisterFailure("Response body is null");
                Log.e(TAG, "Response body is null");
            }
        } else {
            String errorMessage = "Login failed: " + response.code() + " " + response.message();
            registerListener.onRegisterFailure(errorMessage);
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
    public void onFailure(Call<RegisterResponse> call, Throwable t) {
        registerListener.onNetworkError(t);
        Log.e(TAG, "Network error: " + t.getMessage(), t);
    }
}
