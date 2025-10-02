package com.petmate.petmatebackend.dto;

import lombok.Data;

@Data
public class PetRequest {
    private String name;
    private String type;
    private String breed;
    private String age;
    private String weight;
    private String location;
    private Double price;
    private String description;

    private String sellerName;
    private String sellerId;
    private String address;
    private String contactNumber;
}
