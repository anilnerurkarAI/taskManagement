package com.encora.codesys.taskmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an authentication response containing the user's name and a JWT token.
 */
@Data // Lombok annotation to generate getters, setters, toString, etc.
@AllArgsConstructor // Lombok annotation to generate a constructor with all arguments
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
public class AuthResponse {
    private String name; // User's name
    private String token; // JWT token for authentication
}
