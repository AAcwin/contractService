package com.example.contractmanagement.DTO;

import lombok.Data;

import java.io.File;

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
    String fileUrl;
}
