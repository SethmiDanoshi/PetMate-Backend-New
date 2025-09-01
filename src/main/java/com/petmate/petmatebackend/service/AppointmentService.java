package com.petmate.petmatebackend.service;

import com.petmate.petmatebackend.dto.AppointmentRequest;
import com.petmate.petmatebackend.dto.AppointmentResponse;
import com.petmate.petmatebackend.dto.AppointmentStatusUpdateRequest;
import com.petmate.petmatebackend.exception.AppointmentConflictException;
import com.petmate.petmatebackend.exception.AppointmentNotFoundException;
import com.petmate.petmatebackend.exception.UnauthorizedAccessException;
import com.petmate.petmatebackend.model.Appointment;
import com.petmate.petmatebackend.model.Doctor;
import com.petmate.petmatebackend.model.User;
import com.petmate.petmatebackend.repository.AppointmentRepository;
import com.petmate.petmatebackend.repository.DoctorRepository;
import com.petmate.petmatebackend.repository.UserRepository;
import com.petmate.petmatebackend.util.AppointmentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentResponse createAppointment(String userId, AppointmentRequest request) {
        // Validate appointment request
        AppointmentValidator.validateAppointmentRequest(request);
        AppointmentValidator.validateAppointmentTime(request.getDate(), request.getTime());
        
        // Check if user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if doctor exists
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Check for conflicting appointments
        List<Appointment> conflictingAppointments = appointmentRepository
                .findConflictingAppointments(request.getDoctorId(), request.getDate(), request.getTime().toString());
        
        if (!conflictingAppointments.isEmpty()) {
            throw new AppointmentConflictException("Doctor has a conflicting appointment at this time");
        }

        Appointment appointment = Appointment.builder()
                .doctorId(request.getDoctorId())
                .userId(userId)
                .date(request.getDate())
                .time(request.getTime())
                .symptoms(request.getSymptoms())
                .status(Appointment.AppointmentStatus.PENDING)
                .appointmentType(request.getAppointmentType())
                .petType(request.getPetType())
                .userContactNumber(request.getUserContactNumber())
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return mapToResponse(savedAppointment, user, doctor);
    }

    public List<AppointmentResponse> getUserAppointments(String userId) {
        List<Appointment> appointments = appointmentRepository.findByUserId(userId);
        return appointments.stream()
                .map(this::mapToResponseWithDetails)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> getDoctorAppointments(String doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        return appointments.stream()
                .map(this::mapToResponseWithDetails)
                .collect(Collectors.toList());
    }

    public AppointmentResponse getAppointmentById(String appointmentId, String userId, String userRole) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));

        // Check if user has access to this appointment
        if (!appointment.getUserId().equals(userId) && !appointment.getDoctorId().equals(userId)) {
            throw new UnauthorizedAccessException("You don't have access to this appointment");
        }

        return mapToResponseWithDetails(appointment);
    }

    public AppointmentResponse updateAppointmentStatus(String appointmentId, String doctorId, AppointmentStatusUpdateRequest request) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));

        // Check if the doctor is authorized to update this appointment
        if (!appointment.getDoctorId().equals(doctorId)) {
            throw new UnauthorizedAccessException("You can only update your own appointments");
        }

        appointment.setStatus(request.getStatus());
        appointment.setNotes(request.getNotes());
        appointment.setUpdatedAt(LocalDate.now());

        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return mapToResponseWithDetails(updatedAppointment);
    }

    public List<AppointmentResponse> getAppointmentsByStatus(String userId, String userRole, Appointment.AppointmentStatus status) {
        List<Appointment> appointments;
        if ("DOCTOR".equals(userRole)) {
            appointments = appointmentRepository.findByDoctorIdAndStatus(userId, status);
        } else {
            appointments = appointmentRepository.findByUserIdAndStatus(userId, status);
        }
        
        return appointments.stream()
                .map(this::mapToResponseWithDetails)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> getDoctorAppointmentsByDate(String doctorId, LocalDate date) {
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndDate(doctorId, date);
        return appointments.stream()
                .map(this::mapToResponseWithDetails)
                .collect(Collectors.toList());
    }

    private AppointmentResponse mapToResponse(Appointment appointment, User user, Doctor doctor) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setDoctorId(appointment.getDoctorId());
        response.setDoctorName(doctor.getName());
        response.setUserId(appointment.getUserId());
        response.setUserName(user.getFullName());
        response.setDate(appointment.getDate());
        response.setTime(appointment.getTime());
        response.setSymptoms(appointment.getSymptoms());
        response.setStatus(appointment.getStatus());
        response.setAppointmentType(appointment.getAppointmentType());
        response.setPetType(appointment.getPetType());
        response.setUserContactNumber(appointment.getUserContactNumber());
        response.setNotes(appointment.getNotes());
        response.setCreatedAt(appointment.getCreatedAt());
        response.setUpdatedAt(appointment.getUpdatedAt());
        return response;
    }

    private AppointmentResponse mapToResponseWithDetails(Appointment appointment) {
        User user = userRepository.findById(appointment.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Doctor doctor = doctorRepository.findById(appointment.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        
        return mapToResponse(appointment, user, doctor);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
}
