package com.example.contractmanagement.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.contractmanagement.mapper.RoleMapper;
import com.example.contractmanagement.pojo.Role;
import com.example.contractmanagement.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleMapper roleMapper;
    @Override
    public List<String> findByFunctionId(int funId) {

        List<Role> roles = roleMapper.selectList(null);

        return roles.stream()
                .filter(role -> containsFunction(role.getFunctions(), funId))
                .map(Role::getName)
                .collect(Collectors.toList());
    }
    @Override
    public boolean containsFunction(String functionIds, int funId) {
        if (StringUtils.isBlank(functionIds)) {
            return false;
        }
        return Arrays.stream(functionIds.split(","))
                .map(String::trim)
                .anyMatch(id -> Integer.parseInt(id) == funId);
    }
}
