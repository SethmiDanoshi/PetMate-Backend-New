package com.petmate.petmatebackend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {
    @Id
    private String id;

    private String fullName;
    private String email;
    private String password;
    private boolean termsAccepted;
    private Role role;

    public enum Role {
        BUYER,
        SELLER
    }
}