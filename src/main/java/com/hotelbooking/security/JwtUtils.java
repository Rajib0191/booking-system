package com.hotelbooking.security;

import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@Slf4j
public class JwtUtils {

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    @Value("${jwt.secret}")
    private String jwtSecretKey;

    private SecretKey key;

    @PostConstruct  //Runs the method automatically after the bean is constructed, but before it's used.
    private void init() {
        byte[] keyBytes = jwtSecretKey.getBytes(StandardCharsets.UTF_8);
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
        //SecretKeySpec Wraps your byte array into a SecretKey object using the algorithm HmacSHA256 (used by JWT HS256).
    }

    // Generate Token
    public String generateToken(String email){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime()+jwtExpirationInMs);
        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    // Extract username/email
    public String getUsernameFromToken(String token){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Check isTokenExpired or not
    public boolean isTokenExpired(String token){
        Date expireTime = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return expireTime.before(new Date());
    }

    // Check Is Token Valid
    public boolean isTokenValid(String token, UserDetails userDetails){
        String userName = getUsernameFromToken(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}

