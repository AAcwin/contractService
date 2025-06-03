package com.example.contractmanagement.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.contractmanagement.mapper.UserRightMapper;
import com.example.contractmanagement.pojo.UserRight;
import com.example.contractmanagement.service.userService;
import com.example.contractmanagement.mapper.UserMapper;
import com.example.contractmanagement.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class userServiceImpl implements userService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRightMapper userRightMapper;
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
        ur.setRolename("newuser");
        userMapper.insert(u);
        userRightMapper.insert(ur);
    }

}
