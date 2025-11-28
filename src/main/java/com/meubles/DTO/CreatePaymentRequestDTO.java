package com.meubles.DTO;

import com.meubles.Model.PaymentMethod;
import lombok.Data;

@Data
public class CreatePaymentRequestDTO {
    private Long productId;
    private PaymentMethod paymentMethod;

    private String stripeToken;

    private String paypalOrderId;
}