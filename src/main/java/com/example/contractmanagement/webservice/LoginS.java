package com.example.contractmanagement.webservice;

import com.example.contractmanagement.pojo.Permission;
import lombok.Data;

@Data
public class LoginS {
    private String name;
    private String role;
    private Permission permission;
    private String authorized;
}
