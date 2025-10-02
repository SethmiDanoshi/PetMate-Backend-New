package com.petmate.petmatebackend.dto;

import lombok.Data;

import java.util.Map;

@Data
public class CreatePaymentRequest {
    private Long amount;
    private String currency;
    private Map<String, String> metadata;
}
