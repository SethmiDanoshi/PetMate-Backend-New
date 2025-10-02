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
@Document(collection = "pets")
public class Pet {
    @Id
    private String id;

    private String name;
    private String type;
    private String breed;
    private String age;
    private String weight;
    private String location;
    private double price;
    private String description;

    private List<String> imageUrls;
    private List<String> imagePublicIds;

    // Seller info
    private String sellerName;
    private String sellerId;
    private String address;
    private String contactNumber;

    private LocalDate createdAt;
    private LocalDate updatedAt;
}
