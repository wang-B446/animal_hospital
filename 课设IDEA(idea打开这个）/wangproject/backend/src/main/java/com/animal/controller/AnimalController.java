package com.animal.controller;

import com.animal.common.PageResult;
import com.animal.common.Result;
import com.animal.entity.Animal;
import com.animal.service.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/animal")
public class AnimalController {

    @Autowired
    private AnimalService animalService;

    @GetMapping
    public Result<List<Animal>> findAll() {
        return Result.success(animalService.findAll());
    }

    @GetMapping("/page")
    public Result<PageResult<Animal>> findByPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(animalService.findByPage(pageNum, pageSize));
    }

    @GetMapping("/{id}")
    public Result<Animal> findById(@PathVariable Integer id) {
        return Result.success(animalService.findById(id));
    }

    @GetMapping("/search")
    public Result<List<Animal>> findByName(@RequestParam String name) {
        return Result.success(animalService.findByName(name));
    }

    @GetMapping("/search/page")
    public Result<PageResult<Animal>> searchByPage(
            @RequestParam String name,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(animalService.searchByPage(name, pageNum, pageSize));
    }

    @PostMapping
    public Result<Void> save(@RequestBody Animal animal) {
        return animalService.save(animal) ? Result.success() : Result.error("添加失败");
    }

    @PutMapping
    public Result<Void> update(@RequestBody Animal animal) {
        return animalService.update(animal) ? Result.success() : Result.error("修改失败");
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteById(@PathVariable Integer id) {
        return animalService.deleteById(id) ? Result.success() : Result.error("删除失败");
    }
}
