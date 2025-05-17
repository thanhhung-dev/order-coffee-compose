package com.example.OrderCoffeeBE.Controller;

import com.example.OrderCoffeeBE.Entity.Request.PostOrderRequest;
import com.example.OrderCoffeeBE.Entity.orders;
import com.example.OrderCoffeeBE.Service.impl.OrderServiceImpl;
import com.example.OrderCoffeeBE.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {
    private final OrderServiceImpl orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<orders>>> getAllOrder() {
        List<orders> orders = orderService.findAll();
        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("No Order found"));
        }
        return ResponseEntity.ok(ApiResponse.success("Get Order Success", orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<orders>> getOrderById(@PathVariable int id) {
        orders fetchCategory = this.orderService.findById(id);
        if (fetchCategory == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Order not found " + id));
        }
        return ResponseEntity.ok(ApiResponse.success("Get Order Success", fetchCategory));
    }


    @PostMapping
    public ResponseEntity<ApiResponse<orders>> createOrder(@RequestBody PostOrderRequest order) {
        orders newOrders = this.orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Create Orders Success", newOrders));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<orders>> updateOrder(@PathVariable int id, @RequestBody PostOrderRequest order) {
        order.setId(id);
        orders hungOrders = this.orderService.updateOrder(order);
        if (hungOrders == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Order not found"));
        }
        return ResponseEntity.ok(ApiResponse.success("Update Order Success", hungOrders));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<orders>> deleteOrder(@PathVariable int id) {
        orders currentOrder = this.orderService.findById(id);
        if (currentOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Order not found" + id));
        }
        this.orderService.sortDeleteOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Delete Order Success", currentOrder));
    }
}
