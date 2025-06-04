package com.example.contractmanagement.webservice;

import lombok.Data;

@Data
public class ContractProcessS {
    private String code;
    private String status;
    private String drafter;
    private String drafttime;
    private String cosigner;
    private String cosigntime;
    private String cosigncontent;
    private String approver;
    private String approvetime;
    private String finalizer;
    private String finalizetime;
    private String signer;
    private String signtime;
    private String signlocation;
    private String ourrepresentative;
    private String customerrepresentative;
    private String signremark;
}
