package com.petmate.petmatebackend.util;

import com.petmate.petmatebackend.dto.AppointmentRequest;
import com.petmate.petmatebackend.exception.AppointmentConflictException;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentValidator {

    public static void validateAppointmentRequest(AppointmentRequest request) {

        if (request.getDoctorId() == null || request.getDoctorId().trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor ID is required");
        }
        
        if (request.getDate() == null) {
            throw new IllegalArgumentException("Appointment date is required");
        }
        
        if (request.getTime() == null) {
            throw new IllegalArgumentException("Appointment time is required");
        }
        
        if (request.getSymptoms() == null || request.getSymptoms().trim().isEmpty()) {
            throw new IllegalArgumentException("Symptoms are required");
        }
        
        if (request.getAppointmentType() == null) {
            throw new IllegalArgumentException("Appointment type is required");
        }
        
        if (request.getPetType() == null) {
            throw new IllegalArgumentException("Pet type is required");
        }
        
        if (request.getUserContactNumber() == null || request.getUserContactNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("User contact number is required");
        }
        
        // Validate date (cannot be in the past)
        if (request.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Appointment date cannot be in the past");
        }

        LocalTime appointmentTime = request.getTime();
        LocalTime businessStart = LocalTime.of(8, 0);
        LocalTime businessEnd = LocalTime.of(20, 0);
        
        if (appointmentTime.isBefore(businessStart) || appointmentTime.isAfter(businessEnd)) {
            throw new IllegalArgumentException("Appointments must be scheduled between 8:00 AM and 8:00 PM");
        }
    }
    
    public static void validateAppointmentTime(LocalDate date, LocalTime time) {

        LocalDate maxDate = LocalDate.now().plusMonths(3);
        if (date.isAfter(maxDate)) {
            throw new IllegalArgumentException("Appointments cannot be scheduled more than 3 months in advance");
        }

        if (date.getDayOfWeek().getValue() > 5) { // Saturday = 6, Sunday = 7
            throw new IllegalArgumentException("Appointments cannot be scheduled on weekends");
        }
    }
}


