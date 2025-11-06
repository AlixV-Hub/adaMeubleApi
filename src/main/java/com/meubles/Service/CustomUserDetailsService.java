package com.meubles.Service;

import com.meubles.Entity.UserEntity;
import com.meubles.Repository.UserRepository;
import org.springframework.security.core.GrantedAuthority; // --- AJOUT ---
import org.springframework.security.core.authority.SimpleGrantedAuthority; // --- AJOUT ---
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List; // --- AJOUT ---

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Récupérer votre entité
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        // 2. Créer l'autorité (le rôle) que Spring comprend
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());

        // 3. Renvoyer l'utilisateur Spring avec le bon rôle
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(authority) // <-- LA CORRECTION (au lieu de Collections.emptyList())
        );
    }
}