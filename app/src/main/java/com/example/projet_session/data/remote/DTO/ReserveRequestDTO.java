package com.example.projet_session.data.remote.DTO;

public class ReserveRequestDTO {
    private String date;
    private String nb_places_disponibles;
    private String id;

    public ReserveRequestDTO( String date, String nb_places_disponibles, String id) {
        this.date = date;
        this.nb_places_disponibles = nb_places_disponibles;
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNbPlacesDisponibles() {
        return nb_places_disponibles;
    }

    public void setNbPlacesDisponibles(String nb_places_disponibles) {
        this.nb_places_disponibles = nb_places_disponibles;
    }
    public String getId() {
        return id;
    }

}
