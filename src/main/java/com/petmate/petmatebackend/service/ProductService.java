package com.petmate.petmatebackend.service;

import com.petmate.petmatebackend.model.Product;
import com.petmate.petmatebackend.repository.ProductRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;

    public ProductService(ProductRepository productRepository, CloudinaryService cloudinaryService) {
        this.productRepository = productRepository;
        this.cloudinaryService = cloudinaryService;
    }

    // Create Product
    public Product createProduct(Product product, List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("At least one image is required");
        }

        List<String> imageUrls = new ArrayList<>();
        List<String> publicIds = new ArrayList<>();

        for (MultipartFile image : images) {
            Map<String, String> uploadResult = cloudinaryService.uploadImage(image);
            imageUrls.add(uploadResult.get("url"));
            publicIds.add(uploadResult.get("publicId"));
        }

        product.setImageUrls(imageUrls);
        product.setImagePublicIds(publicIds);
        product.setCreatedAt(LocalDate.now());
        product.setUpdatedAt(LocalDate.now());

        return productRepository.save(product);

    }

    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    //  Get single product
    public Product getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));
    }

    //  Update product
    public Product updateProduct(String id, Product updatedProduct, List<MultipartFile> newImages) {
        Product existing = getProductById(id);

        existing.setProductName(updatedProduct.getProductName());
        existing.setBrand(updatedProduct.getBrand());
        existing.setPrice(updatedProduct.getPrice());
        existing.setCategory(updatedProduct.getCategory());
        existing.setQuantity(updatedProduct.getQuantity());
        existing.setDate(updatedProduct.getDate());
        existing.setDescription(updatedProduct.getDescription());
        existing.setUpdatedAt(LocalDate.now());

        // If new images provided, replace old ones
        if (newImages != null && !newImages.isEmpty()) {
            // delete old images
            existing.getImagePublicIds().forEach(cloudinaryService::deleteImage);

            List<String> newUrls = new ArrayList<>();
            List<String> newPublicIds = new ArrayList<>();

            for (MultipartFile image : newImages) {
                Map<String, String> uploadResult = cloudinaryService.uploadImage(image);
                newUrls.add(uploadResult.get("url"));
                newPublicIds.add(uploadResult.get("publicId"));
            }

            existing.setImageUrls(newUrls);
            existing.setImagePublicIds(newPublicIds);
        }

        return productRepository.save(existing);
    }

    //  Delete product
    public void deleteProduct(String id) {
        Product existing = getProductById(id);
        existing.getImagePublicIds().forEach(cloudinaryService::deleteImage);
        productRepository.deleteById(id);
    }

    // Get product by sellerID
    public List<Product> getProductsBySeller(String sellerId) {
        return productRepository.findBySellerId(sellerId);
    }

    //  Get products by category
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    // Get product by type
    public List<Product> getProductsByType(String type) {
        return productRepository.findByType(type);
    }
}