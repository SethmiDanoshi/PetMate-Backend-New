package com.petmate.petmatebackend.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private String itemId;
    private String itemType;
    private Double amount;
    private String currency;
    private String paymentIntentId;
    private String buyerId;
    private String sellerId;
}
