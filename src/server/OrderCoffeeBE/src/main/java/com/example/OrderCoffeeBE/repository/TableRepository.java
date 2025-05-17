package com.example.OrderCoffeeBE.repository;

import com.example.OrderCoffeeBE.Entity.tables;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<tables, Integer> {
}
