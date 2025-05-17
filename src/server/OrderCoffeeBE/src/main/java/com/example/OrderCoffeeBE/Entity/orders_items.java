package com.example.OrderCoffeeBE.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "orders_items")
public class orders_items {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private orders order;
    @Column(name ="product_id", nullable = false)
    private Integer product_id;
    private int quantity;
    private int subtotal;
    private Integer status;
    private String notes;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
