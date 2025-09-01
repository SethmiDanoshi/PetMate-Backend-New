package com.petmate.petmatebackend.dto;

import com.petmate.petmatebackend.model.Appointment.AppointmentStatus;
import lombok.Data;

@Data
public class AppointmentStatusUpdateRequest {
    private AppointmentStatus status;
    private String notes;
}

