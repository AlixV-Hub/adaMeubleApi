package com.meubles.Service;

import com.meubles.DTO.*;
import com.meubles.Entity.ProductEntity;
import com.meubles.Exception.ProductNotFoundException;
import com.meubles.Model.Status;
import com.meubles.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findByStatus(Status.ENABLED).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return convertToDTO(entity);
    }

    // Méthode privée pour la conversion (évite la duplication)
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
        dto.setCategory(new CategoryDTO(
                entity.getCategory().getId(),
                entity.getCategory().getName()
        ));

        // Colors
        dto.setColors(entity.getColors().stream()
                .map(c -> new ColorDTO(c.getId(), c.getName()))
                .collect(Collectors.toList())
        );

        // Materials
        dto.setMaterials(entity.getMaterials().stream()
                .map(m -> new MaterialDTO(m.getId(), m.getName()))
                .collect(Collectors.toList())
        );

        return dto;
    }
}