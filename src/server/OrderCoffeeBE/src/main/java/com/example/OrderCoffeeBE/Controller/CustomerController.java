package com.example.OrderCoffeeBE.Controller;

import com.example.OrderCoffeeBE.Entity.customers;
import com.example.OrderCoffeeBE.Service.impl.CustomerServiceImpl;
import com.example.OrderCoffeeBE.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Customers", description = "Customer management operations")
public class CustomerController {
    private final CustomerServiceImpl customerService;

    @GetMapping
    @Operation(summary = "Get all customers", description = "Retrieve all customers in the system")
    public ResponseEntity<ApiResponse<List<customers>>> getAllCustomers() {
        List<customers> customers = customerService.findAll();
        return ResponseEntity.ok(ApiResponse.success("Get customers success", customers));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID", description = "Retrieve a specific customer by their ID")
    public ResponseEntity<ApiResponse<customers>> getCustomerById(
            @Parameter(description = "Customer ID", required = true) @PathVariable int id) {
        try {
            customers customer = customerService.findById(id);
            return ResponseEntity.ok(ApiResponse.success("Get customer success", customer));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Customer not found"));
        }
    }

    @PostMapping
    @Operation(summary = "Create new customer", description = "Create a new customer in the system")
    public ResponseEntity<ApiResponse<customers>> createCustomer(@RequestBody customers customer) {
        try {
            customers newCustomer = customerService.createCustomer(customer);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Create customer success", newCustomer));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer", description = "Update an existing customer")
    public ResponseEntity<ApiResponse<customers>> updateCustomer(
            @Parameter(description = "Customer ID", required = true) @PathVariable int id,
            @RequestBody customers customer) {
        try {
            customer.setId(id);
            customers updatedCustomer = customerService.updateCustomer(customer);
            return ResponseEntity.ok(ApiResponse.success("Update customer success", updatedCustomer));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Customer not found"));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer", description = "Delete a customer from the system")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(
            @Parameter(description = "Customer ID", required = true) @PathVariable int id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.ok(ApiResponse.success("Delete customer success", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Customer not found"));
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search customers by name", description = "Search for customers by name")
    public ResponseEntity<ApiResponse<List<customers>>> searchCustomers(
            @Parameter(description = "Name to search for") @RequestParam String name) {
        List<customers> customers = customerService.searchByName(name);
        return ResponseEntity.ok(ApiResponse.success("Search customers success", customers));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Find customer by email", description = "Find a customer by their email address")
    public ResponseEntity<ApiResponse<customers>> getCustomerByEmail(
            @Parameter(description = "Customer email", required = true) @PathVariable String email) {
        customers customer = customerService.findByEmail(email);
        if (customer != null) {
            return ResponseEntity.ok(ApiResponse.success("Get customer success", customer));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Customer not found"));
        }
    }
}