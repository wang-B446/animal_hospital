package com.animal.service.impl;

import com.animal.entity.User;
import com.animal.mapper.UserMapper;
import com.animal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<User> listAll() {
        return userMapper.findAll();
    }

    @Override
    public User getById(Integer id) {
        return userMapper.findById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public boolean register(User user) {
        // 检查用户名是否已存在
        User exist = userMapper.findByUsername(user.getUsername());
        if (exist != null) {
            return false; // 用户名已存在
        }
        user.setRole("admin");
        userMapper.insert(user);
        return true;
    }

    @Override
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
