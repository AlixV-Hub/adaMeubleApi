package com.meubles.Service;

import com.meubles.DTO.*;
import com.meubles.Entity.ColorEntity;
import com.meubles.Entity.MaterialEntity;
import com.meubles.Entity.ProductEntity;
import com.meubles.Entity.UserEntity;
import com.meubles.Exception.ProductNotFoundException;
import com.meubles.Model.Status;
import com.meubles.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;


    public ProductDTO createProduct(CreateProductRequest request, String userRole, Long userId) {
        ProductEntity entity = new ProductEntity();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setPrice(request.getPrice().doubleValue());
        entity.setDimensions(request.getDimensions());
        // enregistre la création si c'est un user
        if ("USER".equals(userRole)) {
            entity.setCreatedByUserId(userId);
        }
        if (userRole.equalsIgnoreCase("ADMIN")) {
            entity.setStatus(Status.ENABLED);
            entity.setCreatedByUserId(null);
        } else if (userRole.equalsIgnoreCase("USER")) {
            entity.setStatus(Status.ENABLED);
            entity.setCreatedByUserId(userId);
        }
//SKU
        if (request.getSku() == null || request.getSku().isEmpty()) {
            entity.setSku("SKU-" + UUID.randomUUID().toString().substring(0, 8));
        } else {
            entity.setSku(request.getSku());
        }

        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            entity.setImageUrl(request.getImageUrls().get(0));
        }
        // 🔹 Associer la catégorie
        if (request.getCategoryId() != null) {
            categoryRepository.findById(request.getCategoryId())
                    .ifPresent(entity::setCategory);
        }

// 🔹 Associer les couleurs
        if (request.getCouleurIds() != null && !request.getCouleurIds().isEmpty()) {
            Set<ColorEntity> colors = new HashSet<>(colorRepository.findAllById(request.getCouleurIds()));
            entity.setColors(colors);
        }

// 🔹 Associer les matières
        if (request.getMatiereIds() != null && !request.getMatiereIds().isEmpty()) {
            Set<MaterialEntity> materials = new HashSet<>(materialRepository.findAllById(request.getMatiereIds()));
            entity.setMaterials(materials);
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
    public void deleteProduct(Long productId, UserEntity currentUser) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produit introuvable"));

        boolean isOwner = product.getCreatedByUserId() != null
                && product.getCreatedByUserId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole().name().equals("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous ne pouvez pas supprimer ce produit");
        }

        // Supprime les associations avant suppression
        product.getColors().clear();
        product.getMaterials().clear();

        productRepository.delete(product);
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
