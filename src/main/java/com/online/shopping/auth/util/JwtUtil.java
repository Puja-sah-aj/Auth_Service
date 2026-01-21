package com.online.shopping.auth.util;

import com.online.shopping.auth.entity.LoginEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;


@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // ---------------- TOKEN GENERATION ----------------

    public String generateToken(LoginEntity loginEntity) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", loginEntity.getUser().getId().toString()); // ✅ store UUID as String
        claims.put("name", loginEntity.getUser().getName());
        return createToken(claims, loginEntity.getPhoneNumber());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ---------------- CLAIM EXTRACTION ----------------

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    // ---------------- UUID EXTRACTION ----------------

    public UUID extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        String userIdStr = claims.get("id", String.class); // ✅ correct key
        return UUID.fromString(userIdStr);
    }

    // ---------------- VALIDATION ----------------

    public Boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username)
                && extractExpiration(token).after(new Date());
    }
}

