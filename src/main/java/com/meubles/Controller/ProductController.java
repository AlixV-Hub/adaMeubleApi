package com.meubles.Controller;

import com.meubles.DTO.CreateProductRequest;
import com.meubles.DTO.ProductDTO;
import com.meubles.Entity.UserEntity;
import com.meubles.Repository.UserRepository;
import com.meubles.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        productService.deleteProduct(id, user);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(
            @RequestBody CreateProductRequest request,
            Authentication authentication) {

        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        ProductDTO createdProduct = productService.createProduct(request, user.getRole().name(), user.getId());
        return ResponseEntity.ok(createdProduct);
    }


    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("{id}/buy")
    public ResponseEntity<ProductDTO> buyProduct(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        ProductDTO product = productService.buyProduct(id, userId);
        return ResponseEntity.ok(product);
    }
}
