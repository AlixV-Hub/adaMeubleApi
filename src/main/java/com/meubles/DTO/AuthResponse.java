package com.meubles.DTO;

public class AuthResponse {

    private String token;
    private String email;
    private String role;

    // Constructeur vide
    public AuthResponse() {
    }

    // Constructeur avec paramètres
    public AuthResponse(String token, String email, String role) {
        this.token = token;
        this.email = email;
        this.role = role;
    }

    // Getters et Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}