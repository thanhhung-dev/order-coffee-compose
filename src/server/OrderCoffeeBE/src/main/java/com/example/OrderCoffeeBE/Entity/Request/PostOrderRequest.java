package com.example.OrderCoffeeBE.Entity.Request;

import lombok.Data;

import java.util.List;

@Data
public class PostOrderRequest {
    private int id;
    private Integer table_id;
    private Integer customer_id;
    private String status;
    private int totalAmount;
    private List<PostOrderItemRequest> items;
}
