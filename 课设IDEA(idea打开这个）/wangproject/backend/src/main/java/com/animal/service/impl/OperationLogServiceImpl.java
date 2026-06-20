package com.animal.service.impl;

import com.animal.common.PageResult;
import com.animal.entity.OperationLog;
import com.animal.mapper.OperationLogMapper;
import com.animal.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    public void save(OperationLog log) {
        operationLogMapper.insert(log);
    }

    @Override
    public List<OperationLog> findAll() {
        return operationLogMapper.findAll();
    }

    @Override
    public PageResult<OperationLog> findByPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<OperationLog> list = operationLogMapper.findByPage(offset, pageSize);
        long total = operationLogMapper.countAll();
        return new PageResult<>(list, total, pageNum, pageSize);
    }
}
