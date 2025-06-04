package com.example.contractmanagement.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("userright")
public class UserRight {
    private String username;
    private String rolename;
}
