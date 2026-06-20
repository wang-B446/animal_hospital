package com.animal.controller;

import com.animal.common.Result;
import com.animal.entity.StatVO;
import com.animal.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stat")
public class StatController {

    @Autowired
    private StatService statService;

    @GetMapping
    public Result<StatVO> getStatistics() {
        return Result.success(statService.getStatistics());
    }
}
