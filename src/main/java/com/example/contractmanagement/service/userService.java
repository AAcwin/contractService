package com.example.contractmanagement.service;

import com.example.contractmanagement.pojo.User;

import java.util.List;

public interface userService {
    User findByName(String name);

    void register(String name, String password);

}
