package com.animal.mapper;

import com.animal.entity.Doctor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DoctorMapper {
    List<Doctor> findAll();
    Doctor findById(Integer id);
    List<Doctor> findByName(String name);
    int insert(Doctor doctor);
    int update(Doctor doctor);
    int deleteById(Integer id);

    // 分页查询
    List<Doctor> findByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);
    long countAll();
    List<Doctor> searchByPage(@Param("name") String name, @Param("offset") int offset, @Param("pageSize") int pageSize);
    long countByName(@Param("name") String name);
}
