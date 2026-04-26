package com.familytree.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtUtils {

    private static final int MIN_SECRET_LENGTH = 32;
    private static final int DEFAULT_EXPIRATION = 86400000; // 24 hours

    private final SecretKey signingKey;
    private final long expiration;

    public JwtUtils(
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.expiration:86400000}") long expiration) {
        this.expiration = expiration > 0 ? expiration : DEFAULT_EXPIRATION;

        if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
            throw new IllegalStateException(
                    "JWT Secret must be configured. Please set 'jwt.secret' in application configuration.");
        }

        if (jwtSecret.length() < MIN_SECRET_LENGTH) {
            throw new IllegalStateException(
                    String.format("JWT Secret must be at least %d characters long for security reasons. " +
                            "Current length: %d", MIN_SECRET_LENGTH, jwtSecret.length()));
        }

        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        log.info("[JWT初始化] Token expiration set to {}ms", this.expiration);
    }

    public String generateToken(Long userId) {
        return generateToken(userId, new HashMap<>());
    }

    public String generateToken(Long userId, Map<String, Object> additionalClaims) {
        Map<String, Object> claims = new HashMap<>(additionalClaims);
        claims.put("userId", userId);
        claims.put("iat", System.currentTimeMillis());

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .compact();

        log.debug("[生成Token] userId={}, expiration={}", userId, expirationDate);
        return token;
    }

    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(token)
                    .getBody();

            String subject = claims.getSubject();
            return Long.parseLong(subject);
        } catch (Exception e) {
            log.warn("[解析Token失败] error={}", e.getMessage());
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
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.warn("[Token已过期] error={}", e.getMessage());
            return false;
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            log.warn("[Token格式错误] error={}", e.getMessage());
            return false;
        } catch (io.jsonwebtoken.security.SecurityException e) {
            log.warn("[Token签名验证失败] error={}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("[Token验证异常] error={}", e.getMessage());
            return false;
        }
    }

    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.warn("[获取Token Claims失败] error={}", e.getMessage());
            return null;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                return true;
            }
            Date expiration = claims.getExpiration();
            return expiration != null && expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public long getExpiration() {
        return expiration;
    }
}