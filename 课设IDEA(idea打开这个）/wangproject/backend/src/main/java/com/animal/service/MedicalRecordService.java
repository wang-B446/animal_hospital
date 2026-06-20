package com.animal.service;

import com.animal.common.PageResult;
import com.animal.entity.MedicalRecord;
import java.util.List;

public interface MedicalRecordService {
    List<MedicalRecord> findAll();
    MedicalRecord findById(Integer id);
    List<MedicalRecord> search(String keyword);
    boolean save(MedicalRecord medicalRecord);
    boolean update(MedicalRecord medicalRecord);
    boolean deleteById(Integer id);

    // 分页查询
    PageResult<MedicalRecord> findByPage(int pageNum, int pageSize);
    PageResult<MedicalRecord> searchByPage(String keyword, int pageNum, int pageSize);
}
