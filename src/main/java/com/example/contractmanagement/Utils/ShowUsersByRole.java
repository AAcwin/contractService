package com.example.contractmanagement.Utils;

import com.example.contractmanagement.pojo.ToWeb;
import com.example.contractmanagement.pojo.UserRight;
import com.example.contractmanagement.service.FunctionsService;
import com.example.contractmanagement.service.RoleService;
import com.example.contractmanagement.service.UserRightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShowUsersByRole {
    @Autowired
    FunctionsService functionsService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserRightService userRightService;
    public List<UserRight> showUsers(String rolename){
        int funId = functionsService.findByName(rolename);
        List<String> users = roleService.findByFunctionId(funId);
        List<UserRight> result = new ArrayList<>();
        for (String user : users){
            UserRight ur =userRightService.findByRole(user);
            if(ur != null){
                result.add(ur);
            }
        }
        return result;
    }
}
