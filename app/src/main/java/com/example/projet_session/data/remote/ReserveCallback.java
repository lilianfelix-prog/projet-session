package com.example.projet_session.data.remote;

import android.util.Log;

import com.example.projet_session.data.remote.DTO.ReserveResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReserveCallback implements Callback<ReserveResponse> {

    private static final String TAG = "TravelsCallback";

    private final com.example.projet_session.data.remote.ReserveCallback.ReserveListener reserveListener;

    public interface ReserveListener {
        void onReserveSuccess(ReserveResponse response);

        void onReserveFailure(String errorMessage);

        void onNetworkError(Throwable t);
    }

    public ReserveCallback(com.example.projet_session.data.remote.ReserveCallback.ReserveListener reserveListener) {
        this.reserveListener = reserveListener;
    }

    @Override
    public void onResponse(Call<ReserveResponse> call, Response<ReserveResponse> response) {
        if (response.isSuccessful() ) {
            ReserveResponse reserveResponse = response.body();

            reserveListener.onReserveSuccess(reserveResponse);

        } else {
            String errorMessage = "Login failed: " + response.code() + " " + response.message();
            reserveListener.onReserveFailure(errorMessage);
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
    public void onFailure(Call<ReserveResponse> call, Throwable t) {
        reserveListener.onNetworkError(t);
        Log.e(TAG, "Network error: " + t.getMessage(), t);
    }
}
