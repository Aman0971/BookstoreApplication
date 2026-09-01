package org.bookstorebackend.messaging;

import org.bookstorebackend.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void consumeOrderCreatedEvent(OrderCreatedEvent event) {
        System.out.println("Order event received from RabbitMQ: " + event);
        System.out.println("Processing order: " + event.getOrderId());
    }
}
