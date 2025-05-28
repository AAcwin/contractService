package com.example.contractmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.contractmanagement.Utils.ThreadLocalUtil;
import com.example.contractmanagement.mapper.ContractProcessMapper;
import com.example.contractmanagement.pojo.ContractProcess;
import com.example.contractmanagement.service.ContractProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContractProcessServiceImpl implements ContractProcessService {
    @Autowired
    ContractProcessMapper contractProcessMapper;
    @Override
    public boolean insertIntoTable(String num,int type,String user) {
        ContractProcess contractProcess = new ContractProcess();
        contractProcess.setConnum(num);
        contractProcess.setUserName(user);
        contractProcess.setTime(LocalDateTime.now());
        contractProcess.setState(0);
        contractProcess.setType(type);
        try {
            contractProcessMapper.insert(contractProcess);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public List<ContractProcess> myContracts() {
        LambdaQueryWrapper<ContractProcess> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ContractProcess::getState,0)
                .eq(ContractProcess::getUserName, ThreadLocalUtil.getTL());
        return contractProcessMapper.selectList(lambdaQueryWrapper);
    }
}
