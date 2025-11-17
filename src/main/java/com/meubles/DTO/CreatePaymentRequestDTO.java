package com.meubles.DTO;

import com.meubles.Model.PaymentMethod;
import lombok.Data;

@Data
public class CreatePaymentRequestDTO {
    private Long productId;
    private PaymentMethod paymentMethod;  // CARD, PAYPAL, etc.

    // Pour Stripe (si paiement par carte)
    private String stripeToken;  // Token généré par Stripe.js côté frontend

    // Pour PayPal (si paiement PayPal)
    private String paypalOrderId;  // ID de commande PayPal
}