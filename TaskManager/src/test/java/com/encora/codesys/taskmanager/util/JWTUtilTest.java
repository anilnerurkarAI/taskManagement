package com.encora.codesys.taskmanager.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

class JWTUtilTest {

    @InjectMocks
    private JWTUtil jwtUtil;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDetails = new User("testuser", "testpassword", new ArrayList<>());
    }

    @Test
    void generateToken() {
        String token = jwtUtil.generateToken(userDetails);
        assertNotNull(token);
    }

    @Test
    void extractClaims() {
        String token = jwtUtil.generateToken(userDetails);
        Claims claims = jwtUtil.extractClaims(token);
        assertNotNull(claims);
    }

    @Test
    void extractUsername() {
        String token = jwtUtil.generateToken(userDetails);
        String username = jwtUtil.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void isTokenExpired() {
        String token = jwtUtil.generateToken(userDetails);
        boolean isExpired = jwtUtil.isTokenExpired(token);
        assertFalse(isExpired);
    }

    @Test
    void validateToken() {
        String token = jwtUtil.generateToken(userDetails);
        boolean isValid = jwtUtil.validateToken(token);
        assertTrue(isValid);
    }

    @Test
    void getUsernameFromToken() {
        String token = jwtUtil.generateToken(userDetails);
        String username = jwtUtil.getUsernameFromToken(token);
        assertEquals("testuser", username);
    }

    @Test
    void validateTokenWithUserDetails() {
        String token = jwtUtil.generateToken(userDetails);
        boolean isValid = jwtUtil.validateToken(token, userDetails);
        assertTrue(isValid);
    }
}
