package com.example.OrderCoffeeBE.Service.impl;

import com.example.OrderCoffeeBE.Entity.Request.PostOrderItemRequest;
import com.example.OrderCoffeeBE.Entity.Request.PostOrderRequest;
import com.example.OrderCoffeeBE.Entity.enums.OrderStatus;
import com.example.OrderCoffeeBE.Entity.orders;
import com.example.OrderCoffeeBE.Entity.orders_items;
import com.example.OrderCoffeeBE.Entity.products;
import com.example.OrderCoffeeBE.Entity.tables;
import com.example.OrderCoffeeBE.Service.OrderService;
import com.example.OrderCoffeeBE.repository.OrderItemRepository;
import com.example.OrderCoffeeBE.repository.OrdersRepository;
import com.example.OrderCoffeeBE.repository.ProductRepository;
import com.example.OrderCoffeeBE.repository.TableRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service("orderService")
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrdersRepository ordersRepository;
    private final OrderItemRepository orderItemRepository;
    private final TableRepository tableRepository;
    private final ProductRepository productRepository;
    @Override
    public List<orders> findAll() {
        return ordersRepository.findAllNotDeleted();
    }

    @Override
    @Transactional
    public orders createOrder(PostOrderRequest orderDTO) {
        // Enhanced validation
        validateOrderRequest(orderDTO);
        
        // Validate table exists and is available
        tables table = tableRepository.findById(orderDTO.getTable_id())
                .orElseThrow(() -> new IllegalArgumentException("Table not found with ID: " + orderDTO.getTable_id()));
        
        // Validate and calculate total amount
        int calculatedTotal = calculateOrderTotal(orderDTO.getItems());
        
        // Create order with proper defaults
        orders order = new orders();
        order.setTable_id(orderDTO.getTable_id());
        order.setCustomer_id(orderDTO.getCustomer_id());
        order.setStatus(orderDTO.getStatus() != null ? orderDTO.getStatus() : OrderStatus.PENDING.getStatus());
        order.setDeleted(0);
        order.setTotal_amount(calculatedTotal);
        
        orders savedOrder = ordersRepository.save(order);
        
        // Create order items with validation
        List<orders_items> orderItemsList = new ArrayList<>();
        for (PostOrderItemRequest itemDTO : orderDTO.getItems()) {
            // Validate product exists and is active
            products product = productRepository.findById(itemDTO.getProduct_id())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + itemDTO.getProduct_id()));
            
            if (product.getStatus() != 1) {
                throw new IllegalArgumentException("Product is not available: " + product.getName());
            }
            
            orders_items item = new orders_items();
            item.setOrder(savedOrder);
            item.setProduct_id(itemDTO.getProduct_id());
            item.setQuantity(itemDTO.getQuantity());
            item.setSubtotal(product.getPrice() * itemDTO.getQuantity());
            item.setStatus(1); // Active by default
            item.setNotes(itemDTO.getNotes());
            orderItemsList.add(item);
        }

        // Save order items
        orderItemRepository.saveAll(orderItemsList);
        savedOrder.setItems(orderItemsList);
        
        return savedOrder;
    }
    @Override
    public orders updateOrder(PostOrderRequest orderDTO) {
        Optional<orders> optionalOrder = ordersRepository.findById(orderDTO.getId());
        if (optionalOrder.isPresent()) {
            orders currentOrder = optionalOrder.get();
            // Chỉ cập nhật table_id nếu giá trị không null và hợp lệ
            if (orderDTO.getTable_id() != null) {
                // Kiểm tra table_id có tồn tại trong bảng tables
                if (!tableRepository.existsById(orderDTO.getTable_id())) {
                    throw new IllegalArgumentException("Table ID không hợp lệ: " + orderDTO.getTable_id());
                }
                currentOrder.setTable_id(orderDTO.getTable_id());
            }
            // Update only non-null fields
            if (orderDTO.getTable_id() != null) {
                currentOrder.setTable_id(orderDTO.getTable_id());
            }
            if (orderDTO.getStatus() != null) {
                currentOrder.setStatus(orderDTO.getStatus());
            }
            if (orderDTO.getItems() != null) {
                orderItemRepository.deleteById(currentOrder.getId());
                List<orders_items> orderItemsList = new ArrayList<>();
                for (PostOrderItemRequest itemDTO : orderDTO.getItems()) {
                    orders_items item = new orders_items();
                    item.setProduct_id(itemDTO.getProduct_id());
                    item.setQuantity(itemDTO.getQuantity());
                    item.setSubtotal(itemDTO.getSubtotal());
                    item.setStatus(itemDTO.getStatus());
                    item.setNotes(itemDTO.getNotes());

                    orderItemsList.add(item);
                }
                orderItemRepository.saveAll(orderItemsList);
                currentOrder.setItems(orderItemsList);
            }
            return ordersRepository.save(currentOrder);
        } else {
            // Order not found
            return null;
        }
    }

    @Override
    public void sortDeleteOrder(int id) {
        ordersRepository.softDeleteById(id);
    }

    @Override
    public orders findById(int id) {
        return ordersRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found with ID: " + id));
    }
    
    /**
     * Validates the order request data
     */
    private void validateOrderRequest(PostOrderRequest orderDTO) {
        if (orderDTO.getTable_id() == null || orderDTO.getTable_id() <= 0) {
            throw new IllegalArgumentException("Invalid table ID.");
        }
        if (orderDTO.getItems() == null || orderDTO.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item.");
        }
        
        // Validate order items
        for (PostOrderItemRequest item : orderDTO.getItems()) {
            if (item.getProduct_id() == null || item.getProduct_id() <= 0) {
                throw new IllegalArgumentException("Invalid product ID in order item.");
            }
            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Invalid quantity in order item.");
            }
        }
    }
    
    /**
     * Calculates the total amount for an order based on current product prices
     */
    private int calculateOrderTotal(List<PostOrderItemRequest> items) {
        int total = 0;
        for (PostOrderItemRequest item : items) {
            products product = productRepository.findById(item.getProduct_id())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + item.getProduct_id()));
            total += product.getPrice() * item.getQuantity();
        }
        return total;
    }
    
    /**
     * Updates order status with workflow validation
     */
    public orders updateOrderStatus(int orderId, String newStatus) {
        orders order = findById(orderId);
        
        // Validate status transition
        if (!isValidStatusTransition(order.getStatus(), newStatus)) {
            throw new IllegalArgumentException("Invalid status transition from " + order.getStatus() + " to " + newStatus);
        }
        
        order.setStatus(newStatus);
        return ordersRepository.save(order);
    }
    
    /**
     * Validates if a status transition is allowed
     */
    private boolean isValidStatusTransition(String currentStatus, String newStatus) {
        if (currentStatus == null) {
            return newStatus.equals(OrderStatus.PENDING.getStatus());
        }
        
        // Define allowed transitions
        switch (currentStatus) {
            case "pending":
                return newStatus.equals(OrderStatus.IN_PROGRESS.getStatus()) || 
                       newStatus.equals(OrderStatus.CANCELLED.getStatus());
            case "in-progress":
                return newStatus.equals(OrderStatus.COMPLETED.getStatus()) || 
                       newStatus.equals(OrderStatus.CANCELLED.getStatus());
            case "completed":
            case "cancelled":
                return false; // No transitions allowed from these states
            default:
                return false;
        }
    }
    
    /**
     * Gets orders by status
     */
    public List<orders> findByStatus(String status) {
        return ordersRepository.findByStatusAndDeletedOrderByCreatedAtDesc(status, 0);
    }
    
    /**
     * Gets pending orders for barista interface
     */
    public List<orders> getPendingOrders() {
        return findByStatus(OrderStatus.PENDING.getStatus());
    }
    
    /**
     * Gets in-progress orders for barista interface
     */
    public List<orders> getInProgressOrders() {
        return findByStatus(OrderStatus.IN_PROGRESS.getStatus());
    }
}
