package com.example.contractmanagement.controller;

import com.example.contractmanagement.Utils.ContractProcessWithContent;
import com.example.contractmanagement.Utils.ThreadLocalUtil;
import com.example.contractmanagement.Utils.UpdateProcess;
import com.example.contractmanagement.pojo.Contract;
import com.example.contractmanagement.pojo.ContractProcess;
import com.example.contractmanagement.pojo.ToWeb;
import com.example.contractmanagement.service.ContractProcessService;
import com.example.contractmanagement.service.ContractService;
import com.example.contractmanagement.webservice.ContractProcessS;
import com.example.contractmanagement.webservice.ContractS;
import com.example.contractmanagement.webservice.CounterSignR;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ToWeb> draftContract(
            @RequestParam String contractname,
            @RequestParam String customername,
            @RequestParam String content,
            @RequestParam String starttime,
            @RequestParam String endtime) {

        String uid = contractService.insertIntoTable(contractname, customername, content, starttime, endtime);

        if (!uid.isEmpty()) {
            contractProcessService.insertIntoTable(uid, 2, ThreadLocalUtil.getTL());
            Map<String, String> uuid = new TreeMap<>();
            uuid.put("contractnum", uid);

            return ResponseEntity
                    .status(200) // 200
                    .body(ToWeb.success(uuid));
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400 Bad Request
                .body(ToWeb.error("非法操作"));
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
    public ResponseEntity<ToWeb> finishCounterSign(@RequestBody CounterSignR c) {
        // 检查合同状态
        if (contractService.checkState(c.getCode()) != 1) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // 400
                    .body(ToWeb.error("非法操作"));
        }

        // 处理会签流程
        if (contractProcessService.finishProcess(c.getCode(), 1, ThreadLocalUtil.getTL(), c.getCosigncontent())) {
            updateProcess.updateTable(c.getCode());
            return ResponseEntity
                    .status(HttpStatus.OK) // 200
                    .body(ToWeb.success());
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                .body(ToWeb.error("数据错误"));
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
    public ResponseEntity<ToWeb> allContracts() {
        List<Contract> contracts1 = contractService.showConstracts();
        List<ContractS> contracts = new ArrayList<>();

        // 如果查询结果为空
        if (contracts1.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // 404
                    .body(ToWeb.error("合同列表为空"));
        }

        // 转换DTO
        for (Contract c : contracts1) {
            ContractS cs = new ContractS();
            cs.setName(c.getName());
            cs.setCode(c.getNum());
            cs.setContent(c.getContent());
            cs.setCustomer(c.getCustomer());
            cs.setBeginDate(c.getBeginTime().toString());
            cs.setEndDate(c.getEndTime().toString());
            String status = switch (c.getType()) {
                case 1 -> "起草";
                case 2 -> "会签完成";
                case 3 -> "定稿完成";
                case 4 -> "审批完成";
                case 5 -> "签订完成";
                default -> "";
            };
            cs.setStatus(status);
            contracts.add(cs);
        }

        return ResponseEntity
                .status(HttpStatus.OK) // 200
                .body(ToWeb.success(contracts));  // 返回转换后的DTO列表
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

    @GetMapping("/process")
    public ResponseEntity<ToWeb> getAllProcess(){
        List<ContractProcessS> contractProcessSES = new ArrayList<>();
        Set<String> cons = contractProcessService.getAllNum();
        for(String s : cons){
            ContractProcessS contractProcessS = new ContractProcessS();
            contractProcessS.setCode(s);
            String status = switch (contractService.checkState(s)) {
                case 1 -> "待会签";
                case 2 -> "待定稿";
                case 3 -> "待审核";
                case 4 -> "待签订";
                case 5 -> "签订完成";
                default -> "";
            };
            contractProcessS.setStatus(status);
            List<ContractProcess> contractProcesses = contractProcessService.findBytype(s,2);//起稿人
            for(ContractProcess c : contractProcesses){
                contractProcessS.setDrafter(c.getUserName());
                contractProcessS.setDrafttime(c.getTime().toString());
            }
            contractProcesses = contractProcessService.findBytype(s,1);//会签人
            for(ContractProcess c : contractProcesses){
                contractProcessS.setCosigner(contractProcessS.getCosigner()==null?c.getUserName():contractProcessS.getCosigner()+" "+c.getUserName());
                contractProcessS.setCosigntime(c.getTime().toString());
                String contend = "";
                if(contractProcessS.getCosigncontent()!=null){
                    contend = contractProcessS.getCosigncontent();
                }
                contend += " "+c.getContend();
                contractProcessS.setCosigncontent(contend);
            }
            contractProcesses = contractProcessService.findBytype(s,3);//审批人
            for(ContractProcess c : contractProcesses){
                contractProcessS.setApprover(contractProcessS.getApprover()==null?c.getUserName():contractProcessS.getApprover()+" "+c.getUserName());
                contractProcessS.setApprovetime(c.getTime().toString());
            }

            contractProcessS.setFinalizer(contractProcessS.getDrafter());//定稿人
            contractProcessS.setFinalizetime(contractProcessS.getDrafttime());

            contractProcesses = contractProcessService.findBytype(s,4);//审批人
            for(ContractProcess c : contractProcesses){
                contractProcessS.setSigner(contractProcessS.getSigner()==null?c.getUserName():contractProcessS.getSigner()+" "+c.getUserName());
                contractProcessS.setSigntime(c.getTime().toString());
            }
            contractProcessSES.add(contractProcessS);
        }
        return ResponseEntity
                .status(HttpStatus.OK) // 200
                .body(ToWeb.success(contractProcessSES));
    }
}
