package com.meubles.Controller;

import com.meubles.DTO.CreateProductRequest;
import com.meubles.DTO.ProductDTO;
import com.meubles.Entity.UserEntity;
import com.meubles.Repository.UserRepository;
import com.meubles.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductDTO>> getAllProductsForAdmin() {
        List<ProductDTO> allProducts = productService.findAllForAdmin();
        return ResponseEntity.ok(allProducts);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody CreateProductRequest request) {
        ProductDTO createdProduct = productService.createProduct(request, "ADMIN", null);
        return ResponseEntity.ok(createdProduct);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestBody CreateProductRequest request) {
        ProductDTO updatedProduct = productService.updateProduct(id, request);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        productService.deleteProduct(id, user);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/validate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> validateProduct(@PathVariable Long id) {
        ProductDTO validatedProduct = productService.validateProduct(id);
        return ResponseEntity.ok(validatedProduct);
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> rejectProduct(@PathVariable Long id) {
        ProductDTO rejectedProduct = productService.rejectProduct(id);
        return ResponseEntity.ok(rejectedProduct);
    }
}