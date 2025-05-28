package com.example.contractmanagement.service;

import com.example.contractmanagement.pojo.ContractProcess;

import java.util.List;

public interface ContractProcessService {
    boolean insertIntoTable(String num,int type,String user);
    List<ContractProcess> myContracts();
    boolean finishProcess(String connum,int type,String user, String contend);

}
