package com.animal.service;

import com.animal.common.PageResult;
import com.animal.entity.OperationLog;
import java.util.List;

public interface OperationLogService {
    void save(OperationLog log);
    List<OperationLog> findAll();

    // 分页查询
    PageResult<OperationLog> findByPage(int pageNum, int pageSize);
}
