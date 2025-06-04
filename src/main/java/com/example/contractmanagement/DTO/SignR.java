package com.example.contractmanagement.DTO;

import lombok.Data;

@Data
public class SignR {
    private String code;                  // 合同编号
    private String signtime;              // 签订时间
    private String signlocation;          // 签订地点
    private String ourrepresentative;     // 我方代表
    private String customerrepresentative; // 客户代表
    private String remarks;
    // 备注
}
