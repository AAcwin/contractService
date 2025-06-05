package com.example.contractmanagement.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.contractmanagement.DTO.PermissionDTO;
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

    @Override
    public void changePermission(String rolename, PermissionDTO p) {
        LambdaUpdateWrapper<Permission> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Permission::getRolename,rolename)
                .set(Permission::isApprove,p.isApprove())
                .set(Permission::isDraft,p.isDraft())
                .set(Permission::isQuery,p.isQuery())
                .set(Permission::isSign,p.isSign())
                .set(Permission::isFinalize,p.isFinalize())
                .set(Permission::isCountersign,p.isCountersign());
        permissionMapper.update(lambdaUpdateWrapper);

    }
}
