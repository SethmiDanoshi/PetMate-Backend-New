package com.petmate.petmatebackend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "doctors")
public class Doctor {
    @Id
    private String id;

    private String name;
    private String email;
    private String password;
    private String contactNumber;
    private String address;
    private String licensePdfUrl;
    private String licensePdfPublicId; // Cloudinary public ID for deletion
    private boolean isVerified;
    private boolean isActive;
    private Role role;

    public enum Role {
        DOCTOR
    }
}

