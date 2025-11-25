package com.meubles.Repository;

import com.meubles.Entity.UserPreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreferenceEntity, Long> {

    List<UserPreferenceEntity> findByUserId(Long userId);
    Optional<UserPreferenceEntity> findByUserIdAndCategoryIdAndColorIdAndMaterialId(
            Long userId, Long categoryId, Long colorId, Long materialId
    );
    Optional<UserPreferenceEntity> findByIdAndUserId(Long id, Long userId);
}