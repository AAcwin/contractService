package com.example.contractmanagement.controller;

import com.example.contractmanagement.Utils.ContractProcessWithContent;
import com.example.contractmanagement.Utils.ThreadLocalUtil;
import com.example.contractmanagement.Utils.UpdateProcess;
import com.example.contractmanagement.pojo.ContractProcess;
import com.example.contractmanagement.pojo.ToWeb;
import com.example.contractmanagement.service.ContractProcessService;
import com.example.contractmanagement.service.ContractService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
        String uid = contractService.insertIntoTable(contractname,customername,content,starttime,endtime);
        if(!uid.isEmpty()){
            contractProcessService.insertIntoTable(uid,2,ThreadLocalUtil.getTL());
            Map<String,String> uuid = new TreeMap<>();
            uuid.put("contractnum",uid);
            return ToWeb.success(uuid);
        }
        return ToWeb.error("非法操作");
    }

    @PostMapping("/assign")
    public ToWeb assignContract(String connum, @RequestParam List<String> countersign,@RequestParam List<String> approve,@RequestParam List<String> sign){
        if(contractService.checkState(connum)!=1){
            return ToWeb.error("非法操作");
        }
        for (String c : countersign){
            if(!contractProcessService.insertIntoTable(connum,1,c)){
                return ToWeb.error("非法操作");
            }
        }
        for(String a : approve){
            if(!contractProcessService.insertIntoTable(connum,3,a)){
                return ToWeb.error("非法操作");
            }
        }
        for (String s : sign){
            if(!contractProcessService.insertIntoTable(connum,4,s)){
                return ToWeb.error("非法操作");
            }
        }
        return ToWeb.success();
    }
    @GetMapping("/mydetail")
    public ToWeb getMyContracts(){
       return ToWeb.success(contractProcessService.myContracts());
    }


    @PostMapping("/countersign")
    public ToWeb finishCounterSign(String contractnum,String content){
        if(contractService.checkState(contractnum)!=1){
            return ToWeb.error("非法操作");
        }
        if(contractProcessService.finishProcess(contractnum,1, ThreadLocalUtil.getTL(),content)){
            updateProcess.updateTable(contractnum);
            return ToWeb.success();
        }
        return ToWeb.error("数据错误");

    }
    @PostMapping("/final")
    public ToWeb finishFinal(String contractnum,String content){
        if(contractService.checkState(contractnum)!=2){
            return ToWeb.error("非法操作");
        }
        if(contractService.finishC(contractnum,content)){
            contractProcessService.finalProcess(contractnum);
            return ToWeb.success();
        }
        return ToWeb.error("数据错误");
    }

    @PostMapping("/approve")
    public ToWeb finishApprove(String contractnum, String result,String content){
        if(contractService.checkState(contractnum)!=3){
            return ToWeb.error("非法操作");
        }
        if(Objects.equals(result, "true")){
            contractProcessService.finishProcess(contractnum,3,ThreadLocalUtil.getTL(),content);
            updateProcess.updateTable(contractnum);
        }else{
            contractService.changeType(contractnum,0);
            contractProcessService.changeState(contractnum,0);
        }
        return ToWeb.success();
    }

    @PostMapping("/sign")
    public ToWeb finishSign(String contractnum,String content){
        if(contractService.checkState(contractnum)!=4){
            return ToWeb.error("非法操作");
        }
        contractProcessService.finishProcess(contractnum,4,ThreadLocalUtil.getTL(),content);
        updateProcess.updateTable(contractnum);
        return ToWeb.success();
    }

    @GetMapping("/detail")
    public ToWeb allContracts(){
        return ToWeb.success(contractService.showConstracts());
    }

    @PostMapping("/typedetail")
    public ToWeb typeContracts(int type){
        return ToWeb.success(contractService.findByType(type));
    }

    @GetMapping("/mycontracts")
    public ToWeb myContracts(){
        return ToWeb.success(contractService.findByUser());
    }

    @GetMapping("/mysignedcontracts")
    public ToWeb mySignedContracts(){
        return ToWeb.success(contractService.findByMyType(5));
    }

    @PostMapping("/getcountermessage")
    public ToWeb getCounterMessage(String contractnum){
        List<ContractProcessWithContent> dto = new ArrayList<>();
        List<ContractProcess> contractProcess =contractProcessService.findMessage(contractnum,1);
        for(ContractProcess c : contractProcess){
            ContractProcessWithContent c1 = new ContractProcessWithContent();
            c1.setUserName(c.getUserName());
            c1.setContend(c.getContend());
            c1.setTime(c.getTime());
            dto.add(c1);
        }
        return ToWeb.success(dto);
    }

    @PostMapping("/getfinnalmessage")
    public ToWeb getFinnalMessage(String contractnum){
        List<ContractProcessWithContent> dto = new ArrayList<>();
        List<ContractProcess> contractProcess =contractProcessService.findMessage(contractnum,3);
        for(ContractProcess c : contractProcess){
            ContractProcessWithContent c1 = new ContractProcessWithContent();
            c1.setUserName(c.getUserName());
            c1.setContend(c.getContend());
            c1.setTime(c.getTime());
            dto.add(c1);
        }
        return ToWeb.success(dto);
    }
}
