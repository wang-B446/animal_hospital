package com.animal.controller;

import com.animal.common.PageResult;
import com.animal.common.Result;
import com.animal.entity.Doctor;
import com.animal.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public Result<List<Doctor>> findAll() {
        return Result.success(doctorService.findAll());
    }

    @GetMapping("/page")
    public Result<PageResult<Doctor>> findByPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(doctorService.findByPage(pageNum, pageSize));
    }

    @GetMapping("/{id}")
    public Result<Doctor> findById(@PathVariable Integer id) {
        return Result.success(doctorService.findById(id));
    }

    @GetMapping("/search")
    public Result<List<Doctor>> findByName(@RequestParam String name) {
        return Result.success(doctorService.findByName(name));
    }

    @GetMapping("/search/page")
    public Result<PageResult<Doctor>> searchByPage(
            @RequestParam String name,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(doctorService.searchByPage(name, pageNum, pageSize));
    }

    @PostMapping
    public Result<Void> save(@RequestBody Doctor doctor) {
        return doctorService.save(doctor) ? Result.success() : Result.error("添加失败");
    }

    @PutMapping
    public Result<Void> update(@RequestBody Doctor doctor) {
        return doctorService.update(doctor) ? Result.success() : Result.error("修改失败");
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteById(@PathVariable Integer id) {
        return doctorService.deleteById(id) ? Result.success() : Result.error("删除失败");
    }
}
