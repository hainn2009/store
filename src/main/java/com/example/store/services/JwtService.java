package com.example.store.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.store.entities.Role;
import com.example.store.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Value("${spring.jwt.secret}")
    private String secret;

    @Value("${spring.jwt.accessTokenExpiration}")
    private Integer accessTokenExpiration;

    @Value("${spring.jwt.refreshTokenExpiration}")
    private Integer refreshTokenExpiration;

    public String generateAccessToken(User user) {
        return generateToken(user, accessTokenExpiration * 1000);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenExpiration * 1000);
    }

    private String generateToken(User user, final long tokenExpiration) {
        return Jwts.builder()
            .subject(user.getId().toString())
            .claim("email", user.getEmail())
            .claim("name", user.getName())
            .claim("role", user.getRole())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            var claims = getClaim(token);

            return claims.getExpiration().after(new Date());
        } catch (JwtException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private Claims getClaim(String token) {
        return Jwts
                .parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parseSignedClaims(token)
                .getPayload();
        // return Jwts.parserBuilder()
        //     .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
        //     .build()
        //     .parseClaimsJws(token)
        //     .getBody();
    }

    public Long getUserIdFromToken(String token) {
        return Long.valueOf(getClaim(token).getSubject());
    }

    public Role getRoleFromToken(String token) {
        return Role.valueOf(getClaim(token).get("role", String.class));
    }
}
