package com.example.contractmanagement.controller;

import com.example.contractmanagement.Utils.ThreadLocalUtil;
import com.example.contractmanagement.Utils.UpdateProcess;
import com.example.contractmanagement.pojo.ToWeb;
import com.example.contractmanagement.service.ContractProcessService;
import com.example.contractmanagement.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contract")
public class ContractController {
    @Autowired
    ContractService contractService;
    @Autowired
    ContractProcessService contractProcessService;
    @Autowired
    UpdateProcess updateProcess;
    @PostMapping("/draft")
    public ToWeb draftContract(String contractname,String customername,String content,String starttime,String endtime){
        if(contractService.insertIntoTable(contractname,customername,content,starttime,endtime)){
            return ToWeb.success();
        }
        return ToWeb.error("数据错误");
    }

    @PostMapping("/assign")
    public ToWeb assignContract(String connum, @RequestParam List<String> countersign,@RequestParam List<String> approve,@RequestParam List<String> sign){
        for (String c : countersign){
            if(!contractProcessService.insertIntoTable(connum,1,c)){
                return ToWeb.error("数据无效");
            }
        }
        for(String a : approve){
            if(!contractProcessService.insertIntoTable(connum,2,a)){
                return ToWeb.error("数据无效");
            }
        }
        for (String s : sign){
            if(!contractProcessService.insertIntoTable(connum,3,s)){
                return ToWeb.error("数据无效");
            }
        }
        return ToWeb.success();
    }
@GetMapping("/mydetail")
    public ToWeb getMyContracts(){
       return ToWeb.success(contractProcessService.myContracts());
    }


@PostMapping("/countersign")
    public ToWeb finishCounterSign(String contractnum,String suggest){
        contractProcessService.finishProcess(contractnum,1, ThreadLocalUtil.getTL(),suggest);
        updateProcess.updateTable(contractnum);
        return ToWeb.success();
}

}
