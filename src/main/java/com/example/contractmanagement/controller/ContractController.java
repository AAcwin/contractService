package com.example.contractmanagement.controller;

import com.example.contractmanagement.pojo.ToWeb;
import com.example.contractmanagement.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contract")
public class ContractController {
    @Autowired
    ContractService contractService;
    @PostMapping("/draft")
    public ToWeb draftContract(String contractname,String customername,String content,String starttime,String endtime){
        if(contractService.insertIntoTable(contractname,customername,content,starttime,endtime)){
            return ToWeb.success();
        }
        return ToWeb.error("数据错误");
    }

}
