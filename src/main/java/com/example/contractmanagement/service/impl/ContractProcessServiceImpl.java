package com.example.contractmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.injector.methods.Update;
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
        contractProcess.setType(type);
        if(contractProcess.getType()==1){
            contractProcess.setState(1);
        }
        else{
            contractProcess.setState(0);
        }
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
        lambdaQueryWrapper.eq(ContractProcess::getState,1)
                .eq(ContractProcess::getUserName, ThreadLocalUtil.getTL());
        return contractProcessMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public boolean finishProcess(String connum,int type,String user,String contend) {
        LambdaUpdateWrapper<ContractProcess> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ContractProcess::getConnum,connum)
                .eq(ContractProcess::getType,type)
                .eq(ContractProcess::getUserName,user)
                .set(ContractProcess::getContend,contend)
                .set(ContractProcess::getState,2);
        try {
            contractProcessMapper.update(lambdaUpdateWrapper);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public void finalProcess(String connum) {
        LambdaUpdateWrapper<ContractProcess> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ContractProcess::getUserName,ThreadLocalUtil.getTL())
                .eq(ContractProcess::getConnum,connum)
                .eq(ContractProcess::getType,2)
                .set(ContractProcess::getState,2);
        contractProcessMapper.update(lambdaUpdateWrapper);

        LambdaUpdateWrapper<ContractProcess> lambdaUpdateWrapper1 = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper1.eq(ContractProcess::getConnum,connum)
                .eq(ContractProcess::getType,3)
                .set(ContractProcess::getState,1);
        contractProcessMapper.update(lambdaUpdateWrapper1);
    }


}
