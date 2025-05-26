package com.example.contractmanagement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.contractmanagement.mapper")
public class ContractManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContractManagementApplication.class, args);
    }

}
