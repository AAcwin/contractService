package com.example.contractmanagement.service;


public interface ContractService {
    String insertIntoTable(String contractname, String customername, String content, String starttime, String endtime);
    boolean finishC(String connum,String contend);
}
