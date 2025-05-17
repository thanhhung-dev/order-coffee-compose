package com.example.OrderCoffeeBE.Controller;

import com.example.OrderCoffeeBE.Entity.categories;
import com.example.OrderCoffeeBE.Entity.tables;
import com.example.OrderCoffeeBE.Service.impl.TableServiceImpl;
import com.example.OrderCoffeeBE.repository.TableRepository;
import com.example.OrderCoffeeBE.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/table")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TableController {
    private final TableServiceImpl tableService;
    @GetMapping
    public ResponseEntity<ApiResponse<List<tables>>> getTables() {
        List<tables> TableEntities = tableService.findAll();
        return ResponseEntity.ok(ApiResponse.success("GET Tables success", TableEntities));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<tables>> getTableById(@PathVariable int id) {
       tables findTables = tableService.findById(id);
       if(findTables == null) {
           return ResponseEntity.ok(ApiResponse.error("Table not found"));
       }
        return ResponseEntity.ok(ApiResponse.success("GET Table success", findTables));
    }
    @PostMapping
    public ResponseEntity<ApiResponse<tables>> addTable(@RequestBody tables tables) {
        tables newTables = this.tableService.createTable(tables);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Create Category Success", newTables));
    }
    @PatchMapping()
    public ResponseEntity<ApiResponse<tables>> updateTable(@PathVariable int id, @RequestBody tables table) {
        table.setId(id);
        tables hungTable = this.tableService.updateTable(table);
        if (hungTable == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Category not found"));
        }
        return ResponseEntity.ok(ApiResponse.success("Update Category Success", hungTable));
    }
    @DeleteMapping()
    public ResponseEntity<ApiResponse<tables>> deleteTable(@RequestParam int id) {
        tables currentTables = this.tableService.findById(id);
        if (currentTables != null) {
            this.tableService.deleteTable(id);
            return ResponseEntity.ok(ApiResponse.success("Delete Product success", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Product not found"));
        }
    }
}
