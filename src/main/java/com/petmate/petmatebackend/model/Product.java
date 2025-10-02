package com.petmate.petmatebackend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "products")
public class Product {
    @Id
    private String id;

    private String productName;
    private String brand;
    private double price;
    private String category; // dog, cat, bird, etc.
    private int quantity;
    private LocalDate date;
    private String description;

    private List<String> imageUrls;      // Cloudinary secure URLs
    private List<String> imagePublicIds; // For deleting later

    private String sellerId;
    private String type;

    private LocalDate createdAt;
    private LocalDate updatedAt;
}
