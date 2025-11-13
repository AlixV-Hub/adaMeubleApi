package com.meubles.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

public class CreateProductRequest {

    @Getter
    @Setter
    @NotBlank(message = "Le nom du produit est obligatoire")
    private String name;

    @Getter
    @Setter
    @NotBlank(message = "La description est obligatoire")
    private String description;

    @Getter
    @Setter
    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être positif")
    private BigDecimal price;

    @Getter
    @Setter
    @NotNull(message = "La catégorie est obligatoire")
    private Long categoryId;

    private List<Long> coulorIds;
    private List<Long> materialIds;

    @Getter
    @Setter
    private String sku;
    @Setter
    @Getter
    private String dimensions;
    @Setter
    @Getter
    private List<String> imageUrls;

    // --- Constructeurs, Getters, Setters ---
    public CreateProductRequest() {}

    public List<Long> getCouleurIds() { return coulorIds; }
    public void setCouleurIds(List<Long> coulorIds) { this.coulorIds = coulorIds; }

    public List<Long> getMatiereIds() { return materialIds; }
    public void setMatiereIds(List<Long> materialIds) { this.materialIds = materialIds; }


    }

