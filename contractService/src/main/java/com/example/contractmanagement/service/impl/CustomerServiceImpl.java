package com.example.contractmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.contractmanagement.mapper.CustomerMapper;
import com.example.contractmanagement.pojo.Customer;
import com.example.contractmanagement.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public boolean addCustomer(Customer customer) {
        // 生成客户编号
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String shortUuid = uuid.substring(0, 8);
        String timestamp = String.valueOf(System.currentTimeMillis());
        String customerNum = "CUS" + shortUuid + timestamp.substring(timestamp.length() - 6);
        customer.setNum(customerNum);

        int rows = customerMapper.insert(customer);
        return rows > 0;
    }

    @Override
    public boolean deleteCustomer(String num) {
        if (!StringUtils.hasText(num)) {
            return false;
        }

        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customer::getNum, num);

        int rows = customerMapper.delete(queryWrapper);
        return rows > 0;
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        if (customer == null || !StringUtils.hasText(customer.getNum())) {
            return false;
        }

        LambdaUpdateWrapper<Customer> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Customer::getNum, customer.getNum());

        // 只更新非空字段
        if (StringUtils.hasText(customer.getName())) {
            updateWrapper.set(Customer::getName, customer.getName());
        }
        if (StringUtils.hasText(customer.getAddress())) {
            updateWrapper.set(Customer::getAddress, customer.getAddress());
        }
        if (StringUtils.hasText(customer.getTel())) {
            updateWrapper.set(Customer::getTel, customer.getTel());
        }
        if (StringUtils.hasText(customer.getFax())) {
            updateWrapper.set(Customer::getFax, customer.getFax());
        }
        if (StringUtils.hasText(customer.getCode())) {
            updateWrapper.set(Customer::getCode, customer.getCode());
        }
        if (StringUtils.hasText(customer.getBank())) {
            updateWrapper.set(Customer::getBank, customer.getBank());
        }
        if (StringUtils.hasText(customer.getAccount())) {
            updateWrapper.set(Customer::getAccount, customer.getAccount());
        }
        if (StringUtils.hasText(customer.getRemark())) {
            updateWrapper.set(Customer::getRemark, customer.getRemark());
        }

        int rows = customerMapper.update(updateWrapper);
        return rows > 0;
    }

    @Override
    public Customer getCustomerByNum(String num) {
        if (!StringUtils.hasText(num)) {
            return null;
        }

        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customer::getNum, num);

        return customerMapper.selectOne(queryWrapper);
    }

    @Override
    public List<Customer> getCustomerByName(String name) {
        if (!StringUtils.hasText(name)) {
            return null;
        }

        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Customer::getName, name);

        return customerMapper.selectList(queryWrapper);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerMapper.selectList(null);
    }

    @Override
    public List<Customer> getCustomersByPage(int page, int size) {
        if (page < 1) page = 1;
        if (size < 1) size = 10;

        Page<Customer> customerPage = new Page<>(page, size);
        Page<Customer> result = customerMapper.selectPage(customerPage, null);

        return result.getRecords();
    }
}