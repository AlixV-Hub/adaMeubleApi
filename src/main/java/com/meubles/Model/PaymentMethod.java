package com.meubles.Model;

public enum PaymentMethod {
    CARD,           // Carte bancaire (via Stripe)
    PAYPAL,         // PayPal
    BANK_TRANSFER,  // Virement bancaire
    CASH            // Espèces (si rencontre physique)
}