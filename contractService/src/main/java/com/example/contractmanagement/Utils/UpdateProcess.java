package com.example.contractmanagement.Utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.contractmanagement.mapper.ContractMapper;
import com.example.contractmanagement.mapper.ContractProcessMapper;
import com.example.contractmanagement.pojo.Contract;
import com.example.contractmanagement.pojo.ContractProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateProcess {
    @Autowired
    ContractProcessMapper contractProcessMapper;
    @Autowired
    ContractMapper contractMapper;
    private void check(String connum,int checknum,int targetnum,int nowtype,int totype){
        LambdaQueryWrapper<ContractProcess> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(ContractProcess::getConnum,connum)
                .eq(ContractProcess::getType, checknum)
                .ne(ContractProcess::getState,2);
        if(contractProcessMapper.selectOne(lambdaQueryWrapper)==null){
            if(targetnum==0){
                LambdaUpdateWrapper<Contract> lambdaUpdateWrapper1 = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper1.eq(Contract::getNum,connum)
                        .eq(Contract::getType,nowtype)
                        .set(Contract::getType,totype);
                contractMapper.update(lambdaUpdateWrapper1);
                return;
            }
            LambdaUpdateWrapper<ContractProcess> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(ContractProcess::getConnum,connum)
                    .eq(ContractProcess::getType, targetnum)
                    .eq(ContractProcess::getState,0)
                    .set(ContractProcess::getState,1);
            int rows = contractProcessMapper.update(lambdaUpdateWrapper);
            if(rows!=0){
                LambdaUpdateWrapper<Contract> lambdaUpdateWrapper1 = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper1.eq(Contract::getNum,connum)
                        .eq(Contract::getType,nowtype)
                        .set(Contract::getType,totype);
                contractMapper.update(lambdaUpdateWrapper1);
            }
        }
    }
    public void updateTable(String connum) {
        //检查会签情况
        check(connum,1,2,1,2);
        //检测审批情况
        check(connum,3,4,3,4);
        //检测签订情况
        check(connum,4,0,4,5);
    }
}
