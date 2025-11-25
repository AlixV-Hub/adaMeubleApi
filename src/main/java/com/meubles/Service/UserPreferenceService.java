package com.meubles.Service;

import com.meubles.DTO.CreatePreferenceRequestDTO;
import com.meubles.DTO.UserPreferenceDTO;
import com.meubles.Entity.UserPreferenceEntity;
import com.meubles.Repository.UserPreferenceRepository;
import com.meubles.Repository.CategoryRepository;
import com.meubles.Repository.ColorRepository;
import com.meubles.Repository.MaterialRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserPreferenceService {

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private MaterialRepository materialRepository;

    public List<UserPreferenceDTO> getUserPreferences(Long userId) {
        return userPreferenceRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserPreferenceDTO addPreference(Long userId, CreatePreferenceRequestDTO request) {
        if (request.getCategoryId() == null &&
                request.getColorId() == null &&
                request.getMaterialId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Au moins un critère doit être renseigné"
            );
        }

        var existing = userPreferenceRepository.findByUserIdAndCategoryIdAndColorIdAndMaterialId(
                userId,
                request.getCategoryId(),
                request.getColorId(),
                request.getMaterialId()
        );

        if (existing.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cette préférence existe déjà"
            );
        }

        UserPreferenceEntity entity = new UserPreferenceEntity();
        entity.setUserId(userId);
        entity.setCategoryId(request.getCategoryId());
        entity.setColorId(request.getColorId());
        entity.setMaterialId(request.getMaterialId());

        UserPreferenceEntity saved = userPreferenceRepository.save(entity);
        return convertToDTO(saved);
    }

    @Transactional
    public void deletePreference(Long preferenceId, Long userId) {
        UserPreferenceEntity preference = userPreferenceRepository.findByIdAndUserId(preferenceId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Préférence introuvable ou vous n'êtes pas autorisé à la supprimer"
                ));

        userPreferenceRepository.delete(preference);
    }

    private UserPreferenceDTO convertToDTO(UserPreferenceEntity entity) {
        UserPreferenceDTO dto = new UserPreferenceDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setCategoryId(entity.getCategoryId());
        dto.setColorId(entity.getColorId());
        dto.setMaterialId(entity.getMaterialId());

        if (entity.getCategoryId() != null) {
            categoryRepository.findById(entity.getCategoryId())
                    .ifPresent(cat -> dto.setCategoryName(cat.getName()));
        }
        if (entity.getColorId() != null) {
            colorRepository.findById(entity.getColorId())
                    .ifPresent(color -> dto.setColorName(color.getName()));
        }
        if (entity.getMaterialId() != null) {
            materialRepository.findById(entity.getMaterialId())
                    .ifPresent(mat -> dto.setMaterialName(mat.getName()));
        }

        return dto;
    }
}