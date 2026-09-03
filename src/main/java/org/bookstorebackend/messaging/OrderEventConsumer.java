package org.bookstorebackend.messaging;

import lombok.RequiredArgsConstructor;
import org.bookstorebackend.config.RabbitMQConfig;
import org.bookstorebackend.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void consumeOrderCreatedEvent(OrderCreatedEvent event) {

        System.out.println("Order event received from RabbitMQ: " + event);

        emailService.sendOrderConfirmationEmail(
                event.getUserEmail(),
                event.getOrderId(),
                event.getTotalAmount()
        );
    }
}