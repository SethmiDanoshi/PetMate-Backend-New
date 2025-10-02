package com.petmate.petmatebackend.controller;

import com.petmate.petmatebackend.model.Product;
import com.petmate.petmatebackend.payload.ApiResponse;
import com.petmate.petmatebackend.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Create Product
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Product>> createProduct(
            @RequestPart("product") Product product,
            @RequestPart("images")List<MultipartFile> images
            ){
        Product saved = productService.createProduct(product,images);
        return ResponseEntity.ok(new ApiResponse<>(true, 201, "Product created successfullty", ZonedDateTime.now(),saved));
    }
    // Get all products
    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(
                            false,
                            HttpStatus.NOT_FOUND.value(),
                            "No any product in db" ,
                            ZonedDateTime.now(),
                            null
                    )
            );
        }
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Products retrieved successfully", ZonedDateTime.now(), products));
    }

    //  Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable String id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Product retrieved successfully", ZonedDateTime.now(), product));
    }

    //  Update product
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable String id,
            @RequestPart("product") Product updatedProduct,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        Product updated = productService.updateProduct(id, updatedProduct, images);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Product updated successfully", ZonedDateTime.now(), updated));
    }

    //  Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Product deleted successfully", ZonedDateTime.now(), null));
    }

    // Get products by sellerId
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsBySeller(@PathVariable String sellerId) {
        List<Product> products = productService.getProductsBySeller(sellerId);

        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(
                            false,
                            HttpStatus.NOT_FOUND.value(),
                            "No product found for this seller : " + sellerId,
                            ZonedDateTime.now(),
                            null
                    )
            );
        }

        return ResponseEntity.ok(
                new ApiResponse<>(true, 200, "Products by seller retrieved successfully",
                        ZonedDateTime.now(), products)
        );
    }

    // Get products by category
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);

        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(
                            false,
                            HttpStatus.NOT_FOUND.value(),
                            "No product found for category: " + category,
                            ZonedDateTime.now(),
                            null
                    )
            );
        }

        return ResponseEntity.ok(
                new ApiResponse<>(true, 200, "Products by category retrieved successfully",
                        ZonedDateTime.now(), products)
        );
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByType(@PathVariable String type) {
        List<Product> products = productService.getProductsByType(type);
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(
                            false,
                            HttpStatus.NOT_FOUND.value(),
                            "No products found for type: " + type,
                            ZonedDateTime.now(),
                            null
                    )
            );
        }
        return ResponseEntity.ok(
                new ApiResponse<>(true, 200, "Products by type retrieved successfully",
                        ZonedDateTime.now(), products)
        );
    }
}
