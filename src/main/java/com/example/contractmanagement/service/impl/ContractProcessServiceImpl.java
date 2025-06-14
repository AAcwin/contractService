package com.example.contractmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.injector.methods.Update;
import com.example.contractmanagement.Utils.ThreadLocalUtil;
import com.example.contractmanagement.mapper.ContractProcessMapper;
import com.example.contractmanagement.pojo.Contract;
import com.example.contractmanagement.pojo.ContractProcess;
import com.example.contractmanagement.service.ContractProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class ContractProcessServiceImpl implements ContractProcessService {
    @Autowired
    ContractProcessMapper contractProcessMapper;
    @Override
    public boolean insertIntoTable(String num,int type,String user) {
        ContractProcess contractProcess = new ContractProcess();
        contractProcess.setConnum(num);
        contractProcess.setUserName(user);
        contractProcess.setType(type);
        if(contractProcess.getType()==0){
            contractProcess.setState(2);
            contractProcess.setTime(LocalDateTime.now());
        }
        else{
            contractProcess.setState(1);
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
                .set(ContractProcess::getState,2)
                .set(ContractProcess::getTime,LocalDateTime.now());
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

    @Override
    public void changeState(String connum,int state) {
        LambdaUpdateWrapper<ContractProcess> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ContractProcess::getConnum,connum)
                .set(ContractProcess::getState,state);
        contractProcessMapper.update(lambdaUpdateWrapper);
    }

    @Override
    public List<ContractProcess> findMessage(String contractnum,int type) {
        LambdaQueryWrapper<ContractProcess> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ContractProcess::getConnum,contractnum)
                .eq(ContractProcess::getState,2)
                .eq(ContractProcess::getType,type);
        return contractProcessMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public List<ContractProcess> findBytype(String contractnum,int type){
        LambdaQueryWrapper<ContractProcess> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ContractProcess::getConnum,contractnum)
                .eq(ContractProcess::getType,type);
        return contractProcessMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public Set<String> getAllNum(){
        Set<String> nums = new TreeSet<>();
        List<ContractProcess> contractProcesses = contractProcessMapper.selectList(null);
        for(ContractProcess c : contractProcesses){
            nums.add(c.getConnum());
        }
        return nums;
    }

    @Override
    public void deleteContract(String code) {
        LambdaUpdateWrapper<ContractProcess> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ContractProcess::getConnum,code);
        contractProcessMapper.delete(lambdaUpdateWrapper);
    }


}
