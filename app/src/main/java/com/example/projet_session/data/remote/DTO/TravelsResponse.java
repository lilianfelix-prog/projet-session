package com.example.projet_session.data.remote.DTO;

import java.util.List;

public class TravelsResponse {
    private List<TravelDTO> travels;
    private boolean success;

    public TravelsResponse() {}

    public List<TravelDTO> getTravels() {
        return travels;
    }
    public boolean isSuccess() {
        return success;
    }
}
