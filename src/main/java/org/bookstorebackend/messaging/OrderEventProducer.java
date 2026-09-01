package org.bookstorebackend.messaging;

import lombok.RequiredArgsConstructor;
import org.bookstorebackend.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
    public class OrderEventProducer {
        private final RabbitTemplate rabbitTemplate;

        public void sendOrderCreatedEvent(OrderCreatedEvent event) {

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_EXCHANGE,
                    RabbitMQConfig.ORDER_ROUTING_KEY,
                    event
            );
            System.out.println("Order event sent to RabbitMQ: " + event);
        }
    }
