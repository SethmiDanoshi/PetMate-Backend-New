package com.petmate.petmatebackend.dto;

import com.petmate.petmatebackend.model.Appointment.AppointmentStatus;
import com.petmate.petmatebackend.model.Appointment.AppointmentType;
import com.petmate.petmatebackend.model.Appointment.PetType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentResponse {
    private String id;
    private String doctorId;
    private String doctorName;
    private String userId;
    private String userName;
    private LocalDate date;
    private LocalTime time;
    private String symptoms;
    private AppointmentStatus status;
    private AppointmentType appointmentType;
    private PetType petType;
    private String userContactNumber;
    private String notes;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}

