package com.petmate.petmatebackend.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ApiError {
    private Boolean status;
    private int statusCode;
    private String error;
    private String message;

    public ApiError(HttpStatus status, String message) {
        this.status = false;
        this.statusCode = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
    }
}
