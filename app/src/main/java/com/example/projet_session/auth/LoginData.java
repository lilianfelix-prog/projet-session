package com.example.projet_session.auth;

public class LoginData {
    private String email;
    private String password;

    public LoginData(String email, String password){
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
