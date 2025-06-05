package com.example.contractmanagement.service;

import com.example.contractmanagement.DTO.PermissionDTO;
import com.example.contractmanagement.pojo.Permission;

public interface PermissionService {
    Permission getByrolename(String rolename);
    void changePermission(String rolename, PermissionDTO p);
}
