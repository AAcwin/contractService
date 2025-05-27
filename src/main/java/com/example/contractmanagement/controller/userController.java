package com.example.contractmanagement.controller;
import com.example.contractmanagement.Utils.JwtUtil;
import com.example.contractmanagement.pojo.ToWeb;
import com.example.contractmanagement.pojo.User;
import com.example.contractmanagement.pojo.UserRight;
import com.example.contractmanagement.service.URightService;
import com.example.contractmanagement.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

@RestController
@RequestMapping("/user")
public class userController {
    @Autowired
    private userService userService;
@PostMapping("/register")
    public ToWeb register(String username, String password){
        User u = userService.findByName(username);
        if(u != null){
            return ToWeb.error("用户名已存在");
        }
        userService.register(username,password);
        return  ToWeb.success();
    };

@PostMapping("/login")
public ToWeb login(String username,String password){
    User u = userService.findByName(username);
    if(u == null){
        return ToWeb.error("用户名不存在");
    }
    if (!Objects.equals(u.getPassword(), password)){
        return ToWeb.error("用户名或密码错误");
    }
    String claims = u.getUsername();
    String userInfo = JwtUtil.genToken(claims);
    Map<String, Object> result = new TreeMap<>();
    result.put("userInfo", userInfo);
    return ToWeb.success(result);
}
}
