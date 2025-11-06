package com.meubles.Security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails; // --- AJOUT ---
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(String email, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }


    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // --- NOUVELLE MÉTHODE AJOUTÉE ---
    /**
     * Valide le token ET vérifie s'il appartient bien à l'utilisateur.
     * C'est la méthode que le filtre de sécurité doit appeler.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String emailFromToken = getEmailFromToken(token);
        // Vérifie que l'email du token == l'email de l'utilisateur
        // ET que le token est techniquement valide (non expiré, bonne signature)
        return (emailFromToken.equals(userDetails.getUsername()) && validateToken(token));
    }
}