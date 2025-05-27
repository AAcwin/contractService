package com.example.contractmanagement.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("functions")
public class Functions {
    @TableId
    private int id;
    private String module;
    private String code;
    private String name;
    private String description;
    private String status;
}
