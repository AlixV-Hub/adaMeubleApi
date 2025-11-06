package com.meubles.Service;

import com.meubles.DTO.*;
import com.meubles.Entity.UserEntity;
import com.meubles.Model.Role;
import com.meubles.Repository.UserRepository;
import com.meubles.Security.JwtUtil;
import com.meubles.Exception.EmailAlreadyExistsException;
import com.meubles.Exception.InvalidCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       JwtUtil jwtUtil,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request) {
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Cet email est déjà utilisé");
        }

        // Créer un nouvel utilisateur
        UserEntity user = new UserEntity();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAddress(request.getAddress());
        user.setRole(Role.USER);

        // Sauvegarder en BDD
        userRepository.save(user);

        // Générer un JWT
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().toString());

        // Retourner la réponse
        return new AuthResponse(token, user.getEmail(), user.getRole().toString());
    }

    public AuthResponse login(LoginRequest request) {
        // Chercher l'utilisateur par email
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Email ou mot de passe incorrect"));

        // Vérifier le mot de passe
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Email ou mot de passe incorrect");
        }

        // Générer un JWT
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().toString());

        // Retourner la réponse
        return new AuthResponse(token, user.getEmail(), user.getRole().toString());
    }
}