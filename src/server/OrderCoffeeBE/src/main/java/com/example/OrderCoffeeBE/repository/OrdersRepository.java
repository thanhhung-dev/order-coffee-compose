package com.example.OrderCoffeeBE.repository;

import com.example.OrderCoffeeBE.Entity.orders;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrdersRepository extends JpaRepository<orders, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE orders o SET o.deleted = 1 WHERE o.id = :orderId")
    void softDeleteById(int orderId);

    // Fetch all active orders (not deleted)
    @Query("SELECT o FROM orders o WHERE o.deleted = 0")
    List<orders> findAllNotDeleted();

    // Fetch all soft-deleted orders
    @Query("SELECT o FROM orders o WHERE o.deleted = 1")
    List<orders> findAllDeleted();
}
