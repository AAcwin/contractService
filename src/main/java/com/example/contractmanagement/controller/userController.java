package com.example.contractmanagement.controller;
import com.example.contractmanagement.pojo.ToWeb;
import com.example.contractmanagement.pojo.User;
import com.example.contractmanagement.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
