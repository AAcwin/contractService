package com.example.contractmanagement.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("role")
public class Role {
    private String name;
    private String description;
    private String functions;
}
