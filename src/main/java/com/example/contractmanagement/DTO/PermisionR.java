package com.example.contractmanagement.DTO;

import com.example.contractmanagement.pojo.Permission;
import lombok.Data;


@Data
public class PermisionR {
    private String role;
    private PermissionDTO permission;
}
