package com.encora.codesys.taskmanager.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Utility class for handling JWT (JSON Web Token) operations.
 */
@Component // Marks this class as a Spring component
public class JWTUtil {

    private final String KEY = "MTIzNTQ3ODk1NTY1NGFmZGY0NjIzc2Rmc2Q2NDM0MzI0MzI0MzI0MzJzZmRzZjIxczMyZnNkNjVmc2RmMTJzZg=="; // Secret key for signing JWTs (should be stored securely in a real application)
    private static final int MINUTES = 30; // Token expiration time in minutes

    /**
     * Generates a JWT token for the given user details.
     *
     * @param userDetails The user details to include in the token.
     * @return The generated JWT token.
     */
    public String generateToken(UserDetails userDetails) {
        Instant now = Instant.now(); // Get the current time
        return Jwts.builder()
                .header().type("JWT").and() // Set the token type to "JWT"
                .subject(userDetails.getUsername()) // Set the token subject to the username
                .issuedAt(Date.from(now)) // Set the token issue date to now
                .expiration(Date.from(now.plus(MINUTES, ChronoUnit.MINUTES))) // Set the token expiration time
                .signWith(key()) // Sign the token with the secret key
                .compact(); // Generate the final compact token string
    }

    /**
     * Creates a secret key from the encoded key string.
     *
     * @return The secret key.
     */
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(KEY)); // Decode the base64-encoded key and create a key object
    }

    /**
     * Extracts claims (payload) from the given JWT token.
     *
     * @param token The JWT token.
     * @return The claims extracted from the token.
     */
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key()) // Verify the token signature with the secret key
                .build()
                .parseSignedClaims(token) // Parse the token and extract claims
                .getPayload(); // Get the payload (claims) from the parsed token
    }

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token The JWT token.
     * @return The username extracted from the token.
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject(); // Extract the subject (username) from the token claims
    }

    /**
     * Checks if the given JWT token is expired.
     *
     * @param token The JWT token.
     * @return True if the token is expired, false otherwise.
     */
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date()); // Check if the token expiration date is before the current date
    }

    /**
     * Validates the given JWT token.
     *
     * @param token The JWT token.
     * @return True if the token is valid, false otherwise.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token); // Try to parse and verify the token
            return true; // Token is valid
        } catch (Exception e) {
            return false; // Token is not valid (e.g., signature mismatch, expired)
        }
    }

    /**
     * Gets the username from the given JWT token.
     *
     * @param token The JWT token.
     * @return The username from the token.
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token).getPayload(); // Parse and verify the token
        return claims.getSubject(); // Extract the subject (username) from the token claims
    }

    /**
     * Validates the given JWT token against the provided user details.
     *
     * @param token       The JWT token.
     * @param userDetails The user details to validate against.
     * @return True if the token is valid for the given user, false otherwise.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        // Check if the username in the token matches the provided username and if the token is not expired
        return (userDetails.getUsername().equals(extractUsername(token)) && !isTokenExpired(token));
    }
}
