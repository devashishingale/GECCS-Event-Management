package com.geccs.eventmanagement.controller;

import com.geccs.eventmanagement.dto.LoginRequest;
import com.geccs.eventmanagement.dto.LoginResponse;
import com.geccs.eventmanagement.dto.RegisterRequest;
import com.geccs.eventmanagement.dto.UserResponse;
import com.geccs.eventmanagement.entity.User;
import com.geccs.eventmanagement.service.JwtService;
import com.geccs.eventmanagement.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(
            UserService userService,
            JwtService jwtService) {

        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        User registeredUser = userService.registerUser(request);

        UserResponse response = new UserResponse();

        response.setId(registeredUser.getId());
        response.setName(registeredUser.getName());
        response.setCollegeEmail(registeredUser.getCollegeEmail());
        response.setDepartment(registeredUser.getDepartment());
        response.setYear(registeredUser.getYear());
        response.setEmailVerified(registeredUser.isEmailVerified());
        response.setAccountStatus(registeredUser.getAccountStatus());
        response.setRole(registeredUser.getRole());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(
            @RequestParam String token) {

        String message = userService.verifyEmail(token);

        return ResponseEntity.ok(message);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        // Authenticate the user
        User loggedInUser = userService.loginUser(request);

        /*
         * Generate JWT.
         *
         * The token contains:
         * - college email
         * - actual application role
         */
        String token = jwtService.generateToken(
                loggedInUser.getCollegeEmail(),
                loggedInUser.getRole()
        );

        // Create user response
        UserResponse userResponse = new UserResponse();

        userResponse.setId(loggedInUser.getId());
        userResponse.setName(loggedInUser.getName());
        userResponse.setCollegeEmail(loggedInUser.getCollegeEmail());
        userResponse.setDepartment(loggedInUser.getDepartment());
        userResponse.setYear(loggedInUser.getYear());
        userResponse.setEmailVerified(loggedInUser.isEmailVerified());
        userResponse.setAccountStatus(loggedInUser.getAccountStatus());
        userResponse.setRole(loggedInUser.getRole());

        // Create login response
        LoginResponse loginResponse = new LoginResponse();

        loginResponse.setToken(token);
        loginResponse.setUser(userResponse);

        return ResponseEntity.ok(loginResponse);
    }
}