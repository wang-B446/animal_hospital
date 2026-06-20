package com.animal.controller;

import com.animal.common.Result;
import com.animal.entity.User;
import com.animal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器 - 登录注册
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody User loginUser) {
        User user = userService.login(loginUser.getUsername(), loginUser.getPassword());
        if (user != null) {
            // 返回用户信息（不返回密码）
            user.setPassword(null);
            Map<String, Object> data = new HashMap<>();
            data.put("user", user);
            return Result.success(data);
        }
        return Result.error("用户名或密码错误");
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return Result.error("密码不能为空");
        }
        boolean success = userService.register(user);
        if (success) {
            return Result.success(null, "注册成功");
        }
        return Result.error("用户名已存在");
    }

    /**
     * 获取所有用户列表
     */
    @GetMapping("/list")
    public Result list() {
        List<User> list = userService.listAll();
        return Result.success(list);
    }
}
