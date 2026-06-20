package com.animal.service;

import com.animal.common.PageResult;
import com.animal.entity.Doctor;
import java.util.List;

public interface DoctorService {
    List<Doctor> findAll();
    Doctor findById(Integer id);
    List<Doctor> findByName(String name);
    boolean save(Doctor doctor);
    boolean update(Doctor doctor);
    boolean deleteById(Integer id);

    // 分页查询
    PageResult<Doctor> findByPage(int pageNum, int pageSize);
    PageResult<Doctor> searchByPage(String name, int pageNum, int pageSize);
}
