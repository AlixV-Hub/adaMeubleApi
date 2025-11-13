package com.meubles.DTO;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class UpdateProductRequest {
    private String name;
    private String description;

    @Positive(message = "Le prix doit être positif")
    private BigDecimal price;

    private Long categoryId;
    private List<Long> couleurIds;
    private List<Long> matiereIds;

    private String sku;        // (admin only)
    private String dimensions;
    private String imageUrl;
    private String status;     // ENABLED / DISABLED (admin)
}
