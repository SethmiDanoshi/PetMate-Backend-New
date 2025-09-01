package com.petmate.petmatebackend.service;

import com.petmate.petmatebackend.dto.DoctorAuthResponse;
import com.petmate.petmatebackend.dto.DoctorSignupResponse;
import com.petmate.petmatebackend.dto.DoctorLoginRequest;
import com.petmate.petmatebackend.exception.EmailAlreadyExistsException;
import com.petmate.petmatebackend.exception.InvalidCredentialsException;
import com.petmate.petmatebackend.exception.PasswordMismatchException;
import com.petmate.petmatebackend.model.Doctor;
import com.petmate.petmatebackend.repository.DoctorRepository;
import com.petmate.petmatebackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {
    
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CloudinaryService cloudinaryService;
    
    public DoctorSignupResponse register(String name, String email, String password, 
                                       String confirmPassword, String contactNumber, 
                                       String address, MultipartFile licensePdf) {
        
        if (doctorRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already taken.");
        }
        
        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException("Password did not match");
        }
        
        if (licensePdf == null || licensePdf.isEmpty()) {
            throw new IllegalArgumentException("License PDF is required");
        }
        
        // Upload PDF to Cloudinary
        Map<String, String> uploadResult = cloudinaryService.uploadPdf(licensePdf);
        
        Doctor doctor = Doctor.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .contactNumber(contactNumber)
                .address(address)
                .licensePdfUrl(uploadResult.get("url"))
                .licensePdfPublicId(uploadResult.get("publicId"))
                .isVerified(false) // Initially not verified
                .isActive(true)
                .role(Doctor.Role.DOCTOR)
                .build();
        
        doctorRepository.save(doctor);
        
        return new DoctorSignupResponse(
                doctor.getName(),
                doctor.getEmail(),
                doctor.getContactNumber(),
                doctor.getAddress(),
                doctor.isVerified(),
                "Doctor registration successful. Please wait for verification."
        );
    }
    
    public DoctorAuthResponse login(DoctorLoginRequest request) {
        Doctor doctor = doctorRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password."));
        
        if (!passwordEncoder.matches(request.getPassword(), doctor.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }
        
        if (!doctor.isActive()) {
            throw new InvalidCredentialsException("Account is deactivated.");
        }
        
        String token = jwtUtil.generateToken(doctor);
        
        return new DoctorAuthResponse(
                token,
                doctor.getName(),
                doctor.getEmail(),
                doctor.getContactNumber(),
                doctor.getAddress(),
                doctor.isVerified(),
                doctor.getRole()
        );
    }
    
    public Doctor getDoctorById(String id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }
    
    public Doctor getDoctorByEmail(String email) {
        return doctorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }
    
    public List<Doctor> getPendingDoctors() {
        return doctorRepository.findAll().stream()
                .filter(doctor -> !doctor.isVerified())
                .collect(Collectors.toList());
    }

    public List<Doctor> getVerifiedDoctors() {
        return doctorRepository.findAll().stream()
                .filter(Doctor::isVerified)
                .collect(Collectors.toList());
    }
    
    public void verifyDoctor(String id) {
        Doctor doctor = getDoctorById(id);
        doctor.setVerified(true);
        doctorRepository.save(doctor);
    }
    
    public void rejectDoctor(String id, String reason) {
        Doctor doctor = getDoctorById(id);
        // Delete the PDF from Cloudinary
        if (doctor.getLicensePdfPublicId() != null) {
            cloudinaryService.deletePdf(doctor.getLicensePdfPublicId());
        }
        // Delete the doctor from database
        doctorRepository.deleteById(id);
    }


}
