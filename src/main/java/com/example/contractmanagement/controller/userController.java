package com.example.contractmanagement.controller;
import com.example.contractmanagement.Utils.JwtUtil;
import com.example.contractmanagement.Utils.ShowUsersByRole;
import com.example.contractmanagement.pojo.ToWeb;
import com.example.contractmanagement.pojo.User;
import com.example.contractmanagement.pojo.UserRight;
import com.example.contractmanagement.service.FunctionsService;
import com.example.contractmanagement.service.RoleService;
import com.example.contractmanagement.service.UserRightService;
import com.example.contractmanagement.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/user")
public class userController {
    @Autowired
    private userService userService;
    @Autowired
    private UserRightService userRightService;
    @Autowired
    private ShowUsersByRole showUsersByRole;
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

@GetMapping("/alldetail")
    public ToWeb showUsers(){
    List<UserRight> users = userRightService.getUsers();
    if(users.isEmpty()){
        return ToWeb.error("查询为空");
    }
    return ToWeb.success(users);
}
@GetMapping("/signdetail")
public ToWeb showSignUsers(){
   List<UserRight> result = showUsersByRole.showUsers("会签合同");
   if(result.isEmpty()){
       return ToWeb.error("查询为空");
   }
   return ToWeb.success(result);
}

@GetMapping("/contractdetail")
public ToWeb showContractUsers(){
    List<UserRight> result = showUsersByRole.showUsers("定稿合同");
    if(result.isEmpty()){
        return ToWeb.error("查询为空");
    }
    return ToWeb.success(result);
}

    @GetMapping("/approvedetail")
    public ToWeb showApproveUsers(){
        List<UserRight> result = showUsersByRole.showUsers("审批合同");
        if(result.isEmpty()){
            return ToWeb.error("查询为空");
        }
        return ToWeb.success(result);
    }


}
