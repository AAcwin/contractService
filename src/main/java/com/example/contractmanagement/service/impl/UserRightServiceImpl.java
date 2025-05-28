package com.example.contractmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.contractmanagement.mapper.UserRightMapper;
import com.example.contractmanagement.pojo.User;
import com.example.contractmanagement.pojo.UserRight;
import com.example.contractmanagement.service.UserRightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRightServiceImpl implements UserRightService {
    @Autowired
    private UserRightMapper uRightMapper;
    @Override
    public UserRight findByName(String name) {
        LambdaQueryWrapper<UserRight> lq = new LambdaQueryWrapper<>();
        lq.eq(UserRight::getUsername,name);
        return uRightMapper.selectOne(lq);
    }
    @Override
    public UserRight findByRole(String name) {
        LambdaQueryWrapper<UserRight> lq = new LambdaQueryWrapper<>();
        lq.eq(UserRight::getRolename,name);
        return uRightMapper.selectOne(lq);
    }

    @Override
    public List<UserRight> getUsers() {
        return uRightMapper.selectList(null);
    }

    @Override
    public  boolean changeRole(String user,String role){
        LambdaUpdateWrapper<UserRight> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(UserRight::getUsername,user)
                .set(UserRight::getRolename,role);
        int rows = uRightMapper.update(lambdaUpdateWrapper);
        if (rows==0){
            return false;
        }
        return true;
    }

}
