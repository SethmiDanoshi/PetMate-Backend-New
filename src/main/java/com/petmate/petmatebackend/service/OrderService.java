package com.petmate.petmatebackend.service;

import com.petmate.petmatebackend.model.Order;
import com.petmate.petmatebackend.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    public OrderService(OrderRepository orderRepository) { this.orderRepository = orderRepository; }

    public Order createOrder(String itemId, String itemType, String buyerId, String sellerId, Double amount,
                             String currency, String paymentIntentId) {
        Order o = new Order();
        o.setItemId(itemId);
        o.setItemType(itemType);
        o.setBuyerId(buyerId);
        o.setSellerId(sellerId);
        o.setAmount(amount);
        o.setCurrency(currency);
        o.setPaymentIntentId(paymentIntentId);
        o.setStatus(Order.OrderStatus.valueOf("PENDING"));
        o.setCreatedAt(LocalDateTime.now());
        o.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(o);
    }

    public void markPaid(String paymentIntentId) {
        orderRepository.findByPaymentIntentId(paymentIntentId).ifPresent(o -> {
            o.setStatus(Order.OrderStatus.valueOf("PAID"));
            o.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(o);
        });
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrdersByBuyerId(String buyerId) {
        return orderRepository.findByBuyerId(buyerId);
    }

    public List<Order> getOrdersBySellerId(String sellerId) {
        return orderRepository.findBySellerId(sellerId);
    }

    public Order updateOrderStatus(String id, Order.OrderStatus status) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(status);
                    order.setUpdatedAt(LocalDateTime.now());
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }
}
