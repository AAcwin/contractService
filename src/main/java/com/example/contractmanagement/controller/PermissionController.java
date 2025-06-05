package com.example.contractmanagement.controller;

import com.example.contractmanagement.DTO.PermisionR;
import com.example.contractmanagement.Utils.ThreadLocalUtil;
import com.example.contractmanagement.pojo.ToWeb;
import com.example.contractmanagement.service.PermissionService;
import com.example.contractmanagement.service.RoleService;
import com.example.contractmanagement.service.UserRightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/role")
public class PermissionController {
    @Autowired
    UserRightService userRightService;
    @Autowired
    PermissionService permissionService;
    @PutMapping("/{username}")
    public ResponseEntity updateUserRoleAndPermissions(
            @PathVariable String username,
            @RequestBody PermisionR request) {
        userRightService.changeRole(username,request.getRole());
        permissionService.changePermission(username,request.getPermission());
        return ResponseEntity.ok("");
    }
}
