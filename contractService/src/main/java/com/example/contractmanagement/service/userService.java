package com.example.contractmanagement.service;

import com.example.contractmanagement.pojo.User;
import java.util.List;

public interface userService {

    // 原有的方法
    User findByName(String name);
    void register(String name, String password,String email);

    // 新增的管理员功能方法
    List<User> getAllUsers();
    boolean addUser(User user);
    boolean deleteUser(String username);
    boolean updateUser(User user);
    User getUserByUsername(String username);
}