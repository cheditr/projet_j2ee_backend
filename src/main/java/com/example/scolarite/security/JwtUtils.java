package com.example.scolarite.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

@Component
public class JwtUtils {

    private final String jwtSecretString = "uneCleTresTresLongueEtSecuriseeQuiEstUtiliseePourSignerLesTokensJWT1234567890";
    private final SecretKey jwtSecret = Keys.hmacShaKeyFor(jwtSecretString.getBytes(StandardCharsets.UTF_8));
    private int jwtExpirationMs = 604800000; // 7 jours en millisecondes

    public String generateJwtToken(Authentication authentication) {
        // Récupérer l'utilisateur (principal) du contexte d'authentification
        String username = authentication.getName();

        // Générer le JWT
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            // Si le JWT est invalide, on peut le traiter (ex: token expiré)
        }
        return false;
    }
}
