package com.example.contractmanagement.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
@TableName("user")
public class User {
    private String username;
    @JsonIgnore
    private String password;
    private String email;
}
