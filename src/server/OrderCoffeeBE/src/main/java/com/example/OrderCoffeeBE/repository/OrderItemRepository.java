package com.example.OrderCoffeeBE.repository;

import com.example.OrderCoffeeBE.Entity.orders_items;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<orders_items, Integer> {
}
