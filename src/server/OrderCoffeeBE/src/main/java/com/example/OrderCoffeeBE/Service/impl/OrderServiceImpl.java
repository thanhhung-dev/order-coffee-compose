package com.example.OrderCoffeeBE.Service.impl;

import com.example.OrderCoffeeBE.Entity.Request.PostOrderItemRequest;
import com.example.OrderCoffeeBE.Entity.Request.PostOrderRequest;
import com.example.OrderCoffeeBE.Entity.orders;
import com.example.OrderCoffeeBE.Entity.orders_items;
import com.example.OrderCoffeeBE.Entity.products;
import com.example.OrderCoffeeBE.Service.OrderService;
import com.example.OrderCoffeeBE.repository.OrderItemRepository;
import com.example.OrderCoffeeBE.repository.OrdersRepository;
import com.example.OrderCoffeeBE.repository.TableRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Table;
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
    @Override
    public List<orders> findAll() {
        return ordersRepository.findAllNotDeleted();
    }

    @Override
    public orders createOrder(PostOrderRequest orderDTO) {
        // Validate input
        if (orderDTO.getTable_id() == null || orderDTO.getTable_id() <= 0) {
            throw new IllegalArgumentException("Invalid table ID.");
        }
        if (orderDTO.getItems() == null || orderDTO.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item.");
        }
        orders order = new orders();
        order.setTable_id(orderDTO.getTable_id());
        order.setStatus(orderDTO.getStatus());
        order.setDeleted(0);
        order.setTotal_amount(orderDTO.getTotalAmount());
        orders savedOrder = ordersRepository.save(order);
        List<orders_items> orderItemsList = new ArrayList<>();
        for (PostOrderItemRequest itemDTO : orderDTO.getItems()) {
            orders_items item = new orders_items();
            item.setOrder(savedOrder);
            item.setProduct_id(itemDTO.getProduct_id());
            item.setQuantity(itemDTO.getQuantity());
            item.setSubtotal(itemDTO.getSubtotal());
            item.setNotes(itemDTO.getNotes());
            orderItemsList.add(item);
        }

        // Lưu các items của đơn hàng
        orderItemRepository.saveAll(orderItemsList);

        // Gán items vào đơn hàng và trả về
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
}
