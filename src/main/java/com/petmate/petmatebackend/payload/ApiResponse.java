package com.petmate.petmatebackend.payload;

import java.time.ZonedDateTime;

import com.petmate.petmatebackend.dto.AuthResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean status;
    private int statusCode;
    private String message;
    private ZonedDateTime timestamp;
    private T data;
}
