package com.example.contractmanagement.controller;

import com.example.contractmanagement.Utils.ContractProcessWithContent;
import com.example.contractmanagement.Utils.ThreadLocalUtil;
import com.example.contractmanagement.Utils.UpdateProcess;
import com.example.contractmanagement.pojo.Contract;
import com.example.contractmanagement.pojo.ContractProcess;
import com.example.contractmanagement.pojo.ToWeb;
import com.example.contractmanagement.service.ContractProcessService;
import com.example.contractmanagement.service.ContractService;
import com.example.contractmanagement.DTO.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.example.contractmanagement.Utils.fromString.fromString;

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
            contractProcessService.insertIntoTable(uid, 0, ThreadLocalUtil.getTL());
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
    public ResponseEntity<ToWeb> assignContract(@RequestBody AssignR request) {
        try {
            // 验证合同状态
            if (contractService.checkState(request.getCode()) == 5) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ToWeb.error("非法操作：合同当前状态不允许分配"));
            }

            // 验证至少有一个角色被分配
            if (request.getCosigner() == null &&
                    request.getFinalizer() == null &&
                    request.getApprover() == null &&
                    request.getSigner() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ToWeb.error("必须指定至少一个角色（会签人/定稿人/审批人/签订人）"));
            }

            // 处理会签人分配
            if (request.getCosigner() != null &&
                    !contractProcessService.insertIntoTable(request.getCode(), 1, request.getCosigner())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ToWeb.error("分配会签人失败"));
            }

            // 处理定稿人分配
            if (request.getFinalizer() != null &&
                    !contractProcessService.insertIntoTable(request.getCode(), 2, request.getFinalizer())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ToWeb.error("分配定稿人失败"));
            }

            // 处理审批人分配
            if (request.getApprover() != null &&
                    !contractProcessService.insertIntoTable(request.getCode(), 3, request.getApprover())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ToWeb.error("分配审批人失败"));
            }

            // 处理签订人分配
            if (request.getSigner() != null &&
                    !contractProcessService.insertIntoTable(request.getCode(), 4, request.getSigner())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ToWeb.error("分配签订人失败"));
            }

            return ResponseEntity.ok(ToWeb.success("角色分配成功"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ToWeb.error("分配过程中发生错误: " + e.getMessage()));
        }
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
        if (contractProcessService.finishProcess(c.getCode(), 1, ThreadLocalUtil.getTL(), c.getContent())) {
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
    public ResponseEntity<ToWeb> finishFinal(@RequestBody CounterSignR c){
        if(contractService.checkState(c.getCode())!=2){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // 400
                    .body(ToWeb.error("非法操作"));
        }
        if(contractProcessService.finishProcess(c.getCode(), 2, ThreadLocalUtil.getTL(), null)){
            contractService.changeContend(c.getCode(),c.getContent());
            updateProcess.updateTable(c.getCode());
            return ResponseEntity
                    .status(HttpStatus.OK) // 200
                    .body(ToWeb.success());
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                .body(ToWeb.error("数据错误"));
    }

    @PostMapping("/approve")
    public ResponseEntity<ToWeb> finishApprove(@RequestBody ApproveR r) {
        try {
            // 验证合同状态（必须为定稿完成状态）
            if (contractService.checkState(r.getCode()) != 3) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ToWeb.error("非法操作：只有定稿完成的合同才能审批"));
            }

            // 处理审批通过
            if ("approve".equalsIgnoreCase(r.getApprovalResult())) {
                if (!contractProcessService.finishProcess(r.getCode(), 3, ThreadLocalUtil.getTL(), r.getApprovalComment())) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(ToWeb.error("审批流程更新失败"));
                }
                updateProcess.updateTable(r.getCode());

                return ResponseEntity.ok()
                        .body(ToWeb.success("合同审批通过"));
            }
            // 处理审批拒绝
            else {
                contractProcessService.finishProcess(r.getCode(), 3, ThreadLocalUtil.getTL(), r.getApprovalComment());
                contractService.changeType(r.getCode(), 1);
                return ResponseEntity.ok()
                        .body(ToWeb.success("合同已拒绝并回滚到初始状态"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ToWeb.error("审批过程中发生错误: " + e.getMessage()));
        }
    }

    @PostMapping("/sign")
    public ResponseEntity<ToWeb> finishSign(@RequestBody SignR r) {
        try {
            // 验证合同状态（必须为审批完成状态）
            if (contractService.checkState(r.getCode()) != 4) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ToWeb.error("非法操作：只有审批完成的合同才能签订"));
            }

            // 记录签订流程
            if (!contractProcessService.finishProcess(r.getCode(), 4, ThreadLocalUtil.getTL(), r.toString())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ToWeb.error("签订流程记录失败"));
            }
            updateProcess.updateTable(r.getCode());
            return ResponseEntity.ok()
                    .body(ToWeb.success("合同签订成功"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ToWeb.error("签订过程中发生错误: " + e.getMessage()));
        }
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
            List<ContractProcess> contractProcesses = contractProcessService.findBytype(s,0);//起稿人
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
            contractProcesses = contractProcessService.findBytype(s,2);//定稿人
            for(ContractProcess c : contractProcesses){
                contractProcessS.setFinalizer(c.getUserName());
                contractProcessS.setFinalizetime(c.getTime().toString());
            }

            contractProcesses = contractProcessService.findBytype(s,4);//签订
            for(ContractProcess c : contractProcesses){
                contractProcessS.setSigner(contractProcessS.getSigner()==null?c.getUserName():contractProcessS.getSigner()+" "+c.getUserName());
                contractProcessS.setSigntime(c.getTime().toString());
                SignR signR = fromString(c.getContend());
                contractProcessS.setSignlocation(signR.getSignlocation());
                contractProcessS.setOurrepresentative(signR.getOurrepresentative());
                contractProcessS.setCustomerrepresentative(signR.getCustomerrepresentative());
                contractProcessS.setSignremark(signR.getRemarks());
            }
            contractProcessSES.add(contractProcessS);
        }
        return ResponseEntity
                .status(HttpStatus.OK) // 200
                .body(ToWeb.success(contractProcessSES));
    }

}
