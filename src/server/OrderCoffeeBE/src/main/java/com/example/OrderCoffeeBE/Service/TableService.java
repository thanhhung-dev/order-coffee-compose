package com.example.OrderCoffeeBE.Service;

import com.example.OrderCoffeeBE.Entity.tables;

import java.util.List;

public interface TableService {
    List<tables> findAll();
    tables createTable(tables tables);
    tables updateTable(tables tables);
    void deleteTable(int id);
    tables findById(int id);
}
