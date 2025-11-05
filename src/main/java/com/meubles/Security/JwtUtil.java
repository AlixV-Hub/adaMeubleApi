package com.meubles.Security;


import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
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
                    .setSubject(email)                    // L'email dans le "subject"
                    .claim("role", role)                  // Le role dans les "claims" (données custom)
                    .setIssuedAt(now)                     // Date de création
                    .setExpiration(expiryDate)            // Date d'expiration
                    .signWith(SignatureAlgorithm.HS512, secretKey)  // Signature avec notre clé
                    .compact();                           // Créer le token final (String)
        }


        public String getEmailFromToken(String token) {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();  // Récupère l'email
        }

        public boolean validateToken(String token) {
            try {
                Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token);
                return true;  // Si on arrive ici, le token est valide !
            } catch (Exception e) {
                return false;  // Si exception → token invalide
            }
        }
    }

