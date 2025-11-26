package com.meubles.Service;

import com.meubles.DTO.PreferenceDTO;
import com.meubles.DTO.ProductDTO;
import com.meubles.Entity.PreferenceEntity;
import com.meubles.Entity.ProductEntity;
import com.meubles.Entity.UserEntity;
import com.meubles.Repository.PreferenceRepository;
import com.meubles.Repository.ProductRepository;
import com.meubles.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PreferenceService {

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    public List<PreferenceDTO> getPreferencesByUserId(Long userId) {
        return preferenceRepository.findByUser_Id(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public boolean isInPreferences(Long userId, Long productId) {
        return preferenceRepository.existsByUser_IdAndProduct_Id(userId, productId);
    }

    @Transactional
    public PreferenceDTO addPreference(Long userId, Long productId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produit introuvable"));

        if (preferenceRepository.existsByUser_IdAndProduct_Id(userId, productId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Produit déjà en favoris");
        }

        PreferenceEntity preference = new PreferenceEntity();
        preference.setUser(user);
        preference.setProduct(product);

        PreferenceEntity saved = preferenceRepository.save(preference);
        return convertToDTO(saved);
    }

    @Transactional
    public void removePreference(Long userId, Long productId) {
        PreferenceEntity preference = preferenceRepository.findByUser_IdAndProduct_Id(userId, productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Préférence introuvable"));

        preferenceRepository.delete(preference);
    }

    private PreferenceDTO convertToDTO(PreferenceEntity entity) {
        PreferenceDTO dto = new PreferenceDTO();
        dto.setId(entity.getId());
        dto.setProduct(productService.convertToDTO(entity.getProduct()));
        return dto;
    }

}