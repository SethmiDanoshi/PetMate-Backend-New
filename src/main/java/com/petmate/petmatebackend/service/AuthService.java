package com.petmate.petmatebackend.service;

import com.petmate.petmatebackend.dto.AuthResponse;
import com.petmate.petmatebackend.dto.SignUpResponse;
import com.petmate.petmatebackend.dto.UserLoginRequest;
import com.petmate.petmatebackend.dto.UserSignupRequest;
import com.petmate.petmatebackend.exception.EmailAlreadyExistsException;
import com.petmate.petmatebackend.exception.InvalidCredentialsException;
import com.petmate.petmatebackend.exception.PasswordMismatchException;
import com.petmate.petmatebackend.model.User;
import com.petmate.petmatebackend.repository.UserRepository;
import com.petmate.petmatebackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public SignUpResponse register(UserSignupRequest request){
        if (userRepo.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException("Email already taken.");
        }
        if(!request.getPassword().equals(request.getConfirmPassword())){
            throw new PasswordMismatchException("Password did not match");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(request.getRole())
                .termsAccepted(request.isTermsAccepted())
                .build();

        userRepo.save(user);
        return new SignUpResponse(user.getFullName(), user.getEmail());
    }
    public AuthResponse login(UserLoginRequest request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(user.getId(), token, user.getFullName(), user.getEmail(), user.getRole());
    }
}
