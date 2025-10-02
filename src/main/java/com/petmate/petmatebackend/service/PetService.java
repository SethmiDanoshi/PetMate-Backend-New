package com.petmate.petmatebackend.service;

import com.petmate.petmatebackend.dto.PetRequest;
import com.petmate.petmatebackend.model.Pet;
import com.petmate.petmatebackend.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final CloudinaryService cloudinaryService;

    public Pet createPet(PetRequest request, List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("At least one image is required");
        }

        List<String> urls = new ArrayList<>();
        List<String> publicIds = new ArrayList<>();

        for (MultipartFile img : images) {
            Map<String, String> upload = cloudinaryService.uploadImage(img);
            urls.add(upload.get("url"));
            publicIds.add(upload.get("publicId"));
        }

        Pet pet = Pet.builder()
                .name(request.getName())
                .type(request.getType())
                .breed(request.getBreed())
                .age(request.getAge())
                .weight(request.getWeight())
                .location(request.getLocation())
                .price(request.getPrice() == null ? 0.0 : request.getPrice())
                .description(request.getDescription())
                .imageUrls(urls)
                .imagePublicIds(publicIds)
                .sellerName(request.getSellerName())
                .sellerId(request.getSellerId())
                .address(request.getAddress())
                .contactNumber(request.getContactNumber())
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();

        return petRepository.save(pet);
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Pet getPetById(String id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pet not found with id: " + id));
    }

    public Pet updatePet(String id, PetRequest request, List<MultipartFile> newImages) {
        Pet existing = getPetById(id);

        existing.setName(request.getName());
        existing.setType(request.getType());
        existing.setBreed(request.getBreed());
        existing.setAge(request.getAge());
        existing.setWeight(request.getWeight());
        existing.setLocation(request.getLocation());
        existing.setPrice(request.getPrice() == null ? existing.getPrice() : request.getPrice());
        existing.setDescription(request.getDescription());
        existing.setSellerName(request.getSellerName());
        existing.setSellerId(request.getSellerId());
        existing.setAddress(request.getAddress());
        existing.setContactNumber(request.getContactNumber());
        existing.setUpdatedAt(LocalDate.now());

        if (newImages != null && !newImages.isEmpty()) {
            // delete old images (best-effort)
            if (existing.getImagePublicIds() != null) {
                existing.getImagePublicIds().forEach(cloudinaryService::deleteImage);
            }

            List<String> urls = new ArrayList<>();
            List<String> publicIds = new ArrayList<>();
            for (MultipartFile f : newImages) {
                Map<String, String> up = cloudinaryService.uploadImage(f);
                urls.add(up.get("url"));
                publicIds.add(up.get("publicId"));
            }
            existing.setImageUrls(urls);
            existing.setImagePublicIds(publicIds);
        }

        return petRepository.save(existing);
    }

    public void deletePet(String id) {
        Pet existing = getPetById(id);
        if (existing.getImagePublicIds() != null) {
            existing.getImagePublicIds().forEach(cloudinaryService::deleteImage);
        }
        petRepository.deleteById(id);
    }

    public List<Pet> getPetsBySeller(String sellerId) {
        return petRepository.findBySellerId(sellerId);
    }

    public List<Pet> getPetsByType(String type) {
        return petRepository.findByTypeIgnoreCase(type);
    }
}

