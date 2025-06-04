package com.example.contractmanagement.service;

import com.example.contractmanagement.pojo.Permission;

public interface PermissionService {
    Permission getByrolename(String rolename);
}
