package com.petmate.petmatebackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String itemId;
    private String itemType;
    private String buyerId;
    private String sellerId;
    private Double amount;
    private String currency;
    private String paymentIntentId;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum OrderStatus {
        PENDING,
        PAID,
        SHIPPED,
        COMPLETED,
        CANCELLED
    }
}
