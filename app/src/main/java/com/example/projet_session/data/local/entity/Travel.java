package com.example.projet_session.data.local.entity;

//this class represents a travel object in the local database

public class Travel {
    private int id;
    private String destination;

    public Travel(int id, String destination) {
        this.id = id;
        this.destination = destination;
    }

    public int getId() {
        return id;
    }

    public String getDestination() {
        return destination;
    }
}
