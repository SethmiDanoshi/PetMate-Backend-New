package com.petmate.petmatebackend.dto;

import com.petmate.petmatebackend.model.Doctor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAuthResponse {
    private String token;
    private String name;
    private String email;
    private String contactNumber;
    private String address;
    private boolean isVerified;
    private Doctor.Role role;
}

