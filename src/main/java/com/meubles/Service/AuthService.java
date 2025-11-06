package com.meubles.Service;

import com.meubles.DTO.*;
import com.meubles.Entity.UserEntity;
import com.meubles.Model.Role;
import com.meubles.Repository.UserRepository;
import com.meubles.Security.JwtUtil;
import com.meubles.Exception.EmailAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired; // --- AJOUT ---
import org.springframework.security.authentication.AuthenticationManager; // --- AJOUT ---
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // --- AJOUT ---
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager; // --- AJOUT ---

    @Autowired // --- AJOUT ---
    public AuthService(UserRepository userRepository,
                       JwtUtil jwtUtil,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager) { // --- AJOUT ---
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager; // --- AJOUT ---
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Cet email est déjà utilisé");
        }

        UserEntity user = new UserEntity();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAddress(request.getAddress());
        user.setRole(Role.USER);
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().toString());
        return new AuthResponse(token, user.getEmail(), user.getRole().toString());
    }

    public AuthResponse login(LoginRequest request) {
        // --- MODIFICATION ICI ---
        // On utilise Spring Security pour valider l'email et le mot de passe
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        // Si on arrive ici, l'utilisateur est valide.

        // On récupère l'utilisateur pour générer le token
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().toString());
        return new AuthResponse(token, user.getEmail(), user.getRole().toString());
    }

    // (La méthode getProfile que nous avons ajoutée plus tôt reste)
    public UserDto getProfile(String email) {
        UserEntity user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email));
        return mapToUserDto(user);
    }

    private UserDto mapToUserDto(UserEntity user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        userDto.setAddress(user.getAddress());
        userDto.setRole(user.getRole());
        return userDto;
    }
}