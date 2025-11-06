package com.meubles.Controller;

import com.meubles.DTO.*;
import com.meubles.Service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import java.security.Principal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }


    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        // 'principal.getName()' renverra l'email (ou l'identifiant)
        // qui a été stocké dans le "subject" du token JWT.

        // Nous déléguons au service pour récupérer les infos
        UserDto userDto = this.authService.getProfile(principal.getName());
        return ResponseEntity.ok(userDto);
    }

}