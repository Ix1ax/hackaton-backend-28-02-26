package dev.ixlax.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final String secret;
    private final long expiresDays;

    public JwtService(
            @Value("${security.jwt.secret:${JWT_SECRET:dev-secret-change-me}}") String secret,
            @Value("${security.jwt.expires-days:${JWT_EXPIRES_DAYS:3650}}") long expiresDays
    ) {
        this.secret = secret;
        this.expiresDays = expiresDays;
    }

    public String generateToken(String subject, Map<String, Object> extraClaims) {
        Instant now = Instant.now();
        Instant exp = now.plus(expiresDays, ChronoUnit.DAYS);

        return Jwts.builder()
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .claims(extraClaims)
                .signWith(secretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractSubject(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String subject = extractSubject(token);
        return subject != null && subject.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration != null && expiration.before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey secretKey() {
        byte[] keyBytes = maybeBase64Decode(secret);
        if (keyBytes.length < 32) {
            keyBytes = sha256(secret);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private static byte[] maybeBase64Decode(String value) {
        try {
            return Decoders.BASE64.decode(value);
        } catch (Exception ignored) {
            return value.getBytes(StandardCharsets.UTF_8);
        }
    }

    private static byte[] sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(value.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalStateException("Cannot hash JWT secret", e);
        }
    }
}

