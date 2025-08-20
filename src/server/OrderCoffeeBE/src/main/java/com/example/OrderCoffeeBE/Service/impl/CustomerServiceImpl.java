package com.example.OrderCoffeeBE.Service.impl;

import com.example.OrderCoffeeBE.Entity.customers;
import com.example.OrderCoffeeBE.Service.CustomerService;
import com.example.OrderCoffeeBE.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service("customerService")
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public List<customers> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public customers findById(int id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Customer not found with id: " + id));
    }

    @Override
    public customers createCustomer(customers customer) {
        // Validate email uniqueness
        if (customer.getEmail() != null && customerRepository.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Customer with email " + customer.getEmail() + " already exists");
        }
        return customerRepository.save(customer);
    }

    @Override
    public customers updateCustomer(customers customer) {
        customers existingCustomer = findById(customer.getId());
        
        // Update fields
        existingCustomer.setName(customer.getName());
        if (customer.getEmail() != null) {
            existingCustomer.setEmail(customer.getEmail());
        }
        if (customer.getPhone() != null) {
            existingCustomer.setPhone(customer.getPhone());
        }
        
        return customerRepository.save(existingCustomer);
    }

    @Override
    public void deleteCustomer(int id) {
        customers customer = findById(id);
        customerRepository.delete(customer);
    }

    @Override
    public customers findByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElse(null);
    }

    @Override
    public customers findByPhone(String phone) {
        return customerRepository.findByPhone(phone)
                .orElse(null);
    }

    @Override
    public List<customers> searchByName(String name) {
        return customerRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }
}