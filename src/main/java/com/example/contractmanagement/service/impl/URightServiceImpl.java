package com.example.contractmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.contractmanagement.mapper.URightMapper;
import com.example.contractmanagement.pojo.UserRight;
import com.example.contractmanagement.service.URightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class URightServiceImpl implements URightService {
    @Autowired
    private URightMapper uRightMapper;
    @Override
    public UserRight findByName(String name) {
        LambdaQueryWrapper<UserRight> lq = new LambdaQueryWrapper<>();
        lq.eq(UserRight::getUsername,name);
        return uRightMapper.selectOne(lq);
    }
}
