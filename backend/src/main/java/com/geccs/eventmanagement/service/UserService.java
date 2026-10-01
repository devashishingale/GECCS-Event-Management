package com.geccs.eventmanagement.service;

import com.geccs.eventmanagement.dto.LoginRequest;
import com.geccs.eventmanagement.dto.RegisterRequest;
import com.geccs.eventmanagement.entity.EmailVerificationToken;
import com.geccs.eventmanagement.entity.User;
import com.geccs.eventmanagement.exception.EmailAlreadyRegisteredException;
import com.geccs.eventmanagement.repository.EmailVerificationTokenRepository;
import com.geccs.eventmanagement.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailVerificationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(
            UserRepository userRepository,
            EmailVerificationTokenRepository tokenRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService) {

        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public User registerUser(RegisterRequest request) {

        // Check if college email is already registered
        if (userRepository.findByCollegeEmail(request.getCollegeEmail()).isPresent()) {
            throw new EmailAlreadyRegisteredException(
                    "College email is already registered"
            );
        }

        // Create new user
        User user = new User();

        user.setName(request.getName());
        user.setCollegeEmail(request.getCollegeEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setDepartment(request.getDepartment());
        user.setYear(request.getYear());

        // Default account settings
        user.setEmailVerified(false);
        user.setAccountStatus("ACTIVE");

        // Save user
        User savedUser = userRepository.save(user);

        // Create email verification token
        EmailVerificationToken verificationToken = new EmailVerificationToken();

        String token = UUID.randomUUID().toString();

        verificationToken.setToken(token);
        verificationToken.setUser(savedUser);
        verificationToken.setExpiresAt(
                LocalDateTime.now().plusMinutes(15)
        );

        // Save verification token
        tokenRepository.save(verificationToken);

        // Send verification email
        emailService.sendVerificationEmail(
                savedUser.getCollegeEmail(),
                token
        );

        return savedUser;
    }

    public String verifyEmail(String token) {

        // Find verification token
        EmailVerificationToken verificationToken =
                tokenRepository.findByToken(token)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid verification token"
                                )
                        );

        // Check token expiration
        if (verificationToken.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            tokenRepository.delete(verificationToken);

            throw new IllegalArgumentException(
                    "Verification token has expired"
            );
        }

        // Get user associated with token
        User user = verificationToken.getUser();

        // Mark email as verified
        user.setEmailVerified(true);

        userRepository.save(user);

        // Delete token after successful verification
        tokenRepository.delete(verificationToken);

        return "Email verified successfully";
    }

    public User loginUser(LoginRequest request) {

        // Find user by college email
        User user = userRepository
                .findByCollegeEmail(request.getCollegeEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid college email or password"
                        )
                );

        // Check password
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new IllegalArgumentException(
                    "Invalid college email or password"
            );
        }

        // Check email verification
        if (!user.isEmailVerified()) {

            throw new IllegalArgumentException(
                    "Please verify your college email before logging in"
            );
        }

        // Check account status
        if (!"ACTIVE".equalsIgnoreCase(user.getAccountStatus())) {

            throw new IllegalArgumentException(
                    "Your account is not active"
            );
        }

        return user;
    }
}