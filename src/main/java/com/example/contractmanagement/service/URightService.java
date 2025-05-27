package com.example.contractmanagement.service;
import com.example.contractmanagement.pojo.UserRight;

public interface URightService {
    UserRight findByName(String name);

}
