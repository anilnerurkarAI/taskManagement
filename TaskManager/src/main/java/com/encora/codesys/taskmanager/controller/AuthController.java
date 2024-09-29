package com.encora.codesys.taskmanager.controller;

import com.encora.codesys.taskmanager.entity.AuthRequest;
import com.encora.codesys.taskmanager.entity.AuthResponse;
import com.encora.codesys.taskmanager.entity.Users;
import com.encora.codesys.taskmanager.repository.UserRepository;
import com.encora.codesys.taskmanager.service.CustomUserDetailsService;
import com.encora.codesys.taskmanager.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController // This annotation marks the class as a REST controller
@Slf4j // Lombok annotation for adding a logger
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager; // Injects the AuthenticationManager for handling authentication

    @Autowired
    private UserRepository userRepository; // Injects the UserRepository for accessing user data

    @Autowired
    private PasswordEncoder passwordEncoder; // Injects the PasswordEncoder for password hashing

    @Autowired
    private CustomUserDetailsService customUserDetailsService; // Injects the CustomUserDetailsService for user-related operations

    @Autowired
    private JWTUtil jwtUtil; // Injects the JWTUtil for handling JWT operations

    @PostMapping("/login") // Maps POST requests to /login to this method
    @CrossOrigin // Enables Cross-Origin Resource Sharing (CORS) for this method
    public ResponseEntity<AuthResponse> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        // Attempts to authenticate the user
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            // Generates a JWT token for the authenticated user
            String jwtToken = jwtUtil.generateToken((UserDetails) authentication.getPrincipal());
            // Updates the user's JWT token in the database (for managing active sessions)
            jwtToken = customUserDetailsService.login(authRequest.getUsername(), jwtToken);
            // Returns an OK response with the JWT token
            return ResponseEntity.ok(new AuthResponse(authRequest.getUsername(),jwtToken));
        } catch (AuthenticationException e) {
            // Handles authentication failures (e.g., invalid credentials)
            log.error("Incorrect username or password", e);
            return ResponseEntity.internalServerError().
                    body(new AuthResponse("","Incorrect username or password"));
        }
    }

    @PostMapping("/signup") // Maps POST requests to /signup to this method
    @CrossOrigin // Enables Cross-Origin Resource Sharing (CORS) for this method
    public ResponseEntity<String> signup(@RequestBody Users user) {
        // Checks if a user with the same username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        // Encodes the user's password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Saves the new user to the database
        userRepository.save(user);

        // Returns a Created status indicating successful user registration
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/signout") // Maps POST requests to /signout to this method
    @CrossOrigin // Enables Cross-Origin Resource Sharing (CORS) for this method
    public ResponseEntity<String> logout(@RequestBody AuthRequest authRequest) {
        // Attempts to log the user out
        if (authRequest.isLogout() && customUserDetailsService.logout(authRequest)) {
            return ResponseEntity.ok("Logged out successfully");
        }
        // Returns a Bad Request response for invalid logout requests
        return ResponseEntity.badRequest().body("Invalid logout request");
    }

}
