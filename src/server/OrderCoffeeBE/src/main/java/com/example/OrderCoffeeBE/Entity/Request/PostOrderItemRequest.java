package com.example.OrderCoffeeBE.Entity.Request;

import lombok.Data;

@Data
public class PostOrderItemRequest {
    private Integer product_id;
    private int quantity;
    private int subtotal;
    private Integer status;
    private String notes;
}
