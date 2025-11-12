package com.meubles.Entity;

import com.meubles.Model.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="products")


public class ProductEntity {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @Column(nullable = false)
    private String name;

    @Setter
    @Getter
    @Column(nullable = false)
    private String description;

    @Setter
    @Getter
    @Column(nullable = false)
    private Double price;

    @Getter
    @Setter
    @Column(nullable = false)
    private String dimensions;

    @Setter
    @Getter
    @Column(nullable = false)
    private String imageUrl;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    @Setter
    @Getter
    @Column(unique = true, nullable = false)
    private String sku;
    @Setter
    @Getter
    @Column
    private Long createdByUserId;

    @Setter
    @Getter
    @ManyToOne
    private CategoryEntity category;

    @Setter
    @Getter
    @ManyToMany
    @JoinTable(
            name = "product_color",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "color_id")
    )
    private Set<ColorEntity> colors;
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private UserEntity creator; // le vendeur (créateur du produit)

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private UserEntity buyer; // l’acheteur

    @Setter
    @Getter
    @ManyToMany
    @JoinTable(
            name = "product_material",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "material_id")
    )
    private Set<MaterialEntity> materials;

    public ProductEntity() {
        this.colors = new HashSet<>();
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


}
