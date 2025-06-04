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
    private void check(String connum,int checknum,int nowtype,int totype){
        LambdaQueryWrapper<ContractProcess> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(ContractProcess::getConnum,connum)
                .eq(ContractProcess::getType, checknum);

        if(contractProcessMapper.selectOne(lambdaQueryWrapper)!=null){
            lambdaQueryWrapper
                    .eq(ContractProcess::getConnum,connum)
                    .eq(ContractProcess::getType, checknum)
                    .ne(ContractProcess::getState,2);
            if(contractProcessMapper.selectOne(lambdaQueryWrapper)!=null){
                return;
            }
            System.out.println("type"+checknum+" finished");
            LambdaUpdateWrapper<Contract> lambdaUpdateWrapper1 = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper1.eq(Contract::getNum,connum)
                        .eq(Contract::getType,nowtype)
                        .set(Contract::getType,totype);
            contractMapper.update(lambdaUpdateWrapper1);

        }
    }
    public void updateTable(String connum) {
        //检查会签情况
        check(connum,1,1,2);
        //检查定稿情况
        check(connum,2,2,3);
        //检测审批情况
        check(connum,3,3,4);
        //检测签订情况
        check(connum,4,4,5);
    }
}
