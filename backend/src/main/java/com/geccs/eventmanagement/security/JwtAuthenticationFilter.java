package com.geccs.eventmanagement.security;

import com.geccs.eventmanagement.entity.User;
import com.geccs.eventmanagement.repository.UserRepository;
import com.geccs.eventmanagement.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository) {

        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Get Authorization header
        String authorizationHeader =
                request.getHeader("Authorization");

        // If there is no Authorization header,
        // continue with the request
        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // Extract JWT
        String token =
                authorizationHeader.substring(7);

        try {

            // Validate JWT
            if (jwtService.isTokenValid(token)) {

                // Extract college email from JWT
                String collegeEmail =
                        jwtService.extractCollegeEmail(token);

                // Find user in database
                User user = userRepository
                        .findByCollegeEmail(collegeEmail)
                        .orElse(null);

                if (user != null
                        && user.isEmailVerified()
                        && "ACTIVE".equalsIgnoreCase(
                                user.getAccountStatus())) {

                    /*
                     * Get the role from the DATABASE.
                     *
                     * We deliberately use the database role
                     * instead of blindly trusting the JWT role.
                     */
                    String role = user.getRole();

                    /*
                     * Convert:
                     *
                     * STUDENT
                     *
                     * into:
                     *
                     * ROLE_STUDENT
                     *
                     * Spring Security uses the ROLE_ prefix
                     * when checking hasRole().
                     */
                    SimpleGrantedAuthority authority =
                            new SimpleGrantedAuthority(
                                    "ROLE_" + role.toUpperCase()
                            );

                    /*
                     * Create authenticated user.
                     *
                     * authentication.getName()
                     * = college email
                     */
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    collegeEmail,
                                    null,
                                    Collections.singletonList(authority)
                            );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);
                }
            }

        } catch (Exception exception) {

            /*
             * If the JWT is invalid,
             * continue the request.
             *
             * Spring Security will reject protected
             * endpoints if authentication is required.
             */
        }

        // Continue the request
        filterChain.doFilter(request, response);
    }
}