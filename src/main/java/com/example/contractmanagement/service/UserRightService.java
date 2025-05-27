package com.example.contractmanagement.service;
import com.example.contractmanagement.pojo.User;
import com.example.contractmanagement.pojo.UserRight;

import java.util.List;

public interface UserRightService {
    UserRight findByName(String name);

    List<UserRight> getUsers();
    UserRight findByRole(String name);

}
