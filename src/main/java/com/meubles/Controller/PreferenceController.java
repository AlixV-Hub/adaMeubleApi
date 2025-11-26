package com.meubles.Controller;

import com.meubles.DTO.PreferenceDTO;
import com.meubles.Entity.UserEntity;
import com.meubles.Repository.UserRepository;
import com.meubles.Service.PreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/preferences")
public class PreferenceController {

    @Autowired
    private PreferenceService preferenceService;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/check/{productId}")
    public ResponseEntity<Map<String, Boolean>> checkPreference(
            @PathVariable Long productId,
            Authentication authentication) {

        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        boolean isFavorite = preferenceService.isInPreferences(user.getId(), productId);

        return ResponseEntity.ok(Map.of("isFavorite", isFavorite));
    }
    @GetMapping
    public ResponseEntity<List<PreferenceDTO>> getMyPreferences(Authentication authentication) {
        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        List<PreferenceDTO> preferences = preferenceService.getPreferencesByUserId(user.getId());
        return ResponseEntity.ok(preferences);
    }
    @PostMapping("/{productId}")
    public ResponseEntity<PreferenceDTO> addPreference(
            @PathVariable Long productId,
            Authentication authentication) {

        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        PreferenceDTO preference = preferenceService.addPreference(user.getId(), productId);
        return ResponseEntity.ok(preference);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removePreference(
            @PathVariable Long productId,
            Authentication authentication) {

        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        preferenceService.removePreference(user.getId(), productId);
        return ResponseEntity.noContent().build();
    }
}