package com.animal.controller;

import com.animal.common.PageResult;
import com.animal.common.Result;
import com.animal.entity.Owner;
import com.animal.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/owner")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @GetMapping
    public Result<List<Owner>> findAll() {
        return Result.success(ownerService.findAll());
    }

    @GetMapping("/page")
    public Result<PageResult<Owner>> findByPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(ownerService.findByPage(pageNum, pageSize));
    }

    @GetMapping("/{id}")
    public Result<Owner> findById(@PathVariable Integer id) {
        return Result.success(ownerService.findById(id));
    }

    @GetMapping("/search")
    public Result<List<Owner>> findByName(@RequestParam String name) {
        return Result.success(ownerService.findByName(name));
    }

    @GetMapping("/search/page")
    public Result<PageResult<Owner>> searchByPage(
            @RequestParam String name,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(ownerService.searchByPage(name, pageNum, pageSize));
    }

    @PostMapping
    public Result<Void> save(@RequestBody Owner owner) {
        return ownerService.save(owner) ? Result.success() : Result.error("添加失败");
    }

    @PutMapping
    public Result<Void> update(@RequestBody Owner owner) {
        return ownerService.update(owner) ? Result.success() : Result.error("修改失败");
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteById(@PathVariable Integer id) {
        return ownerService.deleteById(id) ? Result.success() : Result.error("删除失败");
    }
}
