package com.example.store.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Value("${spring.jwt.secret}")
    private String secret;

    public String generateToken(String email) {
        final long tokenExpiration = 1000 * 60 * 60; // 1 hour

        return Jwts.builder().subject(email)
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

    public String getEmailFromToken(@RequestHeader String token) {
        return getClaim(token).getSubject();
    }
}
