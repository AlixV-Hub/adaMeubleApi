package com.meubles.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

public class CreateProductRequest {

    @NotBlank(message = "Le nom du produit est obligatoire")
    private String name;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être positif")
    private BigDecimal price;

    @NotNull(message = "La catégorie est obligatoire")
    private Long categoryId;

    private List<Long> coulorIds;
    private List<Long> materialIds;

    private String sku;
    private String dimensions;
    private List<String> imageUrls;

    // --- Constructeurs, Getters, Setters ---
    public CreateProductRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public List<Long> getCouleurIds() { return coulorIds; }
    public void setCouleurIds(List<Long> coulorIds) { this.coulorIds = coulorIds; }

    public List<Long> getMatiereIds() { return materialIds; }
    public void setMatiereIds(List<Long> materialIds) { this.materialIds = materialIds; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getDimensions() { return dimensions; }
    public void setDimensions(String dimensions) { this.dimensions = dimensions; }

    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
}
