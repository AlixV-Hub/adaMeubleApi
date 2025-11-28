package com.meubles.DTO;

import com.meubles.Entity.ProductEntity;
import com.meubles.Model.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Setter
@Getter
public class ProductDTO {
        private Long id;
        private String name;
        private String description;
        private Double price;
        private String dimensions;
        private String imageUrl;
        private Status status;
        private Long createdByUserId;
        private CategoryDTO category;
        private List<ColorDTO> colors;
        private List<MaterialDTO> materials;
        private String sku;
        public ProductDTO() {}

        public ProductDTO(ProductEntity entity) {
            this.id = entity.getId();
            this.name = entity.getName();
            this.description = entity.getDescription();
            this.price = entity.getPrice();
            this.dimensions = entity.getDimensions();
            this.imageUrl = entity.getImageUrl();
            this.status = entity.getStatus();
            this.createdByUserId = entity.getCreator() != null ? entity.getCreator().getId() : entity.getCreatedByUserId();
            this.sku = entity.getSku() != null ? entity.getSku() : UUID.randomUUID().toString();

            if (entity.getCategory() != null) {
                this.category = new CategoryDTO(entity.getCategory().getId(), entity.getCategory().getName());
            }
            if (entity.getColors() != null) {
                this.colors = entity.getColors().stream()
                        .map(c -> new ColorDTO(c.getId(), c.getName()))
                        .collect(Collectors.toList());
            }
            if (entity.getMaterials() != null) {
                this.materials = entity.getMaterials().stream()
                        .map(m -> new MaterialDTO(m.getId(), m.getName()))
                        .collect(Collectors.toList());
            }
        }

        public ProductDTO(Long id, String name, String description, Double price, String dimensions, String imageUrl, Status status, Long createdByUserId, CategoryDTO category, List<ColorDTO> colors, List<MaterialDTO> materials) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
            this.dimensions = dimensions;
            this.imageUrl = imageUrl;
            this.status = status;
            this.createdByUserId = createdByUserId;
            this.category = category;
            this.colors = colors;
            this.materials = materials;
            this.sku = UUID.randomUUID().toString();
        }

}

