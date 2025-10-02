package com.petmate.petmatebackend.service;

import com.petmate.petmatebackend.dto.CreatePaymentRequest;
import com.petmate.petmatebackend.dto.CreatePaymentResponse;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    public CreatePaymentResponse createPaymentIntent(CreatePaymentRequest req) throws Exception {
        PaymentIntentCreateParams.Builder builder = PaymentIntentCreateParams.builder()
                .setAmount(req.getAmount())
                .setCurrency(req.getCurrency() == null ? "LKR" : req.getCurrency());

        // optional: automatic payment methods
        builder.setAutomaticPaymentMethods(
                PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build()
        );

        if (req.getMetadata() != null) {
            builder.putAllMetadata(req.getMetadata());
        }

        PaymentIntent intent = PaymentIntent.create(builder.build());

        return new CreatePaymentResponse(intent.getClientSecret());
    }
}
