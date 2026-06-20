package com.animal.controller;

import com.animal.common.PageResult;
import com.animal.common.Result;
import com.animal.entity.MedicalRecord;
import com.animal.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/medical-record")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping
    public Result<List<MedicalRecord>> findAll() {
        return Result.success(medicalRecordService.findAll());
    }

    @GetMapping("/page")
    public Result<PageResult<MedicalRecord>> findByPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(medicalRecordService.findByPage(pageNum, pageSize));
    }

    @GetMapping("/{id}")
    public Result<MedicalRecord> findById(@PathVariable Integer id) {
        return Result.success(medicalRecordService.findById(id));
    }

    @GetMapping("/search")
    public Result<List<MedicalRecord>> search(@RequestParam String keyword) {
        return Result.success(medicalRecordService.search(keyword));
    }

    @GetMapping("/search/page")
    public Result<PageResult<MedicalRecord>> searchByPage(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(medicalRecordService.searchByPage(keyword, pageNum, pageSize));
    }

    @PostMapping
    public Result<Void> save(@RequestBody MedicalRecord medicalRecord) {
        return medicalRecordService.save(medicalRecord) ? Result.success() : Result.error("添加失败");
    }

    @PutMapping
    public Result<Void> update(@RequestBody MedicalRecord medicalRecord) {
        return medicalRecordService.update(medicalRecord) ? Result.success() : Result.error("修改失败");
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteById(@PathVariable Integer id) {
        return medicalRecordService.deleteById(id) ? Result.success() : Result.error("删除失败");
    }
}
