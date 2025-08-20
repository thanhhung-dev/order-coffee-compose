package com.example.OrderCoffeeBE.repository;

import com.example.OrderCoffeeBE.Entity.orders;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    
    // Find orders by status and not deleted, ordered by creation date
    @Query("SELECT o FROM orders o WHERE o.status = :status AND o.deleted = :deleted ORDER BY o.createdAt DESC")
    List<orders> findByStatusAndDeletedOrderByCreatedAtDesc(@Param("status") String status, @Param("deleted") int deleted);
    
    // Find orders by table
    @Query("SELECT o FROM orders o WHERE o.table_id = :tableId AND o.deleted = 0")
    List<orders> findByTableIdAndNotDeleted(@Param("tableId") Integer tableId);
    
    // Find orders by customer
    @Query("SELECT o FROM orders o WHERE o.customer_id = :customerId AND o.deleted = 0")
    List<orders> findByCustomerIdAndNotDeleted(@Param("customerId") Integer customerId);
}
