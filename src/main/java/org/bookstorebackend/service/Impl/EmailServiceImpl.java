package org.bookstorebackend.service.Impl;

import lombok.RequiredArgsConstructor;
import org.bookstorebackend.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendOrderConfirmationEmail(
                String to,
                Long orderId,
                Double totalPrice) {

            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(to);
            message.setSubject("BookStore - Order Confirmation");

            message.setText(
                    "Hello,\n\n" +
                            "Your order has been placed successfully.\n\n" +
                            "Order ID: " + orderId + "\n" +
                            "Total Amount: ₹" + totalPrice + "\n\n" +
                            "Thank you for shopping with BookStore!"
            );
            mailSender.send(message);
    }

    @Override
    public void sendOtpEmail(String to, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("BookStore - Password Reset OTP");

        message.setText(
                "Hello,\n\n" +
                        "Your OTP for resetting your BookStore password is: " + otp + "\n\n" +
                        "This OTP is valid for 5 minutes.\n\n" +
                        "If you did not request a password reset, please ignore this email.\n\n" +
                        "Thank you,\n" +
                        "BookStore Team"
        );

        mailSender.send(message);
    }
}
