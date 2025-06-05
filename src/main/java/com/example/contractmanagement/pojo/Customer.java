package com.example.contractmanagement.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("customer")
public class Customer {
    private String name;
    private String address;
    private String phone;
    private String contact;
    private String code;
}