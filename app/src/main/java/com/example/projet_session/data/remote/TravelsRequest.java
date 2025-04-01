package com.example.projet_session.data.remote;

import com.example.projet_session.data.remote.DTO.TravelDTO;
import com.example.projet_session.data.remote.DTO.TravelsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TravelsRequest {
    @GET("travels")
    Call<TravelsResponse> getTravelsResponse();
}
