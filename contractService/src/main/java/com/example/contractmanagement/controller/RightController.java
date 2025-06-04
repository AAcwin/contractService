package com.example.contractmanagement.controller;

import com.example.contractmanagement.pojo.ToWeb;
import com.example.contractmanagement.service.UserRightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/right")
public class RightController {
    @Autowired
    UserRightService userRightService;
    @PostMapping("/assign")
    public ToWeb assign(String username,String rolename){
        if(userRightService.changeRole(username,rolename)){
            return ToWeb.success();
        }
        return ToWeb.error("修改失败");
    }
}
