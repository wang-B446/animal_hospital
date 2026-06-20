package com.animal.service;

import com.animal.common.PageResult;
import com.animal.entity.Appointment;
import java.util.List;

public interface AppointmentService {
    List<Appointment> findAll();
    Appointment findById(Integer id);
    boolean save(Appointment appointment);
    boolean update(Appointment appointment);
    boolean deleteById(Integer id);

    // 分页查询
    PageResult<Appointment> findByPage(int pageNum, int pageSize);
}
