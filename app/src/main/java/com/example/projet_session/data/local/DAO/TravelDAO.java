package com.example.projet_session.data.local.DAO;


public class TravelDAO {
    private String montant;
    private String destination;
    private String selectedDate;
    private String status;
    private String requestedPlaces;
    private String fullName;
    private String email;
    private String phone;

    public TravelDAO(String montant, String destination, String selectedDate, String status, String requestedPlaces, String fullName, String email, String phone) {
        this.montant = montant;
        this.destination = destination;
        this.selectedDate = selectedDate;
        this.status = status;
        this.requestedPlaces = requestedPlaces;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }

    public String getMontant() {
        return montant;
    }

    public String getDestination() {
        return destination;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public String getStatus() {
        return status;
    }

    public String getRequestedPlaces() {
        return requestedPlaces;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
