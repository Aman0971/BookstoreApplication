package org.bookstorebackend.util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.bookstorebackend.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

        @Value("${jwt.secret}")
        private String secret;

        @Value("${jwt.expiration}")
        private long expiration;

        private SecretKey getSigningKey() {

            byte[] keyBytes = Decoders.BASE64.decode(secret);

            return Keys.hmacShaKeyFor(keyBytes);
        }

        public String generateToken(User user) {

            Map<String, Object> claims = new HashMap<>();

            claims.put("userId", user.getId());
            claims.put("role", user.getRole().name());

            return Jwts.builder()
                    .claims(claims)
                    .subject(user.getEmail())
                    .issuedAt(new Date())
                    .expiration(
                            new Date(System.currentTimeMillis() + expiration)
                    )
                    .signWith(getSigningKey())
                    .compact();
        }

        public String extractEmail(String token) {

            return getClaims(token).getSubject();
        }

        public String extractRole(String token) {

            return getClaims(token)
                    .get("role", String.class);
        }

        public Long extractUserId(String token) {

            Number userId = getClaims(token)
                    .get("userId", Number.class);

            return userId.longValue();
        }

        public boolean isTokenExpired(String token) {

            return getClaims(token)
                    .getExpiration()
                    .before(new Date());
        }

        private Claims getClaims(String token) {

            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }
}
