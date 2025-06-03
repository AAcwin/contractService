package com.example.contractmanagement.service;

import com.example.contractmanagement.pojo.Customer;
import java.util.List;

public interface CustomerService {

    /**
     * 添加客户
     */
    boolean addCustomer(Customer customer);

    /**
     * 删除客户
     */
    boolean deleteCustomer(String num);

    /**
     * 更新客户信息
     */
    boolean updateCustomer(Customer customer);

    /**
     * 根据客户编号查询客户
     */
    Customer getCustomerByNum(String num);

    /**
     * 根据客户名称查询客户
     */
    List<Customer> getCustomerByName(String name);

    /**
     * 查询所有客户
     */
    List<Customer> getAllCustomers();

    /**
     * 分页查询客户
     */
    List<Customer> getCustomersByPage(int page, int size);
}