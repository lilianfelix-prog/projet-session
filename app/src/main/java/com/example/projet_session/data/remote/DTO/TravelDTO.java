package com.example.projet_session.data.remote.DTO;

//this class represents a travel object that match the JSON response
public class TravelDTO {
    private int id;
    private String description;
    private String destination;
    private double price;
    private String imgUrl;

    public TravelDTO(){}

    public TravelDTO(int id, String description, String destination, double price, String imgUrl) {
        this.id = id;
        this.description = description;
        this.destination = destination;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getDestination() {
        return destination;
    }

    public double getPrice() {
        return price;
    }
}
