package com.example.store.services;

import java.util.Date;

import javax.crypto.SecretKey;

import com.example.store.entities.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class Jwt {
    private final Claims claims;
    private final  SecretKey secretKey;

    public Jwt(Claims claims, SecretKey secretKey) {
        this.claims = claims;
        this.secretKey = secretKey;
    }

    public boolean isExpired() {
        return claims.getExpiration().before(new Date());
    }

    public Long getUserId() {
        return Long.valueOf(claims.getSubject());
    }

    public Role getRole() {
        return Role.valueOf(claims.get("role", String.class));
    }

    public String toString() {
        return Jwts.builder().claims(claims).signWith(secretKey).compact();
    }
}
