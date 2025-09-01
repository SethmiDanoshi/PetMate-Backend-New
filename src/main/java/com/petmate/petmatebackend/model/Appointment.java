package com.petmate.petmatebackend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "appointments")
public class Appointment {
    @Id
    private String id;
    
    private String doctorId;
    private String userId;
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

    public enum AppointmentStatus {
        PENDING,
        CONFIRMED,
        CANCELLED,
        COMPLETED
    }

    public enum AppointmentType {
        IN_CLINIC,
        HOME_VISIT,
        ONLINE
    }

    public enum PetType {
        DOG,
        CAT,
        BIRD,
        FISH,
        RABBIT,
        HAMSTER,
        OTHER
    }
}

