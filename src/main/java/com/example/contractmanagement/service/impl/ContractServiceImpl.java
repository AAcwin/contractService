package com.example.contractmanagement.service.impl;

import com.example.contractmanagement.Utils.ThreadLocalUtil;
import com.example.contractmanagement.mapper.ContractMapper;
import com.example.contractmanagement.pojo.Contract;
import com.example.contractmanagement.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class ContractServiceImpl implements ContractService {
    @Autowired
    ContractMapper contractMapper;


    @Override
    public boolean insertIntoTable(String contractname, String customername, String content, String starttime, String endtime) {
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
        try {
            contractMapper.insert(contract);
            return true;
        }catch (Exception e){
            return false;
        }

    }
}
