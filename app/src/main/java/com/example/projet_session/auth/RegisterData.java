package com.example.projet_session.auth;

public class RegisterData {
    private String email;
    private String password;

    public RegisterData(String email, String password){
        this.email = email;
        this.password = password;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword() {
        return password;
    }
}
