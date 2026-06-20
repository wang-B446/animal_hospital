package com.animal.service.impl;

import com.animal.common.PageResult;
import com.animal.entity.MedicalRecord;
import com.animal.mapper.MedicalRecordMapper;
import com.animal.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    @Autowired
    private MedicalRecordMapper medicalRecordMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<MedicalRecord> findAll() {
        return medicalRecordMapper.findAll();
    }

    @Override
    public MedicalRecord findById(Integer id) {
        return medicalRecordMapper.findById(id);
    }

    @Override
    public List<MedicalRecord> search(String keyword) {
        return medicalRecordMapper.search(keyword);
    }

    @Override
    public boolean save(MedicalRecord medicalRecord) {
        return medicalRecordMapper.insert(medicalRecord) > 0;
    }

    @Override
    public boolean update(MedicalRecord medicalRecord) {
        return medicalRecordMapper.update(medicalRecord) > 0;
    }

    @Override
    public boolean deleteById(Integer id) {
        boolean result = medicalRecordMapper.deleteById(id) > 0;
        if (result) {
            Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM medical_record", Long.class);
            if (count != null && count == 0) {
                jdbcTemplate.execute("TRUNCATE TABLE medical_record");
            } else {
                jdbcTemplate.execute("SET @rownum = 0");
                jdbcTemplate.execute("UPDATE medical_record SET id = (@rownum := @rownum + 1) ORDER BY id");
                jdbcTemplate.execute("ALTER TABLE medical_record AUTO_INCREMENT = 1");
            }
        }
        return result;
    }

    @Override
    public PageResult<MedicalRecord> findByPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<MedicalRecord> list = medicalRecordMapper.findByPage(offset, pageSize);
        long total = medicalRecordMapper.countAll();
        return new PageResult<>(list, total, pageNum, pageSize);
    }

    @Override
    public PageResult<MedicalRecord> searchByPage(String keyword, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<MedicalRecord> list = medicalRecordMapper.searchByPage(keyword, offset, pageSize);
        long total = medicalRecordMapper.countSearch(keyword);
        return new PageResult<>(list, total, pageNum, pageSize);
    }
}
