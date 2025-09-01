package com.petmate.petmatebackend.repository;

import com.petmate.petmatebackend.model.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    
    List<Appointment> findByUserId(String userId);
    
    List<Appointment> findByDoctorId(String doctorId);
    
    List<Appointment> findByDoctorIdAndDate(String doctorId, LocalDate date);
    
    List<Appointment> findByUserIdAndStatus(String userId, Appointment.AppointmentStatus status);
    
    List<Appointment> findByDoctorIdAndStatus(String doctorId, Appointment.AppointmentStatus status);
    
    @Query("{'doctorId': ?0, 'date': ?1, 'time': ?2, 'status': {$ne: 'CANCELLED'}}")
    List<Appointment> findConflictingAppointments(String doctorId, LocalDate date, String time);
}

