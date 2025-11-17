package com.meubles.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferenceDTO {
    private Long id;
    private Long userId;
    private Long categoryId;
    private Long colorId;
    private Long materialId;

    // Optionnel : noms pour affichage
    private String categoryName;
    private String colorName;
    private String materialName;
}