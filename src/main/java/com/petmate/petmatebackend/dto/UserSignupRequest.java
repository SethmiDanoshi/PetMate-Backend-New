package com.petmate.petmatebackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.petmate.petmatebackend.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserSignupRequest {
    @NotBlank
    @Email
    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("confirmPassword")
    private String confirmPassword;

    @JsonProperty("fullName")
    private String fullName;

    @JsonProperty("role")
    private User.Role role;

    @JsonProperty("termsAccepted")
    private boolean termsAccepted;
}

