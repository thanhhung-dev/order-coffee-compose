package com.example.OrderCoffeeBE.Service;

import com.example.OrderCoffeeBE.Entity.customers;

import java.util.List;

public interface CustomerService {
    List<customers> findAll();
    customers findById(int id);
    customers createCustomer(customers customer);
    customers updateCustomer(customers customer);
    void deleteCustomer(int id);
    customers findByEmail(String email);
    customers findByPhone(String phone);
    List<customers> searchByName(String name);
    boolean existsByEmail(String email);
}