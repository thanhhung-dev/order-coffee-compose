package com.example.OrderCoffeeBE.Controller;

import com.example.OrderCoffeeBE.Entity.Request.PostProductRequest;
import com.example.OrderCoffeeBE.Entity.categories;
import com.example.OrderCoffeeBE.Entity.products;
import com.example.OrderCoffeeBE.Service.impl.ProductServiceImpl;
import com.example.OrderCoffeeBE.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductController {
    private final ProductServiceImpl productService;
    public static String uploadDirectory = System.getProperty("user.dir") + "/access/products";


    @GetMapping
    public ResponseEntity<ApiResponse<List<products>>> getAllProducts() {
        List<products> products = productService.findAll();
        return ResponseEntity.ok(ApiResponse.success("GET Products success", products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<products>> getProductById(@PathVariable Integer id) {
        products findProduct = this.productService.findById(id);
        if (findProduct == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Product not found"));
        }
        return ResponseEntity.ok(ApiResponse.success("Get Product success", findProduct));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<products>> createProduct(@ModelAttribute PostProductRequest requestProduct, MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename();
        Path path = Paths.get(uploadDirectory, originalFilename);
        Files.write(path, image.getBytes());
        products product = new products();
        product.setName(requestProduct.getName());
        product.setDescription(requestProduct.getDescription());
        product.setPrice(requestProduct.getPrice());
        product.setStatus(requestProduct.getStatus());
        product.setCategory_id(requestProduct.getCategory_id());
        product.setImage(originalFilename);
        products saveProduct = productService.createProduct(product);
        return ResponseEntity.ok(ApiResponse.success("Created Product success", saveProduct));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<products>> updateProduct(@PathVariable int id, @ModelAttribute PostProductRequest updateProduct, MultipartFile image) throws IOException {
        updateProduct.setId(id);
        products updatedProduct = productService.updateProduct(updateProduct, image);
        return ResponseEntity.ok(ApiResponse.success("Updated Product success", updatedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Integer id) {
        products currentProduct = this.productService.findById(id);
        if (currentProduct != null) {
            this.productService.deleteProduct(currentProduct);
            return ResponseEntity.ok(ApiResponse.success("Delete Product success", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Product not found"));
        }
    }
}