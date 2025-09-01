package com.petmate.petmatebackend.repository;

import com.petmate.petmatebackend.model.User;

import java.util.Optional;

public interface CustomUserRepository {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
