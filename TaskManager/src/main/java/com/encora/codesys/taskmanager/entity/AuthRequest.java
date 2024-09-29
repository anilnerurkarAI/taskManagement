package com.encora.codesys.taskmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an authentication request containing username, password, logout flag, and token.
 */
@Data // Lombok annotation to generate getters, setters, toString, etc.
@AllArgsConstructor // Lombok annotation to generate a constructor with all arguments
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
public class AuthRequest {
    private String username; // Username for authentication
    private String password; // Password for authentication
    private boolean logout; // Flag indicating if it's a logout request
    private String token; // Token for authentication (e.g., JWT)
}
