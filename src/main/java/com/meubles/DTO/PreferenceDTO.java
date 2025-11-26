package com.meubles.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreferenceDTO {
    private Long id;
    private ProductDTO product;

    public PreferenceDTO() {}

    public PreferenceDTO(Long id, ProductDTO product) {
        this.id = id;
        this.product = product;
    }
}
