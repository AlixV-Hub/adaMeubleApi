package com.meubles.DTO;

import lombok.Data;

@Data
public class CreatePreferenceRequestDTO {
    private Long categoryId;
    private Long colorId;
    private Long materialId;
}