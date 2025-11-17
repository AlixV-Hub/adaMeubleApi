package com.meubles.Model;

public enum PaymentStatus {
    PENDING,        // En attente de paiement
    PROCESSING,     // Paiement en cours de traitement
    COMPLETED,      // Paiement réussi
    FAILED,         // Paiement échoué
    REFUNDED        // Remboursé
}