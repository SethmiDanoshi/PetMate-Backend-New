package com.petmate.petmatebackend.controller;

import com.petmate.petmatebackend.dto.DoctorAuthResponse;
import com.petmate.petmatebackend.dto.DoctorSignupResponse;
import com.petmate.petmatebackend.dto.DoctorLoginRequest;
import com.petmate.petmatebackend.payload.ApiResponse;
import com.petmate.petmatebackend.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<DoctorSignupResponse>> register(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam("contactNumber") String contactNumber,
            @RequestParam("address") String address,
            @RequestParam("licensePdf") MultipartFile licensePdf) {
        
        DoctorSignupResponse response = doctorService.register(
                name, email, password, confirmPassword, contactNumber, address, licensePdf
        );

        ApiResponse<DoctorSignupResponse> apiResponse = new ApiResponse<>(
                true,
                HttpStatus.CREATED.value(),
                "Doctor registration successful",
                ZonedDateTime.now(),
                response
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<DoctorAuthResponse>> login(@RequestBody DoctorLoginRequest request) {
        DoctorAuthResponse authResponse = doctorService.login(request);

        ApiResponse<DoctorAuthResponse> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Doctor login successful",
                ZonedDateTime.now(),
                authResponse
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<ApiResponse<DoctorSignupResponse>> getDoctorProfile(@PathVariable String id) {
        var doctor = doctorService.getDoctorById(id);
        
        DoctorSignupResponse profile = new DoctorSignupResponse(
                doctor.getName(),
                doctor.getEmail(),
                doctor.getContactNumber(),
                doctor.getAddress(),
                doctor.isVerified(),
                "Profile retrieved successfully"
        );

        ApiResponse<DoctorSignupResponse> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Doctor profile retrieved successfully",
                ZonedDateTime.now(),
                profile
        );

        return ResponseEntity.ok(response);
    }






}

