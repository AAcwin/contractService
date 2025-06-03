package com.example.contractmanagement.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.contractmanagement.mapper.PermissionMapper;
import com.example.contractmanagement.pojo.Permission;
import com.example.contractmanagement.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    PermissionMapper permissionMapper;
    @Override
    public Permission getByrolename(String rolename) {
        LambdaQueryWrapper<Permission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Permission::getRolename,rolename);
        return permissionMapper.selectOne(lambdaQueryWrapper);
    }
}
