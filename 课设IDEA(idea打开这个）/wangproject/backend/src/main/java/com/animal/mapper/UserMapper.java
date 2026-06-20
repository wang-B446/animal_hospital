package com.animal.mapper;

import com.animal.entity.User;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface UserMapper {

    List<User> findAll();

    User findById(Integer id);

    User findByUsername(String username);

    void insert(User user);

    int update(User user);

    int deleteById(Integer id);
}
