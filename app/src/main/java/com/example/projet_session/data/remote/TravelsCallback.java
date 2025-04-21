package com.example.projet_session.data.remote;

import android.util.Log;

import com.example.projet_session.auth.LoginResponse;
import com.example.projet_session.data.remote.DTO.TravelDTO;
import com.example.projet_session.data.remote.DTO.TravelsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TravelsCallback implements Callback<TravelsResponse> {

    private static final String TAG = "TravelsCallback";

    private final com.example.projet_session.data.remote.TravelsCallback.TravelsListener travelsListener;

    public interface TravelsListener {
        void onTravelsSuccess(TravelsResponse response);

        void onTravelsFailure(String errorMessage);

        void onNetworkError(Throwable t);
    }

    public TravelsCallback(com.example.projet_session.data.remote.TravelsCallback.TravelsListener travelsListener) {
        this.travelsListener = travelsListener;
    }

    @Override
    public void onResponse(Call<TravelsResponse> call, Response<TravelsResponse> response) {
        if (response.isSuccessful() ) {
            TravelsResponse travelList = response.body();
            if (travelList != null) {

                travelsListener.onTravelsSuccess(travelList);
            } else {
                travelsListener.onTravelsFailure("Response body is null");
                Log.e(TAG, "Response body is null");
            }
        } else {
            String errorMessage = "Login failed: " + response.code() + " " + response.message();
            travelsListener.onTravelsFailure(errorMessage);
            Log.e(TAG, errorMessage);
            try {
                String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                Log.e(TAG, "Error Body: " + errorBody);
            } catch (Exception e) {
                Log.e(TAG, "Error reading error body", e);
            }
        }
    }

    @Override
    public void onFailure(Call<TravelsResponse> call, Throwable t) {
        travelsListener.onNetworkError(t);
        Log.e(TAG, "Network error: " + t.getMessage(), t);
    }
}
