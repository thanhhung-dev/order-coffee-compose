package com.example.OrderCoffeeBE.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "customers")
public class customers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "email", unique = true)
    private String email;
    
    @Column(name = "phone", length = 20)
    private String phone;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @CreationTimestamp
    private LocalDateTime updatedAt;
}