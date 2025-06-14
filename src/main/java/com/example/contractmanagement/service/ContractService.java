package com.example.contractmanagement.service;


import com.example.contractmanagement.pojo.Contract;

import java.util.List;

public interface ContractService {
    String insertIntoTable(String contractname, String customername, String content, String starttime, String endtime);
    boolean finishC(String connum,String contend);
    void changeType(String connum,int type);
    void changeContend(String connum,String contend);
    int checkState(String connum);
    List<Contract> showConstracts();

    List<Contract> findByType(int type);

    List<Contract> findByUser();
    List<Contract> findByMyType(int type);
    void insertUrl(String uid,String url);

    void deleteContract(String code);
}
