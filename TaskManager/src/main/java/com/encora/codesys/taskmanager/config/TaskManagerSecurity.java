package com.encora.codesys.taskmanager.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.encora.codesys.taskmanager.service.CustomUserDetailsService;

@Configuration // Indicates that this class contains Spring configuration
@EnableWebSecurity // Enables Spring Security
@CrossOrigin // Enables Cross-Origin Resource Sharing (CORS) for this configuration class
public class TaskManagerSecurity {

    @Autowired // Injects the CustomUserDetailsService bean
    private CustomUserDetailsService userDetailsService;

    @Autowired // Injects the ObjectPostProcessor bean for post-processing Spring Security objects
    private ObjectPostProcessor<Object> opp;

    @Autowired // Injects the JwtAuthenticationFilter bean
    private JwtAuthenticationFilter filter;

    // Array of URL patterns that should be ignored by Spring Security
    private final String[] ignoringPath = {
            "/swagger-ui.html", // Swagger UI HTML page
            "/swagger-ui/**", // Swagger UI resources
            "/v3/api-docs/**", // Swagger API documentation
            "/h2-console/**", // H2 database console
            "/actuator/**", // Spring Actuator endpoints
            "/login", // Login endpoint
            "/signup" // Signup endpoint
    };

    // Configures the AuthenticationManager bean
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        // Create an AuthenticationManagerBuilder
        AuthenticationManagerBuilder authenticationManagerBuilder = new AuthenticationManagerBuilder(opp);
        // Set the UserDetailsService and PasswordEncoder for authentication
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        // Build and return the AuthenticationManager
        AuthenticationManager manager = authenticationManagerBuilder.build();
        http.authenticationManager(manager);
        return manager;
    }

    // Configures the PasswordEncoder bean, using BCryptPasswordEncoder for password hashing
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configures the SecurityFilterChain bean, which defines the security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configure CORS, CSRF protection, and authorization rules
        http.cors(c->c.configurationSource(corsConfigurationSource())) // Configure CORS using a custom CorsConfigurationSource
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest()
                        .authenticated() // Require authentication for all other requests
                ).addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class) // Add JwtAuthenticationFilter before the UsernamePasswordAuthenticationFilter
                ;
        // Build and return the SecurityFilterChain
        return http.build();
    }

    // Configures the WebSecurityCustomizer bean, which allows customization of web security
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // Ignore specified URL patterns for security checks
        return (web) -> web.ignoring().requestMatchers(ignoringPath); // Allow access to swagger docs
    }

    // Configures the CorsConfigurationSource bean, which defines CORS settings
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // Create a CorsConfiguration object
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow requests from the specified origin
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        // Allow specified HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Allow specified headers
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        // Allow credentials (cookies) to be included in requests
        configuration.setAllowCredentials(true); // Important for cookies
        // Create a UrlBasedCorsConfigurationSource and register the CorsConfiguration
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        // Return the configured CorsConfigurationSource
        return source;
    }

}
