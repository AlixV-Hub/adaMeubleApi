package com.meubles.DTO;

import com.meubles.Model.PaymentMethod;
import com.meubles.Model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long id;
    private Long productId;
    private Long userId;
    private Double amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionId;
    private LocalDateTime paymentDate;
    private LocalDateTime createdAt;

    private String productName;
    private String productImageUrl;
}