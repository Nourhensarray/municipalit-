package com.example.demo.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // 🔒 Clé secrète sécurisée (HS512 → 512 bits = 64 caractères)
    private final String SECRET = "Q9xL29KfjS82Ksd93Jf92Kfs82Kfs920Dks92Kfs82Kfs92Kdls92Kfls92Kfs82";

    private final long EXPIRATION = 86400000L; // 1 jour

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    // ========================
    // Générer un token JWT
    // ========================
    public String generateToken(String email, String service) {
        return Jwts.builder()
                .setSubject(email)
                .claim("service", service)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // ========================
    // Extraire le token depuis la requête HTTP
    // ========================
    public String extractJwtFromRequest(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer "))
            return auth.substring(7);
        return null;
    }

    // ========================
    // Extraire toutes les claims
    // ========================
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ========================
    // Extraire l’email (username)
    // ========================
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // ========================
    // Extraire le service
    // ========================
    public String extractService(String token) {
        return extractClaims(token).get("service", String.class);
    }

    // ========================
    // Vérifier si le token est expiré
    // ========================
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }
}
