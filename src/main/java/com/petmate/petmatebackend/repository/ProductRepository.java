package com.petmate.petmatebackend.repository;

import com.petmate.petmatebackend.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findBySellerId(String sellerId);
    List<Product> findByCategory(String category);
    List<Product> findByType(String type);

}
