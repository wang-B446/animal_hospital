package com.animal.service;

import com.animal.entity.User;
import java.util.List;

public interface UserService {

    List<User> listAll();

    User getById(Integer id);

    User getByUsername(String username);

    boolean register(User user);

    User login(String username, String password);
}
