package com.meubles.Service;

import com.meubles.DTO.CreatePaymentRequestDTO;
import com.meubles.DTO.PaymentDTO;
import com.meubles.Entity.PaymentEntity;
import com.meubles.Entity.ProductEntity;
import com.meubles.Entity.UserEntity;
import com.meubles.Model.PaymentMethod;
import com.meubles.Model.PaymentStatus;
import com.meubles.Model.Status;
import com.meubles.Repository.PaymentRepository;
import com.meubles.Repository.ProductRepository;
import com.meubles.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public PaymentDTO initiatePayment(Long userId, CreatePaymentRequestDTO request) {
        // Vérifier que le produit existe et est disponible
        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Produit introuvable"
                ));

        if (product.getStatus() != Status.ENABLED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ce produit n'est plus disponible"
            );
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Utilisateur introuvable"
                ));

        PaymentEntity payment = new PaymentEntity();
        payment.setProductId(product.getId());
        payment.setUserId(user.getId());
        payment.setAmount(product.getPrice());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.PENDING);

        product.setStatus(Status.ON_HOLD);
        productRepository.save(product);

        if (request.getPaymentMethod() == PaymentMethod.CARD) {
            // TODO: Intégration Stripe réelle
            payment.setTransactionId("stripe_sim_" + System.currentTimeMillis());
            payment.setPaymentStatus(PaymentStatus.PROCESSING);
        } else if (request.getPaymentMethod() == PaymentMethod.PAYPAL) {
            // TODO: Intégration PayPal réelle
            payment.setTransactionId("paypal_sim_" + System.currentTimeMillis());
            payment.setPaymentStatus(PaymentStatus.PROCESSING);
        }

        PaymentEntity saved = paymentRepository.save(payment);
        return convertToDTO(saved);
    }

    @Transactional
    public PaymentDTO confirmPayment(String transactionId) {
        PaymentEntity payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paiement introuvable"
                ));

        if (payment.getPaymentStatus() == PaymentStatus.COMPLETED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ce paiement a déjà été confirmé"
            );
        }

        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        payment.setPaymentDate(LocalDateTime.now());
        ProductEntity product = productRepository.findById(payment.getProductId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Produit introuvable"
                ));

        product.setStatus(Status.DISABLED);
        product.setBuyer(userRepository.findById(payment.getUserId()).orElse(null));
        productRepository.save(product);

        PaymentEntity updated = paymentRepository.save(payment);
        return convertToDTO(updated);
    }

    @Transactional
    public void cancelPayment(Long paymentId, Long userId) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paiement introuvable"
                ));
        if (!payment.getUserId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Vous ne pouvez pas annuler ce paiement"
            );
        }

        if (payment.getPaymentStatus() == PaymentStatus.COMPLETED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Impossible d'annuler un paiement déjà complété"
            );
        }

        payment.setPaymentStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);

        ProductEntity product = productRepository.findById(payment.getProductId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Produit introuvable"
                ));

        product.setStatus(Status.ENABLED);
        productRepository.save(product);
    }

    public List<PaymentDTO> getUserPayments(Long userId) {
        return paymentRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PaymentDTO getPaymentById(Long paymentId, Long userId) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paiement introuvable"
                ));

        if (!payment.getUserId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Accès refusé"
            );
        }

        return convertToDTO(payment);
    }

    private PaymentDTO convertToDTO(PaymentEntity entity) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProductId());
        dto.setUserId(entity.getUserId());
        dto.setAmount(entity.getAmount());
        dto.setPaymentMethod(entity.getPaymentMethod());
        dto.setPaymentStatus(entity.getPaymentStatus());
        dto.setTransactionId(entity.getTransactionId());
        dto.setPaymentDate(entity.getPaymentDate());
        dto.setCreatedAt(entity.getCreatedAt());

        if (entity.getProduct() != null) {
            dto.setProductName(entity.getProduct().getName());
            dto.setProductImageUrl(entity.getProduct().getImageUrl());
        }

        return dto;
    }
}