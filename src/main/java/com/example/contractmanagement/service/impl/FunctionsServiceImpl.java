package com.example.contractmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.contractmanagement.mapper.FunctionsMapper;
import com.example.contractmanagement.pojo.Functions;
import com.example.contractmanagement.service.FunctionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FunctionsServiceImpl implements FunctionsService {
    @Autowired
    FunctionsMapper functionsMapper;
    @Override
    public int findByName(String name) {
        LambdaQueryWrapper<Functions> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Functions::getName,name);
        Functions f = functionsMapper.selectOne(lambdaQueryWrapper);
        if(f == null){
            return 0;
        }
        return f.getId();
    }
}
