package com.meubles.Repository;

import com.meubles.Entity.PreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreferenceRepository extends JpaRepository<PreferenceEntity, Long> {
    List<PreferenceEntity> findByUser_Id(Long userId);
    Optional<PreferenceEntity> findByUser_IdAndProduct_Id(Long userId, Long productId);
    boolean existsByUser_IdAndProduct_Id(Long userId, Long productId);
    void deleteByProduct_Id(Long productId);

}