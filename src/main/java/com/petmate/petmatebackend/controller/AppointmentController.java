package com.petmate.petmatebackend.controller;

import com.petmate.petmatebackend.dto.AppointmentRequest;
import com.petmate.petmatebackend.dto.AppointmentResponse;
import com.petmate.petmatebackend.dto.AppointmentStatusUpdateRequest;
import com.petmate.petmatebackend.model.Appointment;
import com.petmate.petmatebackend.payload.ApiResponse;
import com.petmate.petmatebackend.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final AppointmentService appointmentService;

    // Create new appointment
    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponse>> createAppointment(
            @RequestHeader("User-Id") String userId,
            @RequestBody AppointmentRequest request) {
        
        AppointmentResponse response = appointmentService.createAppointment(userId, request);
        
        ApiResponse<AppointmentResponse> apiResponse = new ApiResponse<>(
                true,
                HttpStatus.CREATED.value(),
                "Appointment created successfully",
                ZonedDateTime.now(),
                response
        );
        
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    // Get all appointments for a user (user dashboard)
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getUserAppointments(
            @PathVariable String userId) {
        
        List<AppointmentResponse> appointments = appointmentService.getUserAppointments(userId);
        
        ApiResponse<List<AppointmentResponse>> apiResponse = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "User appointments retrieved successfully",
                ZonedDateTime.now(),
                appointments
        );
        
        return ResponseEntity.ok(apiResponse);
    }

    // Get all appointments for a doctor (doctor dashboard)
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getDoctorAppointments(
            @PathVariable String doctorId) {
        
        List<AppointmentResponse> appointments = appointmentService.getDoctorAppointments(doctorId);
        
        ApiResponse<List<AppointmentResponse>> apiResponse = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Doctor appointments retrieved successfully",
                ZonedDateTime.now(),
                appointments
        );
        
        return ResponseEntity.ok(apiResponse);
    }

    // Get specific appointment by ID
    @GetMapping("/{appointmentId}")
    public ResponseEntity<ApiResponse<?>> getAppointmentById(
            @PathVariable String appointmentId,
            @RequestHeader(value = "User-Id", required = false) String userId,
            @RequestHeader(value = "User-Role", required = false) String userRole) {

        // validations
        if (appointmentId == null || appointmentId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, HttpStatus.BAD_REQUEST.value(),
                            "Appointment ID is required", ZonedDateTime.now(), null)
            );
        }

        if (userId == null || userId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, HttpStatus.BAD_REQUEST.value(),
                            "User-Id header is required", ZonedDateTime.now(), null)
            );
        }

        if (userRole == null || userRole.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, HttpStatus.BAD_REQUEST.value(),
                            "User-Role header is required", ZonedDateTime.now(), null)
            );
        }

        try {
            AppointmentResponse appointment = appointmentService.getAppointmentById(appointmentId, userId, userRole);

            ApiResponse<AppointmentResponse> apiResponse = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Appointment retrieved successfully",
                    ZonedDateTime.now(),
                    appointment
            );

            return ResponseEntity.ok(apiResponse);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    new ApiResponse<>(false, HttpStatus.FORBIDDEN.value(),
                            ex.getMessage(), ZonedDateTime.now(), null)
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Something went wrong while retrieving appointment", ZonedDateTime.now(), null)
            );
        }
    }


    // Update appointment status (doctor only)
    @PutMapping("/{appointmentId}/status")
    public ResponseEntity<ApiResponse<AppointmentResponse>> updateAppointmentStatus(
            @PathVariable String appointmentId,
            @RequestHeader("User-Id") String doctorId,
            @RequestBody AppointmentStatusUpdateRequest request) {
        
        AppointmentResponse updatedAppointment = appointmentService.updateAppointmentStatus(
                appointmentId, doctorId, request);
        
        ApiResponse<AppointmentResponse> apiResponse = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Appointment status updated successfully",
                ZonedDateTime.now(),
                updatedAppointment
        );
        
        return ResponseEntity.ok(apiResponse);
    }

    // Get appointments by status for user or doctor
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAppointmentsByStatus(
            @PathVariable Appointment.AppointmentStatus status,
            @RequestHeader("User-Id") String userId,
            @RequestHeader("User-Role") String userRole) {
        
        List<AppointmentResponse> appointments = appointmentService.getAppointmentsByStatus(userId, userRole, status);
        
        ApiResponse<List<AppointmentResponse>> apiResponse = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Appointments by status retrieved successfully",
                ZonedDateTime.now(),
                appointments
        );
        
        return ResponseEntity.ok(apiResponse);
    }

    // Get doctor appointments by specific date
    @GetMapping("/doctor/{doctorId}/date/{date}")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getDoctorAppointmentsByDate(
            @PathVariable String doctorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        List<AppointmentResponse> appointments = appointmentService.getDoctorAppointmentsByDate(doctorId, date);
        
        ApiResponse<List<AppointmentResponse>> apiResponse = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Doctor appointments by date retrieved successfully",
                ZonedDateTime.now(),
                appointments
        );
        
        return ResponseEntity.ok(apiResponse);
    }

    // Get all appointments (admin only)
    @GetMapping
    public ResponseEntity<ApiResponse<List<Appointment>>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();

        ApiResponse<List<Appointment>> apiResponse = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "All appointments retrieved successfully",
                ZonedDateTime.now(),
                appointments
        );

        return ResponseEntity.ok(apiResponse);
    }
}

