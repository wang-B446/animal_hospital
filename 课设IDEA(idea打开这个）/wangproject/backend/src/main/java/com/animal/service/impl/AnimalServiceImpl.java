package com.animal.service.impl;

import com.animal.common.PageResult;
import com.animal.entity.Animal;
import com.animal.mapper.AnimalMapper;
import com.animal.service.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnimalServiceImpl implements AnimalService {

    @Autowired
    private AnimalMapper animalMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Animal> findAll() {
        return animalMapper.findAll();
    }

    @Override
    public Animal findById(Integer id) {
        return animalMapper.findById(id);
    }

    @Override
    public List<Animal> findByName(String name) {
        return animalMapper.findByName(name);
    }

    @Override
    public boolean save(Animal animal) {
        return animalMapper.insert(animal) > 0;
    }

    @Override
    public boolean update(Animal animal) {
        return animalMapper.update(animal) > 0;
    }

    @Override
    public boolean deleteById(Integer id) {
        boolean result = animalMapper.deleteById(id) > 0;
        if (result) {
            // 检查表中是否还有数据
            Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM animal", Long.class);
            if (count != null && count == 0) {
                // 表为空时，重置AUTO_INCREMENT为1
                jdbcTemplate.execute("TRUNCATE TABLE animal");
            } else {
                // 表不为空时，重新整理ID并重置
                jdbcTemplate.execute("SET @rownum = 0");
                jdbcTemplate.execute("UPDATE animal SET id = (@rownum := @rownum + 1) ORDER BY id");
                jdbcTemplate.execute("ALTER TABLE animal AUTO_INCREMENT = 1");
            }
        }
        return result;
    }

    @Override
    public PageResult<Animal> findByPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Animal> list = animalMapper.findByPage(offset, pageSize);
        long total = animalMapper.countAll();
        return new PageResult<>(list, total, pageNum, pageSize);
    }

    @Override
    public PageResult<Animal> searchByPage(String name, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Animal> list = animalMapper.searchByPage(name, offset, pageSize);
        long total = animalMapper.countByName(name);
        return new PageResult<>(list, total, pageNum, pageSize);
    }
}
