package com.meubles.DTO;

import com.meubles.Entity.MaterialEntity;

public class MaterialDTO {
    private Long id;
    private String name;

    public MaterialDTO() {
    }

    public MaterialDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MaterialDTO(MaterialEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
