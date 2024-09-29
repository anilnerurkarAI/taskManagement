package com.encora.codesys.taskmanager.service;

import com.encora.codesys.taskmanager.entity.AuthRequest;
import com.encora.codesys.taskmanager.entity.Users;
import com.encora.codesys.taskmanager.repository.UserRepository;
import com.encora.codesys.taskmanager.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

/**
 * Custom implementation of Spring Security's UserDetailsService for loading user details.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // Injects the UserRepository for accessing user data

    @Autowired
    private JWTUtil jwtUtil; // Injects the JWTUtil for handling JWT operations

    /**
     * Loads user details by username.
     *
     * @param username The username of the user to load.
     * @return A UserDetails object representing the user.
     * @throws UsernameNotFoundException If the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Retrieve the user from the database by username
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            // If the user is not found, throw a UsernameNotFoundException
            throw new UsernameNotFoundException("User not found");
        }
        // Create and return a Spring Security UserDetails object with the user's credentials
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    /**
     * Finds a user by their username.
     *
     * @param username The username of the user to find.
     * @return The Users entity representing the user.
     * @throws UsernameNotFoundException If the user is not found.
     */
    public Users findUserByUsername(String username) throws UsernameNotFoundException {
        // Retrieve the user from the database by username
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            // If the user is not found, throw a UsernameNotFoundException
            throw new UsernameNotFoundException("User not found");
        }
        // Return the found Users entity
        return user;
    }

    /**
     * Performs a login operation for the given username and JWT token.
     *
     * @param username  The username of the user logging in.
     * @param jwtToken The JWT token for the user.
     * @return The JWT token if the login is successful.
     * @throws UsernameNotFoundException If the user is not found.
     */
    public String login(String username, String jwtToken) {
        // Retrieve the user from the database by username
        Users users = userRepository.findByUsername(username);
        if (users == null) {
            // If the user is not found, throw a UsernameNotFoundException
            throw new UsernameNotFoundException("User not found");
        }
        // If the user is already logged in, return the existing JWT token
        if (users.getLogin() == Boolean.TRUE) {
            return users.getJwtToken();
        }
        // Update the user's login status, last login date, and JWT token
        users.setLogin(Boolean.TRUE);
        users.setLastLoginDate(Date.from(Instant.now()));
        users.setJwtToken(jwtToken);
        // Save the updated user information to the database
        userRepository.save(users);
        // Return the JWT token
        return jwtToken;
    }

    /**
     * Performs a logout operation for the given authentication request.
     *
     * @param authRequest The authentication request containing logout details.
     * @return True if the logout is successful, false otherwise.
     */
    public boolean logout(AuthRequest authRequest) {
        // Extract the username from the JWT token in the authentication request
        String username = jwtUtil.extractUsername(authRequest.getToken());
        // Check if the extracted username matches the username in the request
        if (authRequest.getUsername().equals(username)) {
            // Retrieve the user from the database by username
            Users users = userRepository.findByUsername(authRequest.getUsername());
            // If the user is not found or not logged in, return false
            if (users == null || !users.getLogin()) {
                return false;
            }
            // Update the user's login status, last logout date, and JWT token
            users.setLogin(Boolean.FALSE);
            users.setLastLogoutDate(Date.from(Instant.now()));
            users.setJwtToken(null);
            // Save the updated user information to the database
            userRepository.save(users);
            // Return true indicating successful logout
            return true;
        }
        // Return false if the usernames don't match or logout fails
        return false;
    }

    /**
     * Checks if the user is currently logged in.
     *
     * @param username The username of the user to check.
     * @return True if the user is logged in, false otherwise.
     * @throws UsernameNotFoundException If the user is not found.
     */
    public boolean isUserLogin(String username) {
        // Retrieve the user from the database by username
        Users users = userRepository.findByUsername(username);
        if (users == null) {
            // If the user is not found, throw a UsernameNotFoundException
            throw new UsernameNotFoundException("User not found");
        }
        // Return the user's login status
        return users.getLogin();
    }
}
