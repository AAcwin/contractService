package com.example.contractmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.contractmanagement.Utils.ThreadLocalUtil;
import com.example.contractmanagement.mapper.ContractMapper;
import com.example.contractmanagement.pojo.Contract;
import com.example.contractmanagement.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class ContractServiceImpl implements ContractService {
    @Autowired
    ContractMapper contractMapper;


    @Override
    public String insertIntoTable(String contractname, String customername, String content, String starttime, String endtime) {
        Contract contract = new Contract();
        contract.setName(contractname);
        contract.setCustomer(customername);
        contract.setContent(content);
        contract.setBeginTime(LocalDate.parse(starttime));
        contract.setEndTime(LocalDate.parse(endtime));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String shortUuid = uuid.substring(0, 7);
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uid = shortUuid + timestamp;
        contract.setNum(uid);
        contract.setUserName(ThreadLocalUtil.getTL());
        contract.setFinishTime(LocalDateTime.now());
        contract.setType(1);
        contractMapper.insert(contract);
        return uid;
    }

    @Override
    public boolean finishC(String connum,String contend) {
        LambdaUpdateWrapper<Contract> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Contract::getNum,connum)
                .eq(Contract::getType,2)
                .set(Contract::getType,3)
                .set(Contract::getContent,contend);
        int rows = contractMapper.update(lambdaUpdateWrapper);
        if(rows == 0){
            return false;
        }
        return true;
    }

    @Override
    public void changeType(String connum, int type) {
        LambdaUpdateWrapper<Contract> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Contract::getNum,connum)
                .set(Contract::getType,type);
        contractMapper.update(lambdaUpdateWrapper);
    }
    @Override
    public int checkState(String connum){
        LambdaQueryWrapper<Contract> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Contract::getNum,connum);
        Contract c = contractMapper.selectOne(lambdaQueryWrapper);
        return c.getType();
    }

    @Override
    public List<Contract> showConstracts() {
        return contractMapper.selectList(null);
    }


    @Override
    public List<Contract> findByType(int type){
        LambdaQueryWrapper<Contract> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Contract::getType,type);
        return contractMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public List<Contract> findByUser(){
        LambdaQueryWrapper<Contract> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Contract::getUserName,ThreadLocalUtil.getTL());
        return contractMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public List<Contract> findByMyType(int type) {
        LambdaQueryWrapper<Contract> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Contract::getType,type)
                .eq(Contract::getUserName,ThreadLocalUtil.getTL());
        return contractMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public boolean adminModifyContract(String connum, String content) {
        LambdaUpdateWrapper<Contract> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Contract::getNum, connum)
                .set(Contract::getContent, content)
                .set(Contract::getFinishTime, LocalDateTime.now()); // 更新修改时间

        int rows = contractMapper.update(lambdaUpdateWrapper);
        return rows > 0;
    }

    @Override
    public boolean adminUpdateContractInfo(String connum, String contractname, String customername,
                                           String content, String starttime, String endtime) {
        LambdaUpdateWrapper<Contract> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Contract::getNum, connum)
                .set(Contract::getName, contractname)
                .set(Contract::getCustomer, customername)
                .set(Contract::getContent, content)
                .set(Contract::getBeginTime, LocalDate.parse(starttime))
                .set(Contract::getEndTime, LocalDate.parse(endtime))
                .set(Contract::getFinishTime, LocalDateTime.now());

        int rows = contractMapper.update(lambdaUpdateWrapper);
        return rows > 0;
    }
}
