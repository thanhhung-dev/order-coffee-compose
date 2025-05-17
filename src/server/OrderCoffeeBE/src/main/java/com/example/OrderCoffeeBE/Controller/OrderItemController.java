package com.example.OrderCoffeeBE.Controller;

import com.example.OrderCoffeeBE.Entity.orders_items;
import com.example.OrderCoffeeBE.Service.impl.OrderItemServiceImpl;
import com.example.OrderCoffeeBE.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/orderItems")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderItemController {
    private final OrderItemServiceImpl orderItemService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<orders_items>>> getAllOrderItems() {
        List<orders_items> items = orderItemService.findAll();
        if (items.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("No order items found"));
        }
        return ResponseEntity.ok(ApiResponse.success("Fetched all order items successfully", items));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<orders_items>> getOrderItemById(@PathVariable int id) {
        try {
            orders_items orderItem = orderItemService.findOrderItemById(id); // Tìm một item cụ thể
            return ResponseEntity.ok(ApiResponse.success("Fetched order item successfully",orderItem));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Order item not found with ID: " + id));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<orders_items>> createOrderItem(@RequestBody orders_items orderItem) {
        orders_items newOrderItem = orderItemService.createOrderItem(orderItem);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order item created successfully", newOrderItem));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<orders_items>> updateOrderItem(@PathVariable int id, @RequestBody orders_items orderItem) {
        orderItem.setId(id);
        orders_items updatedOrderItem = orderItemService.updateOrderItem(orderItem);
        if (updatedOrderItem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Order item not found with ID: " + id));
        }
        return ResponseEntity.ok(ApiResponse.success("Order item updated successfully", updatedOrderItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteOrderItem(@PathVariable int id) {
        try {
            orderItemService.deleteOrderItem(id);
            return ResponseEntity.ok(ApiResponse.success("Order item deleted successfully", "Order item ID: " + id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Order item not found with ID: " + id));
        }
    }
}