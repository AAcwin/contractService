package com.example.contractmanagement.service;

import com.example.contractmanagement.pojo.Customer;

import java.util.List;

public interface CustomerService {
    void addCustomer(Customer c);
    List<Customer> getCustomers();

    boolean existsByCode(String code);

    void deleteByCode(String code);
}
