package org.bookstorebackend.service;
public interface EmailService {

    void sendOrderConfirmationEmail(
                String to,
                Long orderId,
                Double totalPrice
    );
    void sendOtpEmail(
            String to,
            String otp
    );
}
