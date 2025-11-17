package com.meubles.Repository;

import com.meubles.Entity.UserPreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreferenceEntity, Long> {

    // Trouver toutes les préférences d'un utilisateur
    List<UserPreferenceEntity> findByUserId(Long userId);

    // Vérifier si une préférence existe déjà (pour éviter les doublons)
    Optional<UserPreferenceEntity> findByUserIdAndCategoryIdAndColorIdAndMaterialId(
            Long userId, Long categoryId, Long colorId, Long materialId
    );

    // Trouver une préférence par ID et userId (pour vérifier que c'est bien SA préférence avant de supprimer)
    Optional<UserPreferenceEntity> findByIdAndUserId(Long id, Long userId);
}