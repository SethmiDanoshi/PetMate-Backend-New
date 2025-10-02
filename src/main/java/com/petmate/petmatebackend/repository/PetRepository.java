package com.petmate.petmatebackend.repository;

import com.petmate.petmatebackend.model.Pet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends MongoRepository<Pet, String> {
    List<Pet> findBySellerId(String sellerId);
    List<Pet> findByTypeIgnoreCase(String type);
}
