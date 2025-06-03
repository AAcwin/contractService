package com.example.contractmanagement.controller;
import com.example.contractmanagement.Utils.JwtUtil;
import com.example.contractmanagement.pojo.ToWeb;
import com.example.contractmanagement.pojo.User;
import com.example.contractmanagement.pojo.UserRight;
import com.example.contractmanagement.service.PermissionService;
import com.example.contractmanagement.webservice.LoginR;
import com.example.contractmanagement.webservice.LoginS;
import com.example.contractmanagement.webservice.UserR;
import com.example.contractmanagement.service.UserRightService;
import com.example.contractmanagement.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
public class userController {
    @Autowired
    private userService userService;
    @Autowired
    private UserRightService userRightService;
    @Autowired
    private PermissionService permissionService;
    @PostMapping("/register")
    public ResponseEntity<ToWeb> register(@RequestBody UserR request) {
        User u = userService.findByName(request.getUsername());
        if (u != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)  // 409 Conflict
                    .body(ToWeb.error("用户名已存在"));
        }

        userService.register(request.getUsername(), request.getPassword(), request.getEmail());
        return ResponseEntity
                .status(200)  // 201 Created
                .body(ToWeb.success());
    }

    @PostMapping("/login")
    public ResponseEntity<ToWeb> login(@RequestBody LoginR r) {
        User u = userService.findByName(r.getUsername());

        // 用户名不存在
        if (u == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED) // 401 Unauthorized
                    .body(ToWeb.error("用户名不存在"));
        }

        // 密码错误
        if (!Objects.equals(u.getPassword(), r.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED) // 401 Unauthorized
                    .body(ToWeb.error("用户名或密码错误"));
        }

        // 登录成功
        String claims = u.getUsername();
        String userInfo = JwtUtil.genToken(claims);

        LoginS s = new LoginS();
        s.setName(r.getUsername());
        s.setAuthorized(userInfo);
        s.setRole(userRightService.findByName(r.getUsername()).getRolename());
        s.setPermission(permissionService.getByrolename(r.getUsername()));

        return ResponseEntity
                .status(HttpStatus.OK) // 200 OK
                .body(ToWeb.success(s));
    }

@GetMapping("/alldetail")
    public ToWeb showUsers(){
    List<UserRight> users = userRightService.getUsers();
    if(users.isEmpty()){
        return ToWeb.error("查询为空");
    }
    return ToWeb.success(users);
}


}
