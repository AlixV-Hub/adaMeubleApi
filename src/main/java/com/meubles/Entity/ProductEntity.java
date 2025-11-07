package com.meubles.Entity;

import com.meubles.Model.Status;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="products")


public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String dimensions;

    @Column(nullable = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column
    private Long createdByUserId;

    @ManyToOne
    private CategoryEntity category;

    @ManyToMany
    @JoinTable(
            name = "product_color",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "color_id")
    )
    private Set<ColorEntity> colors;

    @ManyToMany
    @JoinTable(
            name = "product_material",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "material_id")
    )
    private Set<MaterialEntity> materials;

    public ProductEntity() {
        this.colors = new HashSet<>(); // évite les doublons
        this.materials = new HashSet<>();
    }

    public ProductEntity(Long id, String name, String description, Double price, String dimensions, String imageUrl, Status status, Long createdByUserId, CategoryEntity category, Set<ColorEntity> colors, Set<MaterialEntity> materials) {
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

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public Set<ColorEntity> getColors() {
        return colors;
    }

    public void setColors(Set<ColorEntity> colors) {
        this.colors = colors;
    }

    public Set<MaterialEntity> getMaterials() {
        return materials;
    }

    public void setMaterials(Set<MaterialEntity> materials) {
        this.materials = materials;
    }
}
