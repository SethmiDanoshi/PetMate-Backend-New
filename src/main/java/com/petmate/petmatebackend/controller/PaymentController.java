package com.petmate.petmatebackend.controller;

import com.petmate.petmatebackend.dto.CreatePaymentRequest;
import com.petmate.petmatebackend.dto.CreatePaymentResponse;
import com.petmate.petmatebackend.dto.OrderRequest;
import com.petmate.petmatebackend.model.Order;
import com.petmate.petmatebackend.service.OrderService;
import com.petmate.petmatebackend.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    public PaymentController(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody CreatePaymentRequest req) throws Exception {
        // create PaymentIntent
        CreatePaymentResponse resp = paymentService.createPaymentIntent(req);
        return ResponseEntity.ok(Map.of("status", true, "data", Map.of("clientSecret", resp.getClientSecret())));
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestHeader("Stripe-Signature") String sigHeader, @RequestBody String payload) {
        // Implement verification & event handling (see below)
        return ResponseEntity.ok("ok");
    }

    // create order after successful client confirmation (optional, recommended)
    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest req) {
        Order order = orderService.createOrder(req.getItemId(), req.getItemType(), req.getBuyerId(), req.getSellerId(),
                req.getAmount(), req.getCurrency(), req.getPaymentIntentId());
        return ResponseEntity.ok(Map.of("status", true, "data", order));
    }
}
