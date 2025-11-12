package com.meubles.Model;

public enum Status {
    ENABLED,   // Produit disponible à la vente
    DISABLED,
    PENDING,// Produit vendu ou retiré
    ON_HOLD,   // Produit réservé (optionnel)
    DENIED     // Produit refusé (optionnel, pour admin)
    ;

}