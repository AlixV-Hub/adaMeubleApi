package com.meubles.Controller;

import com.meubles.DTO.CreatePaymentRequestDTO;
import com.meubles.DTO.PaymentDTO;
import com.meubles.Entity.UserEntity;
import com.meubles.Repository.UserRepository;
import com.meubles.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentDTO> initiatePayment(
            @RequestBody CreatePaymentRequestDTO request,
            Authentication authentication) {
        UserEntity user = getUserFromAuth(authentication);
        PaymentDTO payment = paymentService.initiatePayment(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }


    @PutMapping("/{transactionId}/confirm")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentDTO> confirmPayment(
            @PathVariable String transactionId,
            Authentication authentication) {
        PaymentDTO payment = paymentService.confirmPayment(transactionId);
        return ResponseEntity.ok(payment);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> cancelPayment(
            @PathVariable Long id,
            Authentication authentication) {
        UserEntity user = getUserFromAuth(authentication);
        paymentService.cancelPayment(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<PaymentDTO>> getUserPayments(Authentication authentication) {
        UserEntity user = getUserFromAuth(authentication);
        List<PaymentDTO> payments = paymentService.getUserPayments(user.getId());
        return ResponseEntity.ok(payments);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentDTO> getPaymentById(
            @PathVariable Long id,
            Authentication authentication) {
        UserEntity user = getUserFromAuth(authentication);
        PaymentDTO payment = paymentService.getPaymentById(id, user.getId());
        return ResponseEntity.ok(payment);
    }


    private UserEntity getUserFromAuth(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Utilisateur introuvable"
                ));
    }
}