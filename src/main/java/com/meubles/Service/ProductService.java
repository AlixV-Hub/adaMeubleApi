package com.meubles.Service;

import com.meubles.DTO.*;
import com.meubles.Entity.ProductEntity;
import com.meubles.Entity.UserEntity;
import com.meubles.Exception.ProductNotFoundException;
import com.meubles.Model.Status;
import com.meubles.Repository.ProductRepository;
import com.meubles.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ 1. Récupérer tous les produits disponibles
    public List<ProductDTO> getAllProducts() {
        return productRepository.findByStatus(Status.ENABLED).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ✅ 2. Récupérer un produit par ID
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

    // ✅ Méthode privée pour convertir Entity → DTO
    private ProductDTO convertToDTO(ProductEntity entity) {
        ProductDTO dto = new ProductDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setDimensions(entity.getDimensions());
        dto.setImageUrl(entity.getImageUrl());
        dto.setStatus(entity.getStatus());
        dto.setCreatedByUserId(entity.getCreatedByUserId());

        // Category
        if (entity.getCategory() != null) {
            dto.setCategory(new CategoryDTO(
                    entity.getCategory().getId(),
                    entity.getCategory().getName()
            ));
        }

        // Colors
        if (entity.getColors() != null) {
            dto.setColors(entity.getColors().stream()
                    .map(c -> new ColorDTO(c.getId(), c.getName()))
                    .collect(Collectors.toList())
            );
        }

        // Materials
        if (entity.getMaterials() != null) {
            dto.setMaterials(entity.getMaterials().stream()
                    .map(m -> new MaterialDTO(m.getId(), m.getName()))
                    .collect(Collectors.toList())
            );
        }

        return dto;
    }


    public ProductDTO createProduct(ProductDTO dto) {
        ProductEntity entity = new ProductEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setDimensions(dto.getDimensions());
        entity.setImageUrl(dto.getImageUrl());
        entity.setStatus(Status.ENABLED); // par défaut disponible
        entity.setCreatedByUserId(dto.getCreatedByUserId());

        ProductEntity saved = productRepository.save(entity);
        return convertToDTO(saved);
    }

}
