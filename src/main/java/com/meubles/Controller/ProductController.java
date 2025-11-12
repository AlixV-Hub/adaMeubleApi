package com.meubles.Controller;

import com.meubles.DTO.CreateProductRequest;
import com.meubles.DTO.ProductDTO;
import com.meubles.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody CreateProductRequest request) {
        ProductDTO createdProduct = productService.createProduct(request);
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
