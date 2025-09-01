package com.petmate.petmatebackend.dto;

import com.petmate.petmatebackend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String id;
    private String token;
    private String fullName;
    private String email;
    private User.Role role;

    public enum Role {
        USER,
        SELLER
    }
}