package com.meubles.DTO;

import com.meubles.Model.Status;
import java.util.List;
import java.util.UUID;

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


        public ProductDTO() {
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

        public Long getId() {
            return id;
        }
        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }
        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public String getDimensions() {
            return dimensions;
        }

        public void setDimensions(String dimensions) {
            this.dimensions = dimensions;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public Long getCreatedByUserId() {
            return createdByUserId;
        }

        public void setCreatedByUserId(Long createdByUserId) {
            this.createdByUserId = createdByUserId;
        }

        public CategoryDTO getCategory() {
            return category;
        }

        public void setCategory(CategoryDTO category) {
            this.category = category;
        }

        public List<ColorDTO> getColors() {
            return colors;
        }

        public void setColors(List<ColorDTO> colors) {
            this.colors = colors;
        }

        public List<MaterialDTO> getMaterials() {
            return materials;
        }

        public void setMaterials(List<MaterialDTO> materials) {
            this.materials = materials;
        }
    }

