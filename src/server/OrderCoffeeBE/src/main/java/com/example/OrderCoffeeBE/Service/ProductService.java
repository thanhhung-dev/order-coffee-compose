package com.example.OrderCoffeeBE.Service;

import com.example.OrderCoffeeBE.Entity.Request.PostProductRequest;
import com.example.OrderCoffeeBE.Entity.products;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
public interface ProductService {
    List<products> findAll();
    products findById(int id);
    products createProduct(products products);
    products updateProduct(PostProductRequest products, MultipartFile image);
    void deleteProduct(products product);
}
