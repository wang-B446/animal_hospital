package com.animal.service.impl;

import com.animal.common.PageResult;
import com.animal.entity.Doctor;
import com.animal.mapper.DoctorMapper;
import com.animal.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Doctor> findAll() {
        return doctorMapper.findAll();
    }

    @Override
    public Doctor findById(Integer id) {
        return doctorMapper.findById(id);
    }

    @Override
    public List<Doctor> findByName(String name) {
        return doctorMapper.findByName(name);
    }

    @Override
    public boolean save(Doctor doctor) {
        return doctorMapper.insert(doctor) > 0;
    }

    @Override
    public boolean update(Doctor doctor) {
        return doctorMapper.update(doctor) > 0;
    }

    @Override
    public boolean deleteById(Integer id) {
        boolean result = doctorMapper.deleteById(id) > 0;
        if (result) {
            Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM doctor", Long.class);
            if (count != null && count == 0) {
                jdbcTemplate.execute("TRUNCATE TABLE doctor");
            } else {
                jdbcTemplate.execute("SET @rownum = 0");
                jdbcTemplate.execute("UPDATE doctor SET id = (@rownum := @rownum + 1) ORDER BY id");
                jdbcTemplate.execute("ALTER TABLE doctor AUTO_INCREMENT = 1");
            }
        }
        return result;
    }

    @Override
    public PageResult<Doctor> findByPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Doctor> list = doctorMapper.findByPage(offset, pageSize);
        long total = doctorMapper.countAll();
        return new PageResult<>(list, total, pageNum, pageSize);
    }

    @Override
    public PageResult<Doctor> searchByPage(String name, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Doctor> list = doctorMapper.searchByPage(name, offset, pageSize);
        long total = doctorMapper.countByName(name);
        return new PageResult<>(list, total, pageNum, pageSize);
    }
}
