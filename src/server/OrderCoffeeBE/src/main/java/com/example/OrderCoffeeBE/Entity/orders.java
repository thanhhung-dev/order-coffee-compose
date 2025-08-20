package com.example.OrderCoffeeBE.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "orders")
@DynamicUpdate
public class orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "table_id", nullable = false)
    private Integer table_id;
    
    @Column(name = "customer_id")
    private Integer customer_id;
    
    private String status;
    
    @Column(name = "total_amount", nullable = false)
    private int total_amount;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @CreationTimestamp
    private LocalDateTime updatedAt;
    
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int deleted;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<orders_items> items;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    @JsonIgnore
    private customers customer;
}
