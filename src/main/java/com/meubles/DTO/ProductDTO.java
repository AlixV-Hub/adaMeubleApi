package com.meubles.DTO;

import com.meubles.Entity.ProductEntity;
import com.meubles.Model.Status;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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


    public ProductDTO(Long id, String name, String description, Double price, String dimensions,
                      String imageUrl, Status status, Long createdByUserId,
                      CategoryDTO category, List<ColorDTO> colors, List<MaterialDTO> materials) {
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


    public ProductDTO(ProductEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.dimensions = entity.getDimensions();
        this.imageUrl = entity.getImageUrl();
        this.status = entity.getStatus();
        this.sku = entity.getSku();
        this.createdByUserId = entity.getCreator() != null ? entity.getCreator().getId() : null;

        if (entity.getCategory() != null)
            this.category = new CategoryDTO(entity.getCategory());

        if (entity.getColors() != null)
            this.colors = entity.getColors().stream().map(ColorDTO::new).collect(Collectors.toList());

        if (entity.getMaterials() != null)
            this.materials = entity.getMaterials().stream().map(MaterialDTO::new).collect(Collectors.toList());
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getDimensions() { return dimensions; }
    public void setDimensions(String dimensions) { this.dimensions = dimensions; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Long getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(Long createdByUserId) { this.createdByUserId = createdByUserId; }

    public CategoryDTO getCategory() { return category; }
    public void setCategory(CategoryDTO category) { this.category = category; }

    public List<ColorDTO> getColors() { return colors; }
    public void setColors(List<ColorDTO> colors) { this.colors = colors; }

    public List<MaterialDTO> getMaterials() { return materials; }
    public void setMaterials(List<MaterialDTO> materials) { this.materials = materials; }
}
