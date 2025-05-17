package com.example.OrderCoffeeBE.Service;

import com.example.OrderCoffeeBE.Entity.categories;

import java.util.List;

public interface CategoryService {
     List<categories> getAllCategories();
     categories findByIdCate(int id);
     categories createCate(categories categories);
     categories updateCate(categories categories);
     void deleteCate(int id);
     boolean isNameExist(String name);
}
