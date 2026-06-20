package com.animal.service;

import com.animal.common.PageResult;
import com.animal.entity.Owner;
import java.util.List;

public interface OwnerService {
    List<Owner> findAll();
    Owner findById(Integer id);
    List<Owner> findByName(String name);
    boolean save(Owner owner);
    boolean update(Owner owner);
    boolean deleteById(Integer id);

    // 分页查询
    PageResult<Owner> findByPage(int pageNum, int pageSize);
    PageResult<Owner> searchByPage(String name, int pageNum, int pageSize);
}
