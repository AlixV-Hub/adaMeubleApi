package com.meubles.Repository;


import com.meubles.Entity.CategoryEntity;
import com.meubles.Entity.ColorEntity;
import com.meubles.Entity.MaterialEntity;
import com.meubles.Entity.ProductEntity;
import com.meubles.Model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByStatus(Status status);
    List<ProductEntity> findByCategory(CategoryEntity category);
    List<ProductEntity> findByColorsContaining(ColorEntity color);
    List<ProductEntity> findByMaterialsContaining(MaterialEntity material);
}
