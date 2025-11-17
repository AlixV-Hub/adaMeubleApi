package com.meubles.DTO;

import lombok.Data;

@Data
public class CreatePreferenceRequestDTO {
    private Long categoryId;  // nullable
    private Long colorId;     // nullable
    private Long materialId;  // nullable
}