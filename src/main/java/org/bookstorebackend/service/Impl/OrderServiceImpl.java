package org.bookstorebackend.service.Impl;

import lombok.RequiredArgsConstructor;
import org.bookstorebackend.dto.response.OrderItemResponseDTO;
import org.bookstorebackend.dto.response.OrderResponseDTO;
import org.bookstorebackend.entity.CartItem;
import org.bookstorebackend.entity.Order;
import org.bookstorebackend.entity.OrderItem;
import org.bookstorebackend.entity.Product;
import org.bookstorebackend.entity.User;
import org.bookstorebackend.exception.ResourceNotFoundException;
import org.bookstorebackend.messaging.OrderCreatedEvent;
import org.bookstorebackend.messaging.OrderEventProducer;
import org.bookstorebackend.repository.CartItemRepository;
import org.bookstorebackend.repository.OrderRepository;
import org.bookstorebackend.repository.ProductRepository;
import org.bookstorebackend.repository.UserRepository;
import org.bookstorebackend.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheEvict;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderEventProducer orderEventProducer;


    @Override
    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public OrderResponseDTO addOrder() {

        User user = getLoggedInUser();

        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = Order.builder()
                .user(user)
                .totalPrice(0.0)
                .status(Order.OrderStatus.PLACED)
                .build();

        List<OrderItem> orderItems = new ArrayList<>();

        double totalPrice = 0.0;

        for (CartItem cartItem : cartItems) {

            Product product = cartItem.getProduct();

            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for product: "
                                + product.getBookName()
                );
            }

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .price(product.getPrice())
                    .build();

            orderItems.add(orderItem);

            totalPrice += product.getPrice() * cartItem.getQuantity();

            // Reduce product stock
            product.setQuantity(
                    product.getQuantity() - cartItem.getQuantity()
            );
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);

        // Send order created event to RabbitMQ
        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(),
                user.getId(),
                user.getEmail(),
                savedOrder.getTotalPrice()
        );

        orderEventProducer.sendOrderCreatedEvent(event);

        // Clear user's cart after successful order
        cartItemRepository.deleteAll(cartItems);

        return mapToResponse(savedOrder);
    }
    @Override
    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public OrderResponseDTO buyNow(Long productId, Integer quantity) {

        User user = getLoggedInUser();

        // Quantity validation
        if (quantity == null || quantity <= 0) {
            throw new RuntimeException(
                    "Quantity must be greater than zero"
            );
        }

        // Find product
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + productId
                        )
                );

        // Stock validation
        if (quantity > product.getQuantity()) {
            throw new RuntimeException(
                    "Only " + product.getQuantity()
                            + " items are available for "
                            + product.getBookName()
            );
        }

        // Create Order
        Order order = Order.builder()
                .user(user)
                .totalPrice(product.getPrice() * quantity)
                .status(Order.OrderStatus.PLACED)
                .build();

        // Create Order Item
        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(quantity)
                .price(product.getPrice())
                .build();

        order.setOrderItems(List.of(orderItem));

        // Reduce stock
        product.setQuantity(product.getQuantity() - quantity);

        // Save order
        Order savedOrder = orderRepository.save(order);


       // Send order created event to RabbitMQ
        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(),
                user.getId(),
                user.getEmail(),
                savedOrder.getTotalPrice()
        );

        orderEventProducer.sendOrderCreatedEvent(event);

        return mapToResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getAllOrders(){
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getMyOrders() {

        User user = getLoggedInUser();

        List<Order> orders = orderRepository.findByUser(user);

        return orders.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private User getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));
    }

    private OrderResponseDTO mapToResponse(Order order) {

        List<OrderItemResponseDTO> items =
                order.getOrderItems()
                        .stream()
                        .map(orderItem -> {

                            Product product = orderItem.getProduct();

                            return OrderItemResponseDTO.builder()
                                    .productId(product.getId())
                                    .bookName(product.getBookName())
                                    .author(product.getAuthor())
                                    .quantity(orderItem.getQuantity())
                                    .price(orderItem.getPrice())
                                    .totalPrice(
                                            orderItem.getPrice()
                                                    * orderItem.getQuantity()
                                    )
                                    .bookImage(product.getBookImage())
                                    .build();
                        })
                        .toList();

        return OrderResponseDTO.builder()
                .orderId(order.getId())
                .userId(order.getUser().getId())
                .totalPrice(order.getTotalPrice())
                .orderDate(order.getOrderDate())
                .status(order.getStatus().name())
                .items(items)
                .build();
    }

}

