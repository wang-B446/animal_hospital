package com.animal.mapper;

import com.animal.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OperationLogMapper {
    int insert(OperationLog log);
    List<OperationLog> findAll();

    // 分页查询
    List<OperationLog> findByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);
    long countAll();
}
