package com.example.projet_session.data.remote;

import com.example.projet_session.data.remote.DTO.ReserveRequestDTO;
import com.example.projet_session.data.remote.DTO.ReserveResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReserveRequest {
    @POST("reserve")
    Call<ReserveResponse> reserve(@Body ReserveRequestDTO requestDTO);
}
