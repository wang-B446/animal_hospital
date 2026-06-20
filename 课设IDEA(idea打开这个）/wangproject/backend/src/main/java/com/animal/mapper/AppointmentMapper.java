package com.animal.mapper;

import com.animal.entity.Appointment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AppointmentMapper {
    List<Appointment> findAll();
    Appointment findById(Integer id);
    int insert(Appointment appointment);
    int update(Appointment appointment);
    int deleteById(Integer id);

    // 分页查询
    List<Appointment> findByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);
    long countAll();
}
