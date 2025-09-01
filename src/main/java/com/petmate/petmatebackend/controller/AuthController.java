package com.petmate.petmatebackend.controller;

import com.petmate.petmatebackend.dto.AuthResponse;
import com.petmate.petmatebackend.dto.SignUpResponse;
import com.petmate.petmatebackend.dto.UserLoginRequest;
import com.petmate.petmatebackend.dto.UserSignupRequest;
import com.petmate.petmatebackend.payload.ApiResponse;
import com.petmate.petmatebackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponse>> register (@RequestBody UserSignupRequest request){
        SignUpResponse response = authService.register(request);

        ApiResponse<SignUpResponse> apiResponse = new ApiResponse<>(
                true,
                HttpStatus.CREATED.value(),
                "Signup successful",
                ZonedDateTime.now(),
                response
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody UserLoginRequest request) {
        AuthResponse authResponse = authService.login(request);

        ApiResponse<AuthResponse> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Login successful",
                ZonedDateTime.now(),
                authResponse
        );

        return ResponseEntity.ok(response);
    }
}
