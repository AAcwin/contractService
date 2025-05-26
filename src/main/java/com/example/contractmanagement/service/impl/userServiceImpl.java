package com.example.contractmanagement.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.contractmanagement.service.userService;
import com.example.contractmanagement.mapper.userMapper;
import com.example.contractmanagement.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class userServiceImpl implements userService {
    @Autowired
    private userMapper userMapper;
    @Override
    public User findByName(String name) {
        LambdaQueryWrapper<User> lambdaQuery = new LambdaQueryWrapper<>();
        lambdaQuery.eq(User::getUsername, name);
        return userMapper.selectOne(lambdaQuery);
    }

    @Override
    public void register(String name, String password) {
        User u = new User();
        u.setUsername(name);
        u.setPassword(password);
        userMapper.insert(u);
    }
}
