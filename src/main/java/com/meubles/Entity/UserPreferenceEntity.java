package com.meubles.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_preferences",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "category_id", "color_id", "material_id"}
        ))
@Data
public class UserPreferenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "color_id")
    private Long colorId;

    @Column(name = "material_id")
    private Long materialId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "color_id", insertable = false, updatable = false)
    private ColorEntity color;

    @ManyToOne
    @JoinColumn(name = "material_id", insertable = false, updatable = false)
    private MaterialEntity material;
}