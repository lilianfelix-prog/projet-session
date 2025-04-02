package com.example.projet_session.data.remote;

import com.example.projet_session.data.remote.DTO.TravelDTO;
import com.example.projet_session.data.remote.DTO.TravelsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TravelsRequest {
    @GET("travels")
    Call<TravelsResponse> getTravelsResponse();

    @GET("search")
    Call<TravelsResponse> searchTravels(@Query("search") String searchQuery);
}
