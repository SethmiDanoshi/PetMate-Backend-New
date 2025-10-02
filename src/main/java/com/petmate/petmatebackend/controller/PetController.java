package com.petmate.petmatebackend.controller;

import com.petmate.petmatebackend.dto.PetRequest;
import com.petmate.petmatebackend.model.Pet;

import com.petmate.petmatebackend.payload.ApiResponse;
import com.petmate.petmatebackend.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    // Create pet
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Pet>> createPet(
            @RequestPart("pet") PetRequest petRequest,
            @RequestPart("images") List<MultipartFile> images) {

        Pet created = petService.createPet(petRequest, images);
        ApiResponse<Pet> resp = new ApiResponse<>(true, 201, "Pet created successfully", ZonedDateTime.now(), created);
        return ResponseEntity.status(201).body(resp);
    }

    // Get all pets
    @GetMapping
    public ResponseEntity<ApiResponse<List<Pet>>> getAllPets() {
        List<Pet> list = petService.getAllPets();
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Pets retrieved", ZonedDateTime.now(), list));
    }

    // Get pet by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Pet>> getPetById(@PathVariable String id) {
        Pet pet = petService.getPetById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Pet retrieved", ZonedDateTime.now(), pet));
    }

    // Update
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Pet>> updatePet(
            @PathVariable String id,
            @RequestPart("pet") PetRequest petRequest,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        Pet updated = petService.updatePet(id, petRequest, images);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Pet updated", ZonedDateTime.now(), updated));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePet(@PathVariable String id) {
        petService.deletePet(id);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Pet deleted", ZonedDateTime.now(), null));
    }

    // Get pets by seller
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<ApiResponse<List<Pet>>> getBySeller(@PathVariable String sellerId) {
        List<Pet> list = petService.getPetsBySeller(sellerId);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Seller pets retrieved", ZonedDateTime.now(), list));
    }

    // Get pets by type/category
    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<Pet>>> getByType(@PathVariable String type) {
        List<Pet> list = petService.getPetsByType(type);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Pets by type retrieved", ZonedDateTime.now(), list));
    }
}
