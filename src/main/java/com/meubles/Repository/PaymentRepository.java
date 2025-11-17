package com.meubles.Repository;

import com.meubles.Entity.PaymentEntity;
import com.meubles.Model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    // Trouver tous les paiements d'un utilisateur
    List<PaymentEntity> findByUserId(Long userId);

    // Trouver les paiements d'un utilisateur par status
    List<PaymentEntity> findByUserIdAndPaymentStatus(Long userId, PaymentStatus status);

    // Trouver un paiement par transaction ID (Stripe/PayPal)
    Optional<PaymentEntity> findByTransactionId(String transactionId);

    // Trouver le paiement d'un produit spécifique
    Optional<PaymentEntity> findByProductIdAndPaymentStatus(Long productId, PaymentStatus status);

    // Trouver tous les paiements d'un produit (historique)
    List<PaymentEntity> findByProductId(Long productId);
}