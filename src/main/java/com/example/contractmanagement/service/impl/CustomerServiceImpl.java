package com.example.contractmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.contractmanagement.mapper.CustomerMapper;
import com.example.contractmanagement.pojo.Customer;
import com.example.contractmanagement.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerMapper customerMapper;
    @Override
    public void addCustomer(Customer c) {
        customerMapper.insert(c);
    }

    @Override
    public List<Customer> getCustomers() {
        return customerMapper.selectList(null);
    }

    @Override
    public boolean existsByCode(String code) {
        LambdaQueryWrapper<Customer> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Customer::getCode,code);
        return customerMapper.selectCount(lambdaQueryWrapper) > 0;
    }

    @Override
    public void deleteByCode(String code) {
        LambdaQueryWrapper<Customer> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Customer::getCode,code);
        customerMapper.delete(lambdaQueryWrapper);
    }
}
