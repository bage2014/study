package com.familytree.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
    @Value("${jwt.expiration}")
    private long expiration;

    // 使用安全的签名密钥
    private final SecretKey signingKey;

    public JwtUtils() {
        // 使用安全的密钥生成方法
        this.signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String generateToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        try {
            return Long.parseLong(Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject());
        } catch (Exception e) {
            return null;
        }
    }
    
    public Long extractUserId(String token) {
        return getUserIdFromToken(token);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
