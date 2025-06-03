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
import org.springframework.util.StringUtils;
import java.util.List;
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

    /**
     * 管理员查询所有用户
     */
    @GetMapping("/admin/users")
    public ToWeb getAllUsers() {
        // 这里可以添加管理员权限验证
        List<User> users = userService.getAllUsers();
        return ToWeb.success(users);
    }

    /**
     * 管理员添加用户
     */
    @PostMapping("/admin/add")
    public ToWeb addUser(@RequestBody User user) {
        // 验证必填字段
        if (!StringUtils.hasText(user.getUsername())) {
            return ToWeb.error("用户名不能为空");
        }
        if (!StringUtils.hasText(user.getPassword())) {
            return ToWeb.error("密码不能为空");
        }

        // 检查用户名是否已存在
        if (userService.getUserByUsername(user.getUsername()) != null) {
            return ToWeb.error("用户名已存在");
        }

        boolean success = userService.addUser(user);
        if (success) {
            return ToWeb.success("用户添加成功");
        } else {
            return ToWeb.error("用户添加失败");
        }
    }

    /**
     * 管理员删除用户
     */
    @PostMapping("/admin/delete")
    public ToWeb deleteUser(@RequestParam String username) {
        if (!StringUtils.hasText(username)) {
            return ToWeb.error("用户名不能为空");
        }

        boolean success = userService.deleteUser(username);
        if (success) {
            return ToWeb.success("用户删除成功");
        } else {
            return ToWeb.error("用户删除失败或用户不存在");
        }
    }

    /**
     * 管理员更新用户信息
     */
    @PostMapping("/admin/update")
    public ToWeb updateUser(@RequestBody User user) {
        if (!StringUtils.hasText(user.getUsername())) {
            return ToWeb.error("用户名不能为空");
        }

        boolean success = userService.updateUser(user);
        if (success) {
            return ToWeb.success("用户信息更新成功");
        } else {
            return ToWeb.error("用户信息更新失败");
        }
    }

    /**
     * 管理员查询特定用户
     */
    @GetMapping("/admin/detail")
    public ToWeb getUserDetail(@RequestParam String username) {
        if (!StringUtils.hasText(username)) {
            return ToWeb.error("用户名不能为空");
        }

        User user = userService.getUserByUsername(username);
        if (user != null) {
            return ToWeb.success(user);
        } else {
            return ToWeb.error("用户不存在");
        }
    }

}
