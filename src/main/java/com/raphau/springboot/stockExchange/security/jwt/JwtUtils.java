package com.raphau.springboot.stockExchange.security.jwt;

import com.raphau.springboot.stockExchange.security.MyUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    // example value - should be removed from repository
    private final String JWT_SECRET = "JHZiJ3QpPEhlaCVRXyg9V0JzXXJbO1k8KSEzbXNtTnQoeW87N1k5c3RKbDx7YGMxWSJ0VDJ8fiIyfShYbkRq";

    private final int JWT_EXPIRATION_TIME_MS = 1000 * 60 * 60;

    public String generateJwtToken(Authentication authentication) {
        MyUserDetails userPrincipal = (MyUserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME_MS))
                .signWith(getSignKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String getSubjectFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token) {
        return Jwts.parser().verifyWith(getSignKey()).build().isSigned(token);
    }

}
