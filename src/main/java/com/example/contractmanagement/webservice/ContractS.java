package com.example.contractmanagement.webservice;

import lombok.Data;

@Data
public class ContractS {
    int id;
    String name;
    String code;
    String customer;
    String beginDate;
    String endDate;
    String status;
    String content;
    int amount;
}
