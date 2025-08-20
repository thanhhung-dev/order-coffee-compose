package com.example.OrderCoffeeBE.Controller;

import com.example.OrderCoffeeBE.Entity.Request.PostOrderRequest;
import com.example.OrderCoffeeBE.Entity.orders;
import com.example.OrderCoffeeBE.Service.impl.OrderServiceImpl;
import com.example.OrderCoffeeBE.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Orders", description = "Order management operations")
public class OrderController {
    private final OrderServiceImpl orderService;

    @GetMapping
    @Operation(summary = "Get all orders", description = "Retrieve all orders in the system")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No orders found")
    })
    public ResponseEntity<ApiResponse<List<orders>>> getAllOrder() {
        List<orders> orders = orderService.findAll();
        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("No Order found"));
        }
        return ResponseEntity.ok(ApiResponse.success("Get Order Success", orders));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieve a specific order by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<ApiResponse<orders>> getOrderById(
            @Parameter(description = "Order ID", required = true) @PathVariable int id) {
        orders fetchCategory = this.orderService.findById(id);
        if (fetchCategory == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Order not found " + id));
        }
        return ResponseEntity.ok(ApiResponse.success("Get Order Success", fetchCategory));
    }


    @PostMapping
    @Operation(summary = "Create new order", description = "Create a new order with automatic price calculation")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Order created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid order data")
    })
    public ResponseEntity<ApiResponse<orders>> createOrder(@RequestBody PostOrderRequest order) {
        try {
            orders newOrder = orderService.createOrder(order);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Create Order Success", newOrder));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error creating order: " + e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update order", description = "Update an existing order")
    public ResponseEntity<ApiResponse<orders>> updateOrder(
            @Parameter(description = "Order ID", required = true) @PathVariable int id, 
            @RequestBody PostOrderRequest order) {
        try {
            order.setId(id);
            orders updatedOrder = orderService.updateOrder(order);
            if (updatedOrder != null) {
                return ResponseEntity.ok(ApiResponse.success("Update Order Success", updatedOrder));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Order not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Error updating order: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order", description = "Soft delete an order")
    public ResponseEntity<ApiResponse<orders>> deleteOrder(
            @Parameter(description = "Order ID", required = true) @PathVariable int id) {
        try {
            orders currentOrder = this.orderService.findById(id);
            this.orderService.sortDeleteOrder(id);
            return ResponseEntity.ok(ApiResponse.success("Delete Order Success", currentOrder));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Order not found"));
        }
    }
    
    @PatchMapping("/{id}/status")
    @Operation(summary = "Update order status", description = "Update only the status of an order with workflow validation")
    public ResponseEntity<ApiResponse<orders>> updateOrderStatus(
            @Parameter(description = "Order ID", required = true) @PathVariable int id,
            @Parameter(description = "New status", required = true) @RequestParam String status) {
        try {
            orders updatedOrder = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(ApiResponse.success("Update Order Status Success", updatedOrder));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Order not found"));
        }
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get orders by status", description = "Retrieve orders filtered by status")
    public ResponseEntity<ApiResponse<List<orders>>> getOrdersByStatus(
            @Parameter(description = "Order status", required = true) @PathVariable String status) {
        List<orders> orders = orderService.findByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Get Orders by Status Success", orders));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending orders", description = "Retrieve all pending orders for barista interface")
    public ResponseEntity<ApiResponse<List<orders>>> getPendingOrders() {
        List<orders> orders = orderService.getPendingOrders();
        return ResponseEntity.ok(ApiResponse.success("Get Pending Orders Success", orders));
    }

    @GetMapping("/in-progress")
    @Operation(summary = "Get in-progress orders", description = "Retrieve all in-progress orders for barista interface")
    public ResponseEntity<ApiResponse<List<orders>>> getInProgressOrders() {
        List<orders> orders = orderService.getInProgressOrders();
        return ResponseEntity.ok(ApiResponse.success("Get In-Progress Orders Success", orders));
    }
}
