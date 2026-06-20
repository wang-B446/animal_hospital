package com.animal.mapper;

import com.animal.entity.MedicalRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface MedicalRecordMapper {
    List<MedicalRecord> findAll();
    MedicalRecord findById(Integer id);
    List<MedicalRecord> search(String keyword);
    int insert(MedicalRecord medicalRecord);
    int update(MedicalRecord medicalRecord);
    int deleteById(Integer id);

    // 分页查询
    List<MedicalRecord> findByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);
    long countAll();
    List<MedicalRecord> searchByPage(@Param("keyword") String keyword, @Param("offset") int offset, @Param("pageSize") int pageSize);
    long countSearch(@Param("keyword") String keyword);
}
