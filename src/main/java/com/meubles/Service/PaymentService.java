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

    /**
     * Initier un paiement (réserve le produit)
     */
    @Transactional
    public PaymentDTO initiatePayment(Long userId, CreatePaymentRequestDTO request) {
        // Vérifier que le produit existe et est disponible
        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Produit introuvable"
                ));

        // Vérifier que le produit est disponible
        if (product.getStatus() != Status.ENABLED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ce produit n'est plus disponible"
            );
        }

        // Vérifier que l'utilisateur existe
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Utilisateur introuvable"
                ));

        // Créer le paiement
        PaymentEntity payment = new PaymentEntity();
        payment.setProductId(product.getId());
        payment.setUserId(user.getId());
        payment.setAmount(product.getPrice());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.PENDING);

        // Réserver le produit (statut ON_HOLD)
        product.setStatus(Status.ON_HOLD);
        productRepository.save(product);

        // Simuler le traitement selon la méthode
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

    /**
     * Confirmer un paiement (appelé après succès Stripe/PayPal)
     */
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

        // Marquer le paiement comme complété
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        payment.setPaymentDate(LocalDateTime.now());

        // Marquer le produit comme vendu (DISABLED)
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

    /**
     * Annuler un paiement (libère le produit)
     */
    @Transactional
    public void cancelPayment(Long paymentId, Long userId) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paiement introuvable"
                ));

        // Vérifier que c'est bien le paiement de cet utilisateur
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

        // Marquer comme échoué
        payment.setPaymentStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);

        // Libérer le produit
        ProductEntity product = productRepository.findById(payment.getProductId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Produit introuvable"
                ));

        product.setStatus(Status.ENABLED);
        productRepository.save(product);
    }

    /**
     * Récupérer l'historique des paiements d'un utilisateur
     */
    public List<PaymentDTO> getUserPayments(Long userId) {
        return paymentRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les détails d'un paiement
     */
    public PaymentDTO getPaymentById(Long paymentId, Long userId) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paiement introuvable"
                ));

        // Vérifier que c'est bien le paiement de cet utilisateur
        if (!payment.getUserId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Accès refusé"
            );
        }

        return convertToDTO(payment);
    }

    /**
     * Convertir Entity → DTO
     */
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

        // Ajouter les infos du produit si chargé
        if (entity.getProduct() != null) {
            dto.setProductName(entity.getProduct().getName());
            dto.setProductImageUrl(entity.getProduct().getImageUrl());
        }

        return dto;
    }
}