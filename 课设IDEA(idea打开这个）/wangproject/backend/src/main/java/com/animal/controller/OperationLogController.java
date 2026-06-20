package com.animal.controller;

import com.animal.common.PageResult;
import com.animal.common.Result;
import com.animal.entity.OperationLog;
import com.animal.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/log")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    @GetMapping
    public Result<List<OperationLog>> findAll() {
        return Result.success(operationLogService.findAll());
    }

    @GetMapping("/page")
    public Result<PageResult<OperationLog>> findByPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(operationLogService.findByPage(pageNum, pageSize));
    }
}
