package com.example.OrderCoffeeBE.repository;

import com.example.OrderCoffeeBE.Entity.customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<customers, Integer> {
    
    Optional<customers> findByEmail(String email);
    
    List<customers> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT c FROM customers c WHERE c.phone = :phone")
    Optional<customers> findByPhone(String phone);
    
    boolean existsByEmail(String email);
}