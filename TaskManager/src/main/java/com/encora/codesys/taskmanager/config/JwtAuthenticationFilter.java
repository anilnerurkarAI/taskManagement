package com.encora.codesys.taskmanager.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.filter.OncePerRequestFilter;

import com.encora.codesys.taskmanager.entity.Users;
import com.encora.codesys.taskmanager.service.CustomUserDetailsService;
import com.encora.codesys.taskmanager.util.JWTUtil;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/* The JwtAuthenticationFilter class you provided is a core component for implementing JWT-based authentication in your Spring application. It acts as a security filter, intercepting incoming HTTP requests to verify the presence and validity of JWT tokens, and subsequently authenticating users. */

/* In essence, the JwtAuthenticationFilter acts as a security checkpoint, verifying the authenticity of JWTs and managing user authentication within your Spring application.
 */ 

/*@CrossOrigin: This annotation enables Cross-Origin Resource Sharing (CORS) for this filter. It's crucial for allowing requests from different origins (e.g., your React frontend running on a different port) to interact with your backend. */

@Component
@CrossOrigin 
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // JWTUtil jwtUtil: This is a utility class responsible for handling JWT operations like token generation, validation, and extracting information from the token.

    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JWTUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwtToken);
            } catch (ExpiredJwtException e) {
                // Handle token expiration
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwtToken, userDetails)) {
                Users users = customUserDetailsService.findUserByUsername(username);
                if(users.getJwtToken() != null &&  users.getLogin() == Boolean.TRUE) {
                    // Set authentication in the context
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else{
                    SecurityContextHolder.getContext().setAuthentication(null);
                }
            }
        }
        chain.doFilter(request, response);
    }

/* This method is the heart of the filter. It's automatically invoked by Spring Security for each incoming request.
HttpServletRequest request: This object provides access to the details of the incoming HTTP request (headers, parameters, etc.).
HttpServletResponse response: This object allows you to manipulate the response that will be sent back to the client.
FilterChain filterChain: This chain represents the sequence of filters in your Spring Security configuration. Calling filterChain.doFilter(request, response) passes the request to the next filter in the chain or to the target controller if this is the last filter.
 */    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        doFilter(request, response, filterChain);
    }

}


