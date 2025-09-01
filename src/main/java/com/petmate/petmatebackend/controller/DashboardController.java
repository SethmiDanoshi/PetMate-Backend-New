package com.petmate.petmatebackend.controller;

import com.petmate.petmatebackend.dto.AppointmentResponse;
import com.petmate.petmatebackend.model.Appointment;
import com.petmate.petmatebackend.payload.ApiResponse;
import com.petmate.petmatebackend.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final AppointmentService appointmentService;

    // User Dashboard - Get summary and recent appointments
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserDashboard(@PathVariable String userId) {
        Map<String, Object> dashboardData = new HashMap<>();
        
        // Get all user appointments
        List<AppointmentResponse> allAppointments = appointmentService.getUserAppointments(userId);
        
        // Get appointments by status
        List<AppointmentResponse> pendingAppointments = appointmentService.getAppointmentsByStatus(userId, "USER", Appointment.AppointmentStatus.PENDING);
        List<AppointmentResponse> confirmedAppointments = appointmentService.getAppointmentsByStatus(userId, "USER", Appointment.AppointmentStatus.CONFIRMED);
        List<AppointmentResponse> completedAppointments = appointmentService.getAppointmentsByStatus(userId, "USER", Appointment.AppointmentStatus.COMPLETED);
        
        // Dashboard summary
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalAppointments", allAppointments.size());
        summary.put("pendingAppointments", pendingAppointments.size());
        summary.put("confirmedAppointments", confirmedAppointments.size());
        summary.put("completedAppointments", completedAppointments.size());
        
        dashboardData.put("summary", summary);
        dashboardData.put("recentAppointments", allAppointments.stream().limit(5).toList());
        dashboardData.put("pendingAppointments", pendingAppointments);
        dashboardData.put("confirmedAppointments", confirmedAppointments);
        
        ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "User dashboard data retrieved successfully",
                ZonedDateTime.now(),
                dashboardData
        );
        
        return ResponseEntity.ok(apiResponse);
    }

    // Doctor Dashboard - Get summary and recent appointments
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDoctorDashboard(@PathVariable String doctorId) {
        Map<String, Object> dashboardData = new HashMap<>();
        
        // Get all doctor appointments
        List<AppointmentResponse> allAppointments = appointmentService.getDoctorAppointments(doctorId);
        
        // Get appointments by status
        List<AppointmentResponse> pendingAppointments = appointmentService.getAppointmentsByStatus(doctorId, "DOCTOR", Appointment.AppointmentStatus.PENDING);
        List<AppointmentResponse> confirmedAppointments = appointmentService.getAppointmentsByStatus(doctorId, "DOCTOR", Appointment.AppointmentStatus.CONFIRMED);
        List<AppointmentResponse> completedAppointments = appointmentService.getAppointmentsByStatus(doctorId, "DOCTOR", Appointment.AppointmentStatus.COMPLETED);
        
        // Dashboard summary
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalAppointments", allAppointments.size());
        summary.put("pendingAppointments", pendingAppointments.size());
        summary.put("confirmedAppointments", confirmedAppointments.size());
        summary.put("completedAppointments", completedAppointments.size());
        
        dashboardData.put("summary", summary);
        dashboardData.put("recentAppointments", allAppointments.stream().limit(5).toList());
        dashboardData.put("pendingAppointments", pendingAppointments);
        dashboardData.put("confirmedAppointments", confirmedAppointments);
        
        ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Doctor dashboard data retrieved successfully",
                ZonedDateTime.now(),
                dashboardData
        );
        
        return ResponseEntity.ok(apiResponse);
    }

    // Get user's upcoming appointments
    @GetMapping("/user/{userId}/upcoming")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getUserUpcomingAppointments(@PathVariable String userId) {
        List<AppointmentResponse> allAppointments = appointmentService.getUserAppointments(userId);
        
        // Filter for upcoming appointments (pending or confirmed)
        List<AppointmentResponse> upcomingAppointments = allAppointments.stream()
                .filter(appointment -> appointment.getStatus() == Appointment.AppointmentStatus.PENDING || 
                                     appointment.getStatus() == Appointment.AppointmentStatus.CONFIRMED)
                .toList();
        
        ApiResponse<List<AppointmentResponse>> apiResponse = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "User upcoming appointments retrieved successfully",
                ZonedDateTime.now(),
                upcomingAppointments
        );
        
        return ResponseEntity.ok(apiResponse);
    }

    // Get doctor's today's appointments
    @GetMapping("/doctor/{doctorId}/today")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getDoctorTodayAppointments(@PathVariable String doctorId) {
        List<AppointmentResponse> todayAppointments = appointmentService.getDoctorAppointmentsByDate(doctorId, java.time.LocalDate.now());
        
        ApiResponse<List<AppointmentResponse>> apiResponse = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Doctor today's appointments retrieved successfully",
                ZonedDateTime.now(),
                todayAppointments
        );
        
        return ResponseEntity.ok(apiResponse);
    }
}

