package com.animal.controller;

import com.animal.common.PageResult;
import com.animal.common.Result;
import com.animal.entity.Appointment;
import com.animal.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    public Result<List<Appointment>> findAll() {
        return Result.success(appointmentService.findAll());
    }

    @GetMapping("/page")
    public Result<PageResult<Appointment>> findByPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(appointmentService.findByPage(pageNum, pageSize));
    }

    @GetMapping("/{id}")
    public Result<Appointment> findById(@PathVariable Integer id) {
        return Result.success(appointmentService.findById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody Appointment appointment) {
        return appointmentService.save(appointment) ? Result.success() : Result.error("添加失败");
    }

    @PutMapping
    public Result<Void> update(@RequestBody Appointment appointment) {
        return appointmentService.update(appointment) ? Result.success() : Result.error("修改失败");
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteById(@PathVariable Integer id) {
        return appointmentService.deleteById(id) ? Result.success() : Result.error("删除失败");
    }
}
