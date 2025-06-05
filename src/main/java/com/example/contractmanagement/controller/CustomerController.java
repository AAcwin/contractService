package com.example.contractmanagement.controller;

import com.example.contractmanagement.pojo.Customer;
import com.example.contractmanagement.pojo.ToWeb;
import com.example.contractmanagement.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;
    @GetMapping("/detail")
    public ResponseEntity<ToWeb> getCustomers() {
        try {
            List<Customer> customers = customerService.getCustomers();

            if (customers == null || customers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(ToWeb.success("暂无客户数据"));
            }

            return ResponseEntity.ok()
                    .body(ToWeb.success(customers));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ToWeb.error("获取客户数据失败: " + e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ToWeb> addCustomer(@RequestBody Customer c){
        customerService.addCustomer(c);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ToWeb.success("客户添加成功"));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<ToWeb> deleteCustomer(@PathVariable String code) {
        try {
            // 1. 检查客户是否存在
            if (!customerService.existsByCode(code)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ToWeb.error("客户不存在"));
            }

            // 2. 执行业务删除逻辑
            customerService.deleteByCode(code);

            // 3. 返回成功响应
            return ResponseEntity.ok(ToWeb.success("删除成功"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ToWeb.error("删除失败: " + e.getMessage()));
        }
    }

    @PutMapping("/{code}")
    public ResponseEntity<ToWeb> updateCustomer(@PathVariable String code,
                                                @RequestBody Customer customer){
        // 1. 检查客户是否存在
        if (!customerService.existsByCode(code)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ToWeb.error("客户不存在"));
        }
        // 2. 执行业务删除逻辑
        customerService.deleteByCode(code);
        customerService.addCustomer(customer);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ToWeb.success("客户修改"));
    }
}
