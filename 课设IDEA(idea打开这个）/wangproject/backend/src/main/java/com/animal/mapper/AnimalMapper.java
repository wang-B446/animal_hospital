package com.animal.mapper;

import com.animal.entity.Animal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AnimalMapper {
    List<Animal> findAll();
    Animal findById(Integer id);
    List<Animal> findByName(String name);
    int insert(Animal animal);
    int update(Animal animal);
    int deleteById(Integer id);

    // 分页查询
    List<Animal> findByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);
    long countAll();
    List<Animal> searchByPage(@Param("name") String name, @Param("offset") int offset, @Param("pageSize") int pageSize);
    long countByName(@Param("name") String name);
}
