package com.geccs.eventmanagement.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(
            String recipientEmail,
            String verificationToken) {

        String verificationLink =
                "http://localhost:8080/api/auth/verify?token="
                        + verificationToken;

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(recipientEmail);
        message.setSubject("Verify your GECCS Event Management account");

        message.setText(
                "Hello,\n\n"
                + "Thank you for registering with GECCS Event Management.\n\n"
                + "Please click the link below to verify your college email address:\n\n"
                + verificationLink
                + "\n\n"
                + "This verification link will expire in 15 minutes.\n\n"
                + "If you did not create this account, please ignore this email.\n\n"
                + "Regards,\n"
                + "GECCS Event Management Team"
        );

        mailSender.send(message);
    }
}