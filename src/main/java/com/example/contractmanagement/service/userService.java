package com.example.contractmanagement.service;

import com.example.contractmanagement.pojo.User;

public interface userService {
    User findByName(String name);

    void register(String name, String password);
}
