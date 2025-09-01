package com.petmate.petmatebackend.controller;

import com.petmate.petmatebackend.dto.DoctorSignupResponse;
import com.petmate.petmatebackend.model.Doctor;
import com.petmate.petmatebackend.payload.ApiResponse;
import com.petmate.petmatebackend.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DoctorService doctorService;

    @GetMapping("/doctors/pending")
    public ResponseEntity<ApiResponse<List<Doctor>>> getPendingDoctors() {
        List<Doctor> pendingDoctors = doctorService.getPendingDoctors();
        
        ApiResponse<List<Doctor>> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Pending doctors retrieved successfully",
                ZonedDateTime.now(),
                pendingDoctors
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/doctors/verified")
    public ResponseEntity<ApiResponse<List<Doctor>>> getVerifiedDoctors() {
        List<Doctor> verifiedDoctors = doctorService.getVerifiedDoctors();

        ApiResponse<List<Doctor>> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Verified doctors retrieved successfully",
                ZonedDateTime.now(),
                verifiedDoctors
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/doctors/{id}/verify")
    public ResponseEntity<ApiResponse<String>> verifyDoctor(@PathVariable String id) {
        doctorService.verifyDoctor(id);
        
        ApiResponse<String> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Doctor verified successfully",
                ZonedDateTime.now(),
                "Doctor with ID " + id + " has been verified"
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/doctors/{id}/reject")
    public ResponseEntity<ApiResponse<String>> rejectDoctor(@PathVariable String id, @RequestParam String reason) {
        doctorService.rejectDoctor(id, reason);
        
        ApiResponse<String> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Doctor rejected successfully",
                ZonedDateTime.now(),
                "Doctor with ID " + id + " has been rejected. Reason: " + reason
        );

        return ResponseEntity.ok(response);
    }
}
