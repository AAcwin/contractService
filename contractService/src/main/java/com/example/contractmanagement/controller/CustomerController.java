package com.example.contractmanagement.controller;

import com.example.contractmanagement.pojo.Customer;
import com.example.contractmanagement.pojo.ToWeb;
import com.example.contractmanagement.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * 添加客户
     */
    @PostMapping("/add")
    public ToWeb addCustomer(@RequestBody Customer customer) {
        // 验证必填字段
        if (!StringUtils.hasText(customer.getName())) {
            return ToWeb.error("客户名称不能为空");
        }
        if (!StringUtils.hasText(customer.getTel())) {
            return ToWeb.error("客户电话不能为空");
        }

        boolean success = customerService.addCustomer(customer);
        if (success) {
            return ToWeb.success("客户添加成功");
        } else {
            return ToWeb.error("客户添加失败");
        }
    }

    /**
     * 删除客户
     */
    @PostMapping("/delete")
    public ToWeb deleteCustomer(@RequestParam String num) {
        if (!StringUtils.hasText(num)) {
            return ToWeb.error("客户编号不能为空");
        }

        boolean success = customerService.deleteCustomer(num);
        if (success) {
            return ToWeb.success("客户删除成功");
        } else {
            return ToWeb.error("客户删除失败或客户不存在");
        }
    }

    /**
     * 更新客户信息
     */
    @PostMapping("/update")
    public ToWeb updateCustomer(@RequestBody Customer customer) {
        if (!StringUtils.hasText(customer.getNum())) {
            return ToWeb.error("客户编号不能为空");
        }

        boolean success = customerService.updateCustomer(customer);
        if (success) {
            return ToWeb.success("客户信息更新成功");
        } else {
            return ToWeb.error("客户信息更新失败");
        }
    }

    /**
     * 根据编号查询客户
     */
    @GetMapping("/detail")
    public ToWeb getCustomerByNum(@RequestParam String num) {
        if (!StringUtils.hasText(num)) {
            return ToWeb.error("客户编号不能为空");
        }

        Customer customer = customerService.getCustomerByNum(num);
        if (customer != null) {
            return ToWeb.success(customer);
        } else {
            return ToWeb.error("客户不存在");
        }
    }

    /**
     * 根据名称模糊查询客户
     */
    @GetMapping("/search")
    public ToWeb getCustomerByName(@RequestParam String name) {
        if (!StringUtils.hasText(name)) {
            return ToWeb.error("客户名称不能为空");
        }

        List<Customer> customers = customerService.getCustomerByName(name);
        return ToWeb.success(customers);
    }

    /**
     * 查询所有客户
     */
    @GetMapping("/all")
    public ToWeb getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return ToWeb.success(customers);
    }

    /**
     * 分页查询客户
     */
    @GetMapping("/page")
    public ToWeb getCustomersByPage(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        List<Customer> customers = customerService.getCustomersByPage(page, size);
        return ToWeb.success(customers);
    }
}