package com.example.demo.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Clé secrète d'au moins 64 caractères pour HS512
    private final String secret = "MaSuperCleTresSecretePourMunicipal12345678901234567890";
    private final long expiration = 86400000L; // 1 jour en millisecondes

    private Key getSigningKey() {
        // Convertit la clé en bytes UTF-8 et crée une clé sécurisée pour HS512
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String email, String service) {
        return Jwts.builder()
                .setSubject(email)
                .claim("service", service)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractService(String token) {
        return extractClaims(token).get("service", String.class);
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
