package com.animal.service;

import com.animal.common.PageResult;
import com.animal.entity.Animal;
import java.util.List;

public interface AnimalService {
    List<Animal> findAll();
    Animal findById(Integer id);
    List<Animal> findByName(String name);
    boolean save(Animal animal);
    boolean update(Animal animal);
    boolean deleteById(Integer id);

    // 分页查询
    PageResult<Animal> findByPage(int pageNum, int pageSize);
    PageResult<Animal> searchByPage(String name, int pageNum, int pageSize);
}
