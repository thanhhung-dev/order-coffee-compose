package com.example.OrderCoffeeBE.Service.impl;

import com.example.OrderCoffeeBE.Entity.categories;
import com.example.OrderCoffeeBE.Service.CategoryService;
import com.example.OrderCoffeeBE.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<categories> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public categories findByIdCate(int id) {
        Optional<categories> categoryOptional = categoryRepository.findById(id);
        return categoryOptional.orElse(null);
    }

    @Override
    public categories createCate(categories categories) {
        return categoryRepository.save(categories);
    }

    @Override
    public categories updateCate(categories updateCategories) {
        categories currentCate = this.findByIdCate(updateCategories.getId());
        if (currentCate == null) {
            return null;
        }
        currentCate.setName(updateCategories.getName());
        return this.categoryRepository.save(currentCate);
    }

    @Override
    public void deleteCate(int id) {
        this.categoryRepository.deleteById(id);
    }

    @Override
    public boolean isNameExist(String name) {
        return this.categoryRepository.existsByName(name);
    }
}