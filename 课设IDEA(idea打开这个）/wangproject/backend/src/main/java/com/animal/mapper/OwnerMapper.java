package com.animal.mapper;

import com.animal.entity.Owner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OwnerMapper {
    List<Owner> findAll();
    Owner findById(Integer id);
    List<Owner> findByName(String name);
    int insert(Owner owner);
    int update(Owner owner);
    int deleteById(Integer id);

    // 分页查询
    List<Owner> findByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);
    long countAll();
    List<Owner> searchByPage(@Param("name") String name, @Param("offset") int offset, @Param("pageSize") int pageSize);
    long countByName(@Param("name") String name);
}
