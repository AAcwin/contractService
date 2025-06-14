package com.example.contractmanagement.service;

import com.example.contractmanagement.pojo.ContractProcess;

import java.util.List;
import java.util.Set;

public interface ContractProcessService {
    boolean insertIntoTable(String num,int type,String user);
    List<ContractProcess> myContracts();
    boolean finishProcess(String connum,int type,String user, String contend);
    void finalProcess(String connum);
    void changeState(String connum,int state);
    List<ContractProcess> findMessage(String contractnum,int type);
    List<ContractProcess> findBytype(String contractnum,int type);

    Set<String> getAllNum();

    void deleteContract(String code);
}
