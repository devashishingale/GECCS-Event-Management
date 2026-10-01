package com.geccs.eventmanagement.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    /*
     * Secret key used to sign JWT tokens.
     *
     * IMPORTANT:
     * This is a development key.
     * We will move this to environment variables
     * before production.
     */
    private static final String SECRET_KEY =
            "GECCS_Event_Management_JWT_Secret_Key_2026_Secure";

    /*
     * JWT validity period.
     *
     * 24 hours = 24 * 60 * 60 * 1000 milliseconds
     */
    private static final long JWT_EXPIRATION =
            24 * 60 * 60 * 1000;

    /*
     * Generate the secret key used by JJWT.
     */
    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    /*
     * Generate JWT token for a user.
     *
     * The token contains:
     * - college email
     * - user role
     */
    public String generateToken(
            String collegeEmail,
            String role) {

        Date now = new Date();

        Date expiration = new Date(
                now.getTime() + JWT_EXPIRATION
        );

        return Jwts.builder()
                .subject(collegeEmail)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    /*
     * Extract college email from JWT token.
     */
    public String extractCollegeEmail(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /*
     * Extract role from JWT token.
     */
    public String extractRole(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    /*
     * Check whether JWT token is valid.
     */
    public boolean isTokenValid(String token) {

        try {

            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (Exception exception) {

            return false;
        }
    }
}