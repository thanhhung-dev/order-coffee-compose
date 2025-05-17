package com.example.OrderCoffeeBE.Controller;

import com.example.OrderCoffeeBE.Entity.categories;
import com.example.OrderCoffeeBE.Service.impl.CategoryServiceImpl;
import com.example.OrderCoffeeBE.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoryController {
    private final CategoryServiceImpl categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<categories>>> getAllCategories() {
        List<categories> categories = categoryService.getAllCategories();
        if(categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("No categories found"));
        }
        return ResponseEntity.ok(ApiResponse.success("Get Category Success", categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<categories>> getCategoryById(@PathVariable int id) {
        categories fetchCategory = this.categoryService.findByIdCate(id);
        if(fetchCategory == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Category not found " + id));
        }
        return ResponseEntity.ok(ApiResponse.success("Get Category Success", fetchCategory));
    }



    @PostMapping
    public ResponseEntity<ApiResponse<categories>> createCategory(@RequestBody categories category) {
        //check Name
        boolean isNameExist = this.categoryService.isNameExist(category.getName());
        if (isNameExist) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Category name already exists"));
        }
        categories newCategory = this.categoryService.createCate(category);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Create Category Success", newCategory));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<categories>> updateCategory(@PathVariable int id, @RequestBody categories category)  {
        category.setId(id);
        categories hungCategory = this.categoryService.updateCate(category);
        if (hungCategory == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Category not found"));
        }
        return ResponseEntity.ok(ApiResponse.success("Update Category Success", hungCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<categories>> deleteCategory(@PathVariable int id) {
        categories currentCategory = this.categoryService.findByIdCate(id);
        if (currentCategory == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Category not found" + id));
        }
        this.categoryService.deleteCate(id);
        return ResponseEntity.ok(ApiResponse.success("Delete Category Success", currentCategory));
    }
}