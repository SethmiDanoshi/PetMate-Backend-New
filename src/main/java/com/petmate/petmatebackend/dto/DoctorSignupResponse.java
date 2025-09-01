package com.petmate.petmatebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSignupResponse {
    private String name;
    private String email;
    private String contactNumber;
    private String address;
    private boolean isVerified;
    private String message;
}

