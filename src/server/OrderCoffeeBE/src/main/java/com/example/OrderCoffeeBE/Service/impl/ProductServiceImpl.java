package com.example.OrderCoffeeBE.Service.impl;

import com.example.OrderCoffeeBE.Entity.Request.PostProductRequest;
import com.example.OrderCoffeeBE.Entity.products;
import com.example.OrderCoffeeBE.Service.ProductService;
import com.example.OrderCoffeeBE.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.example.OrderCoffeeBE.Controller.ProductController.uploadDirectory;

@RequiredArgsConstructor
@Service("productService")
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<products> findAll() {
        return productRepository.findAll();
    }

    @Override
    public products findById(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));
    }

    @Override
    public products createProduct(products product) {
        return productRepository.save(product);
    }

    @Override
    public products updateProduct(PostProductRequest updateProduct, MultipartFile image) {
        products current = this.findById(updateProduct.getId());
        if (updateProduct.getName() != null) {
            current.setName(updateProduct.getName());
        }
        if (updateProduct.getPrice() != null) {
            current.setPrice(updateProduct.getPrice());
        }
        if (updateProduct.getDescription() != null) {
            current.setDescription(updateProduct.getDescription());
        }
        if (updateProduct.getCategory_id() != null) {
            current.setCategory_id(updateProduct.getCategory_id());
        }
        if (updateProduct.getStatus() != null) {
            current.setStatus(updateProduct.getStatus());
        }
        if (image != null && !image.isEmpty()) {
            try {
                String fileName = image.getOriginalFilename();
                Path path = Paths.get(uploadDirectory, fileName);
                Files.write(path, image.getBytes());
                current.setImage(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Error Save Image: " + e.getMessage());
            }
        }
        return productRepository.save(current);
    }




    @Override
    public void deleteProduct(products product) {
        productRepository.delete(product);
    }
}
