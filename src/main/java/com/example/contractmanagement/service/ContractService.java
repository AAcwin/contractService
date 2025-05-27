package com.example.contractmanagement.service;


public interface ContractService {
    boolean insertIntoTable(String contractname, String customername, String content, String starttime, String endtime);
}
