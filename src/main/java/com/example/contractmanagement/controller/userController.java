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
import com.example.contractmanagement.webservice.UserS;
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
        System.out.println(u);
        // 用户名不存在
        if (u == null) {
            return ResponseEntity
                    .status(402)
                    .body(ToWeb.error("用户名不存在"));
        }

        // 密码错误
        if (!Objects.equals(u.getPassword(), r.getPassword())) {
            return ResponseEntity
                    .status(402)
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
    public ResponseEntity<ToWeb> showUsers() {
        List<UserRight> users = userRightService.getUsers();
        List<UserS> sendUsers = new ArrayList<>();

        if (users.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // 404
                    .body(ToWeb.error("查询为空"));
        }

        for (UserRight u : users) {
            UserS us = new UserS();
            us.setName(u.getUsername());
            us.setRole(u.getRolename());
            us.setPassword("***");
            us.setEmail("***");
            us.setPermission(permissionService.getByrolename(u.getUsername()));
            System.out.println(us);
            sendUsers.add(us);
        }

        return ResponseEntity
                .status(HttpStatus.OK) // 200
                .body(ToWeb.success(sendUsers));
    }

}
