package com.meubles.DTO;

import com.meubles.Model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class RegisterRequest {

    // Getters et Setters
    @Setter
    @Getter
    @NotBlank(message = "Le prénom est obligatoire")
    private String firstname;

    @Setter
    @Getter
    @NotBlank(message = "Le nom est obligatoire")
    private String lastname;

    @Setter
    @Getter
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    @Setter
    @Getter
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;

    @Setter
    @Getter
    private String address;

    public RegisterRequest() {
    }


    // Constructeur avec tous les paramètres
    public RegisterRequest(String firstname, String lastname, String email, String password, String address) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.address = address;

    }

}