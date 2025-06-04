package com.example.contractmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.contractmanagement.mapper.UserRightMapper;
import com.example.contractmanagement.pojo.UserRight;
import com.example.contractmanagement.service.userService;
import com.example.contractmanagement.mapper.UserMapper;
import com.example.contractmanagement.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class userServiceImpl implements userService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRightMapper userRightMapper;

    // 原有的方法保持不变
    @Override
    public User findByName(String name) {
        LambdaQueryWrapper<User> lambdaQuery = new LambdaQueryWrapper<>();
        lambdaQuery.eq(User::getUsername, name);
        return userMapper.selectOne(lambdaQuery);
    }

    @Override
    public void register(String name, String password,String email) {
        User u = new User();
        u.setUsername(name);
        u.setPassword(password);
        u.setEmail(email);
        UserRight ur = new UserRight();
        ur.setUsername(name);
        userMapper.insert(u);
        userRightMapper.insert(ur);
    }

    // 新增的管理员功能方法
    @Override
    public List<User> getAllUsers() {
        return userMapper.selectList(null);
    }

    @Override
    public boolean addUser(User user) {
        int rows = userMapper.insert(user);
        return rows > 0;
    }

    @Override
    public boolean deleteUser(String username) {
        if (!StringUtils.hasText(username)) {
            return false;
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);

        int rows = userMapper.delete(queryWrapper);
        return rows > 0;
    }

    @Override
    public boolean updateUser(User user) {
        if (user == null || !StringUtils.hasText(user.getUsername())) {
            return false;
        }

        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getUsername, user.getUsername());

        // 只更新密码（如果提供的话）
        if (StringUtils.hasText(user.getPassword())) {
            updateWrapper.set(User::getPassword, user.getPassword());
        }

        int rows = userMapper.update(null, updateWrapper);
        return rows > 0;
    }

    @Override
    public User getUserByUsername(String username) {
        return findByName(username); // 复用已有的方法
    }
}