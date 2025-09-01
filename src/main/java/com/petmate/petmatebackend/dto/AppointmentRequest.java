package com.petmate.petmatebackend.dto;

import com.petmate.petmatebackend.model.Appointment.AppointmentType;
import com.petmate.petmatebackend.model.Appointment.PetType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequest {
    private String doctorId;
    private LocalDate date;
    private LocalTime time;
    private String symptoms;
    private AppointmentType appointmentType;
    private PetType petType;
    private String userContactNumber;
}

