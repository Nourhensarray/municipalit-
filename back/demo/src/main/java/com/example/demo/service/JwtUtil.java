package com.example.demo.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Clé secrète EXACTEMENT 64 bytes pour HS512 (512 bits)
    private final String secret = "Q9xL29KfjS82Ksd93Jf92Kfs82Kfs920Dks92Kfs82Kfs92Kdls92Kfls92Kfs82";

    private final long expiration = 86400000L; // 1 jour

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String email, String service) {
        return Jwts.builder()
                .setSubject(email)
                .claim("service", service)
                .setIssuedAt(new Date())
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
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
