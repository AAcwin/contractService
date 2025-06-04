package com.example.contractmanagement.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("customer")
public class Customer {

    @TableId
    private String num;
    private String name;
    private String address;
    private String tel;
    private String fax;
    private String code;
    private String bank;
    private String account;
    private String remark;     // 备注
}