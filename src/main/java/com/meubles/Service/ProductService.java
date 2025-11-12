package com.meubles.Service;

import com.meubles.DTO.*;
import com.meubles.Entity.ProductEntity;
import com.meubles.Entity.UserEntity;
import com.meubles.Exception.ProductNotFoundException;
import com.meubles.Model.Status;
import com.meubles.Repository.ProductRepository;
import com.meubles.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;


    public ProductDTO createProduct(CreateProductRequest request) {
        ProductEntity entity = new ProductEntity();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setPrice(request.getPrice().doubleValue());
        entity.setDimensions(request.getDimensions());
        entity.setStatus(Status.ENABLED);

        // SKU automatique
        if (request.getSku() == null || request.getSku().isEmpty()) {
            entity.setSku("SKU-" + UUID.randomUUID().toString().substring(0, 8));
        } else {
            entity.setSku(request.getSku());
        }

        // Image principale
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            entity.setImageUrl(request.getImageUrls().get(0));
        }

        ProductEntity saved = productRepository.save(entity);
        return convertToDTO(saved);
    }


    public List<ProductDTO> getAllProducts() {
        return productRepository.findByStatus(Status.ENABLED)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public ProductDTO getProductById(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return convertToDTO(entity);
    }


    @Transactional
    public ProductDTO buyProduct(Long productId, Long userId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produit introuvable"));

        if (product.getStatus() != Status.ENABLED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Le produit n'est pas disponible");
        }

        UserEntity buyer = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        product.setStatus(Status.DISABLED);
        product.setBuyer(buyer);

        ProductEntity updated = productRepository.save(product);
        return convertToDTO(updated);
    }


    private ProductDTO convertToDTO(ProductEntity entity) {
        ProductDTO dto = new ProductDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setDimensions(entity.getDimensions());
        dto.setImageUrl(entity.getImageUrl());
        dto.setSku(entity.getSku());
        dto.setStatus(entity.getStatus());
        dto.setCreatedByUserId(entity.getCreatedByUserId());

        if (entity.getCategory() != null) {
            dto.setCategory(new CategoryDTO(entity.getCategory().getId(), entity.getCategory().getName()));
        }
        if (entity.getColors() != null) {
            dto.setColors(entity.getColors().stream()
                    .map(c -> new ColorDTO(c.getId(), c.getName()))
                    .collect(Collectors.toList()));
        }
        if (entity.getMaterials() != null) {
            dto.setMaterials(entity.getMaterials().stream()
                    .map(m -> new MaterialDTO(m.getId(), m.getName()))
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}
