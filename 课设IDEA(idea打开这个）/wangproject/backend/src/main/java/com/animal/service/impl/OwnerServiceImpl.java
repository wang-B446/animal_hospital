package com.animal.service.impl;

import com.animal.common.PageResult;
import com.animal.entity.Owner;
import com.animal.mapper.OwnerMapper;
import com.animal.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OwnerServiceImpl implements OwnerService {

    @Autowired
    private OwnerMapper ownerMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Owner> findAll() {
        return ownerMapper.findAll();
    }

    @Override
    public Owner findById(Integer id) {
        return ownerMapper.findById(id);
    }

    @Override
    public List<Owner> findByName(String name) {
        return ownerMapper.findByName(name);
    }

    @Override
    public boolean save(Owner owner) {
        return ownerMapper.insert(owner) > 0;
    }

    @Override
    public boolean update(Owner owner) {
        return ownerMapper.update(owner) > 0;
    }

    @Override
    public boolean deleteById(Integer id) {
        boolean result = ownerMapper.deleteById(id) > 0;
        if (result) {
            Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM owner", Long.class);
            if (count != null && count == 0) {
                jdbcTemplate.execute("TRUNCATE TABLE owner");
            } else {
                jdbcTemplate.execute("SET @rownum = 0");
                jdbcTemplate.execute("UPDATE owner SET id = (@rownum := @rownum + 1) ORDER BY id");
                jdbcTemplate.execute("ALTER TABLE owner AUTO_INCREMENT = 1");
            }
        }
        return result;
    }

    @Override
    public PageResult<Owner> findByPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Owner> list = ownerMapper.findByPage(offset, pageSize);
        long total = ownerMapper.countAll();
        return new PageResult<>(list, total, pageNum, pageSize);
    }

    @Override
    public PageResult<Owner> searchByPage(String name, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Owner> list = ownerMapper.searchByPage(name, offset, pageSize);
        long total = ownerMapper.countByName(name);
        return new PageResult<>(list, total, pageNum, pageSize);
    }
}
