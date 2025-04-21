package com.example.projet_session.data.remote.DTO;

import java.util.List;

public class TravelDTO {
    private String id;
    private String nom_voyage;
    private String description;
    private String prix;
    private String destination;
    private String image_url;
    private String duree_jours;
    private String type_de_voyage;
    private String activites_incluses;
    private List<TripDTO> trips;

    public TravelDTO(){}

    public TravelDTO(String id, String nom_voyage, String description, String prix, String destination, String image_url, String duree_jours, String type_de_voyage, String activites_incluses, List<TripDTO> trips) {
        this.id = id;
        this.nom_voyage = nom_voyage;
        this.description = description;
        this.prix = prix;
        this.destination = destination;
        this.image_url = image_url;
        this.duree_jours = duree_jours;
        this.type_de_voyage = type_de_voyage;
        this.activites_incluses = activites_incluses;
        this.trips = trips;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomVoyage() {
        return nom_voyage;
    }

    public void setNomVoyage(String nom_voyage) {
        this.nom_voyage = nom_voyage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getImgUrl() {
        return image_url;
    }

    public void setImageUrl(String image_url) {
        this.image_url = image_url;
    }

    public String getDureeJours() {
        return duree_jours;
    }

    public void setDureeJours(String duree_jours) {
        this.duree_jours = duree_jours;
    }

    public String getTypeDeVoyage() {
        return type_de_voyage;
    }

    public void setTypeDeVoyage(String type_de_voyage) {
        this.type_de_voyage = type_de_voyage;
    }

    public String getActivitesIncluses() {
        return activites_incluses;
    }

    public void setActivitesIncluses(String activites_incluses) {
        this.activites_incluses = activites_incluses;
    }

    public List<TripDTO> getTrips() {
        return trips;
    }

    public void setTrips(List<TripDTO> trips) {
        this.trips = trips;
    }
}
