package com.meubles.Repository;

import com.meubles.Entity.PaymentEntity;
import com.meubles.Model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    List<PaymentEntity> findByUserId(Long userId);
    List<PaymentEntity> findByUserIdAndPaymentStatus(Long userId, PaymentStatus status);
    Optional<PaymentEntity> findByTransactionId(String transactionId);
    Optional<PaymentEntity> findByProductIdAndPaymentStatus(Long productId, PaymentStatus status);
    List<PaymentEntity> findByProductId(Long productId);
    void deleteByProduct_Id(Long productId);
}