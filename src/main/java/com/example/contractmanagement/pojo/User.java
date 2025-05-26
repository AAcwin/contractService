package com.example.contractmanagement.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("User")
public class User {
    private int id;
    private String username;
    private String password;
    private int del;
}
