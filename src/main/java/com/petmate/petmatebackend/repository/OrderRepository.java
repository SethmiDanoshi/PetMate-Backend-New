package com.petmate.petmatebackend.repository;

import com.petmate.petmatebackend.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String> {
    Optional<Order> findByPaymentIntentId(String paymentIntentId);
    List<Order> findByBuyerId(String buyerId);
    List<Order> findBySellerId(String sellerId);
}
