package com.meubles.Controller;

import com.meubles.DTO.CreatePreferenceRequestDTO;
import com.meubles.DTO.UserPreferenceDTO;
import com.meubles.Entity.UserEntity;
import com.meubles.Repository.UserRepository;
import com.meubles.Service.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users/preferences")
public class UserPreferenceController {

    @Autowired
    private UserPreferenceService userPreferenceService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<UserPreferenceDTO>> getUserPreferences(Authentication authentication) {
        UserEntity user = getUserFromAuth(authentication);
        List<UserPreferenceDTO> preferences = userPreferenceService.getUserPreferences(user.getId());
        return ResponseEntity.ok(preferences);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserPreferenceDTO> addPreference(
            @RequestBody CreatePreferenceRequestDTO request,
            Authentication authentication) {
        UserEntity user = getUserFromAuth(authentication);
        UserPreferenceDTO created = userPreferenceService.addPreference(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deletePreference(
            @PathVariable Long id,
            Authentication authentication) {
        UserEntity user = getUserFromAuth(authentication);
        userPreferenceService.deletePreference(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    private UserEntity getUserFromAuth(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Utilisateur introuvable"
                ));
    }
}