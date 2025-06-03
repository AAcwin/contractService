package com.example.contractmanagement.webservice;

import com.example.contractmanagement.pojo.Permission;
import lombok.Data;

@Data
public class UserS {
    private String name;
    private String password;
    private String email;
    private String role;
    private Permission permission;
}
